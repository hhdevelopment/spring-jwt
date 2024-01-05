package fr.hhdev.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

import fr.hhdev.sample.jwt.JwtSecurityContextRepository;

@Configuration
@EnableReactiveMethodSecurity()
public class WebSecurityConfig {

  private JwtSecurityContextRepository securityContextRepository;
  
  public WebSecurityConfig(JwtSecurityContextRepository securityContextRepository) {
    this.securityContextRepository = securityContextRepository;
  }

  @Bean
  GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("ROLE_");
  }

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.authorizeExchange((AuthorizeExchangeSpec exchanges) -> {
      exchanges.anyExchange().permitAll(); // Access is allowed because we will use @PreAuthorize on methods
    });
    http.formLogin((login) -> login.disable()).httpBasic((login) -> login.disable()) // disable form login and basic auth
        .securityContextRepository(securityContextRepository); // set the security context repository
    http.logout((logout) -> logout.disable()); // disable logout no sense in a stateless context
    return http.build();
  }
}
