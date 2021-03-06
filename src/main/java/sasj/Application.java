package sasj;

import sasj.data.user.JournalUserDetailsService;
import sasj.web.UrlLocaleResolver;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;

import java.sql.SQLException;
import java.text.DecimalFormat;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public JournalUserDetailsService userDetailsService() {
        return new JournalUserDetailsService();
    }

    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new UrlLocaleResolver();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/WEB-INF/i18n/words");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Profile("dev")
    public Server h2WebConsole() throws SQLException {
        return Server.createWebServer(
            "-web", "-webAllowOthers", "-webDaemon", "-webPort", "8082");
    }

    @Bean
    public DecimalFormat scoreFormatter() {
        return new DecimalFormat("#.00");
    }
}
