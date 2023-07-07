package br.coop.integrada.api.pa.filter;

import java.util.Map;
import com.auth0.jwt.JWT;
import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import com.auth0.jwt.JWTVerifier;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import static java.util.Arrays.stream;
import org.springframework.http.MediaType;
import com.auth0.jwt.algorithms.Algorithm;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.filter.OncePerRequestFilter;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {		
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String token = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC512("463408a1-54c9-4307-bb1c-6cced559f5a7");
				JWTVerifier verifier = JWT.require(algorithm).build();
				
				DecodedJWT decodeJWT = verifier.verify(token);
				
				String username = decodeJWT.getSubject();
				String[] roles = decodeJWT.getClaim("perfilUsuario").asArray(String.class);
				
				Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
				
				stream(roles).forEach(role -> {
					authorities.add(new SimpleGrantedAuthority(role));					
				});
							
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				filterChain.doFilter(request, response);
				
			}catch( Exception e ) {
				log.info("Token invalido: {}", e.getMessage());
				response.setHeader("message_error: ",e.getMessage());					
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				
				Map<String, String> message_error = new HashMap<>();		
				message_error.put("message_error: ", e.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				
				new ObjectMapper().writeValue(response.getOutputStream(), message_error);
			}
		}else {
			filterChain.doFilter(request, response);
		}		
	}

}
