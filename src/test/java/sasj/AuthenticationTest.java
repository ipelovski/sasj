package sasj;

import sasj.data.user.JournalUserDetailsService;
import sasj.data.user.User;
import sasj.data.user.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationTest {
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JournalUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @After
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void authenticate() {
        User admin = new User("admin");
        admin.setEmail("admin@adminland.com");
        admin.setRole(User.Role.admin);
        admin.setPassword(passwordEncoder.encode("admin_pass"));
        userRepository.save(admin);
        Authentication token = new UsernamePasswordAuthenticationToken(
            "admin", "admin_pass");
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        authenticationManager = new ProviderManager(Collections.singletonList(daoAuthenticationProvider));
        Authentication authentication = authenticationManager.authenticate(token);
        Assert.assertTrue(authentication.isAuthenticated());
    }
}
