package bezbednosttim6.security;

import bezbednosttim6.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
// Injektovanje bean-a za bezbednost 
@EnableWebSecurity

// Ukljucivanje podrske za anotacije "@Pre*" i "@Post*" koje ce aktivirati autorizacione provere za svaki pristup metodi
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Autowired
	private UserRepository userRepository;
	// Servis koji se koristi za citanje podataka o korisnicima aplikacije
	@Autowired
    public UserDetailsService userDetailsService() {return new CustomUserDetailsService(userRepository);}
	
	// Implementacija PasswordEncoder-a koriscenjem BCrypt hashing funkcije.
	// BCrypt po defalt-u radi 10 rundi hesiranja prosledjene vrednosti.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	// 1. koji servis da koristi da izvuce podatke o korisniku koji zeli da se autentifikuje
	// prilikom autentifikacije, AuthenticationManager ce sam pozivati loadUserByUsername() metodu ovog servisa
	// 2. kroz koji enkoder da provuce lozinku koju je dobio od klijenta u zahtevu
	// da bi adekvatan hash koji dobije kao rezultat hash algoritma uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain lozinka)
	@Transactional
 	@Bean
 	public DaoAuthenticationProvider authenticationProvider() {
 	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
 	}
	 // Registrujemo authentication manager koji ce da uradi autentifikaciju korisnika za nas
 	@Bean
 	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
 	    return authConfig.getAuthenticationManager();
 	}
	 // Injektujemo implementaciju iz TokenUtils klase kako bismo mogli da koristimo njene metode za rad sa JWT u TokenAuthenticationFilteru
	@Autowired
	private TokenUtils tokenUtils;
	// Definisemo prava pristupa za zahteve ka odredjenim URL-ovima/rutama
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	    http.authorizeRequests()
	        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	        .requestMatchers("/api/user/register", "/api/user/login/first","/api/user/login/second", "/api/user/loginWithGoogle", "/api/user/activate/*", "/api/user/activate/resend/*", "/api/user/resetPassword", "/api/user/refreshToken", "/api/user/renewPassword", "/api/user/recaptcha", "/api/cert/hello").permitAll()
	        .anyRequest().authenticated()
	        .and()
	        .cors().and()
	        .addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userDetailsService()), BasicAuthenticationFilter.class);

	    http.csrf().disable();
	    http.headers().frameOptions().disable();
	    http.authenticationProvider(authenticationProvider());

	    return http.build();
	}

	// metoda u kojoj se definisu putanje za igorisanje autentifikacije
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    	// Autentifikacija ce biti ignorisana ispod navedenih putanja (kako bismo ubrzali pristup resursima)
    	// Zahtevi koji se mecuju za web.ignoring().antMatchers() nemaju pristup SecurityContext-u
    	// Dozvoljena POST metoda na ruti /auth/login, za svaki drugi tip HTTP metode greska je 401 Unauthorized
    	return (web) -> web.ignoring().requestMatchers(HttpMethod.POST, "/api/auth/**", "/socket/**")
				// Ovim smo dozvolili pristup statickim resursima aplikacije
    			.requestMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico",
    			"/*/*.html", "/*/*.css", "/*/*.js", "/socket/**");
	}
}