package USCitizenshipFlashcard.config;

import USCitizenshipFlashcard.jpa.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    //return 403 if the user is not logged in
    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() { return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);}

    //Defined security role for end points
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           UserRepository userRepository) throws Exception {

        httpSecurity.authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/api/logout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers( "/api/users/**").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/**")).authenticated())
                .addFilterBefore(new AuthenticationFilter(userRepository), UsernamePasswordAuthenticationFilter.class)
                // csrf => Cross-Site Request Forgery
                .csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
}
