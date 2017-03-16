package di;

import accountService.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by magomed on 19.02.17.
 */
@Configuration
public class ServiceConfiguration {
    @Bean
    @Scope(value = "singleton")
    public AccountService accountService() {
        return new AccountService();
    }
}
