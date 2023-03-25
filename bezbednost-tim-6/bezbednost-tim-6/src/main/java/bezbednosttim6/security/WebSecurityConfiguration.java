package bezbednosttim6.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // <-- Obavezno za @PreAuthorize
public class WebSecurityConfiguration {
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests() // csrf->disabled, pošto nam JWT odrađuje zaštitu od CSRF napada
				.requestMatchers("/*").permitAll().requestMatchers("/api/unregisteredUser/","/api/user/login","/api/passenger/activate/*","/api/passenger/activate/resend/*","/api/user/resetPassword").permitAll() // statički html i login mogu svi da pozovu
				.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll() //dozvolimo options request da prodje uvek
				.requestMatchers(HttpMethod.POST,"/api/passenger").permitAll() //dozvolimo registraciju korisnika naravno
				.requestMatchers("/api/**").authenticated() // sav pristup API-ju mora da bude autentikovan
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // ne koristimo HttpSession i kukije
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // JWT procesiramo pre autentikacije
		http.csrf().disable();
	    http.headers().frameOptions().disable();

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();// PasswordEncoderFactories.createDelegatingPasswordEncoder();
		//System.out.println("enkodiraj 'admin': "+encoder.encode("admin"));
		return encoder;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
