package de.milchreis.phobox.server;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.config.StorageConfiguration;
import de.milchreis.phobox.core.model.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
    private AuthenticationEntryPoint authEntryPoint;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		http
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.and()
			.authorizeRequests()
				.antMatchers("/console/**").permitAll()
			.and().cors();
		
		
		http.csrf().disable();
		http.headers().frameOptions().disable();

		StorageConfiguration config = Phobox.getModel().getStorageConfiguration();
		List<UserCredentials> credentials = config.getLoginCredentials();

		if(credentials != null && credentials.size() > 0) {
			http.authorizeRequests().anyRequest().authenticated();
			http.httpBasic().authenticationEntryPoint(authEntryPoint);
		}
    }
    
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowUrlEncodedPercent(true);
        return firewall;
    }
    
    @Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8070"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "x-requested-with"));
		configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
        return source;
	}
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
 
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		StorageConfiguration config = Phobox.getModel().getStorageConfiguration();
		List<UserCredentials> credentials = config.getLoginCredentials();

        if(credentials != null && credentials.size() > 0) {
        	InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> mngConfig = auth.inMemoryAuthentication();

        	for(UserCredentials login : credentials) {

				UserDetails user = User
						.withUsername(login.getUsername())
						.password(login.getPassword())
						.roles("USER")
						.build();

				mngConfig.withUser(user);
			}
        }
    }
    
}
