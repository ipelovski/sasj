package emsbj.user;

import emsbj.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class UserService {
    private static final String modelAttributeName = "activeUser";
    private final Map<Locale, ActiveUser> anonymousMap = new HashMap<>();
    private AuthenticationTrustResolver authenticationTrustResolver;
    private MessageSource messageSource;

    @Autowired
    public UserService(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.authenticationTrustResolver = new AuthenticationTrustResolverImpl();
        for (String localeName : Application.supportedLocales) {
            Locale locale = Locale.forLanguageTag(localeName);
            anonymousMap.put(locale, createAnonymous(locale));
        }
    }

    public void setActiveUser(Model model, Locale locale) {
        model.addAttribute(modelAttributeName, getActiveUser(locale));
    }

    private ActiveUser getActiveUser(Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAnonymous = authenticationTrustResolver.isAnonymous(authentication);
        if (isAnonymous) {
            return getAnonymous(locale);
        }
        else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return new ActiveUser(userDetails);
        } else {
            throw new IllegalStateException("Unknown principal type "
                + authentication.getPrincipal().getClass().getCanonicalName());
        }
    }

    private ActiveUser createAnonymous(Locale locale) {
        String username = messageSource.getMessage("guestName", null, locale);
        User anonymousUser = new User(username);
        anonymousUser.setRole(User.Role.anonymous);
        return new ActiveUser(anonymousUser);
    }

    private ActiveUser getAnonymous(Locale locale) {
        if (anonymousMap.containsKey(locale)) {
            return anonymousMap.get(locale);
        } else {
            throw new IllegalArgumentException("Not supported locale " + locale);
        }
    }
}