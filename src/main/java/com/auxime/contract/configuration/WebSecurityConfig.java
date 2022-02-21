package com.auxime.contract.configuration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auxime.contract.constants.RoleName;
import com.auxime.contract.exceptionHandler.ApiError;
import com.auxime.contract.jwt.JwtAuthEntryPoint;
import com.auxime.contract.jwt.JwtAuthTokenFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class manage the configuration for spring security
 * 
 * @author Nicolas
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthEntryPoint unauthorizedHandler;

	/**
	 * @return New JwtAuthTokenFilter used for the token and connection
	 */
	@Bean
	public JwtAuthTokenFilter authenticationJwtTokenFilter() {
		return new JwtAuthTokenFilter();
	}

	/**
	 * @return A new password Encoder to secure the provided password
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Any end point that requires defense against common vulnerabilities can be
	 * specified here, including public ones. See
	 * {@link HttpSecurity#authorizeRequests} and the `permitAll()` authorization
	 * rule for more details on public end points.
	 * 
	 * @param http the {@link HttpSecurity} to modify
	 * @throws Exception if an error occurs
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				// .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
				.cors().and().authorizeRequests()
				// Swagger security details
				.antMatchers("/api/auth/**").permitAll().antMatchers("/api-docs/**").permitAll()
				.antMatchers("/api/eventPlanning/signupEvent/**")
				.hasAnyAuthority(RoleName.ROLE_USER.toString(), RoleName.ROLE_ADMIN.toString())
				.antMatchers("/api/training/**")
				.hasAnyAuthority(RoleName.ROLE_COUNSELOR.toString(), RoleName.ROLE_ADMIN.toString())
				.antMatchers("/api/host/**")
				.hasAnyAuthority(RoleName.ROLE_COUNSELOR.toString(), RoleName.ROLE_ADMIN.toString())
				.antMatchers("/api/eventPlanning/**")
				.hasAnyAuthority(RoleName.ROLE_COUNSELOR.toString(), RoleName.ROLE_ADMIN.toString())
				.antMatchers("/api/attendants/**")
				.hasAnyAuthority(RoleName.ROLE_COUNSELOR.toString(), RoleName.ROLE_ADMIN.toString(),
						RoleName.ROLE_EXTERNAL_TRAINER.toString(), RoleName.ROLE_INTERNAL_TRAINER.toString())
				.anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, ex) -> {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ApiError error = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage(),
					"Access is limited for this ressource");
			ServletOutputStream out = response.getOutputStream();
			new ObjectMapper().writeValue(out, error);
			out.flush();
		};
	}
}
