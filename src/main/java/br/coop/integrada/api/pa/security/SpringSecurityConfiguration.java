package br.coop.integrada.api.pa.security;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import br.coop.integrada.api.pa.filter.AuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfigurationSource;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@SuppressWarnings("deprecation")
@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter{
	private final String[] WHITELIST_PATTERNS = {"/", "/swagger-pa-portal/**", "/index.html", "/javainuse-openapi/**","/api/genesis/**"};
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.cors();
		http.headers().addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy","script-src 'self'"))
            .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "*"))
            .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE"))
            .addHeaderWriter(new StaticHeadersWriter("Access-Control-Max-Age", "3600"))
            .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers", "x-requested-with"))
            .and().authorizeRequests().antMatchers(WHITELIST_PATTERNS).permitAll();
		
		//http.authorizeRequests().antMatchers("/api/pa/v1/produto/tipo/ativos").hasAnyRole("Administrador");
		http.authorizeRequests().anyRequest().authenticated();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		//http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true).expiredUrl("/authentication/login").sessionRegistry(sessionRegistry());
		
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	/* #controle de sessão #
	@Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }*/
}
