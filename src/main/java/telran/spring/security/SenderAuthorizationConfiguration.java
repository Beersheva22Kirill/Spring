package telran.spring.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SenderAuthorizationConfiguration implements AuthorizationConfiguration {


	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(custom -> custom.requestMatchers(HttpMethod.GET).authenticated()
				.anyRequest().hasRole("ADMIN"));

	}

}
