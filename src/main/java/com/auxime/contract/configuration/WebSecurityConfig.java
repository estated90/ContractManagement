package com.auxime.contract.configuration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auxime.contract.constants.RoleName;
import com.auxime.contract.exception.exceptionhandler.ApiError;
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
@EnableAutoConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

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

	@Bean
	public JwtAuthEntryPoint unauthorizedHandler() {
		return new JwtAuthEntryPoint();
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
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().cors();
		http.authorizeRequests(autorization -> {
			// Swagger security details
			autorization.antMatchers("/api-docs/**").permitAll();
			// API application
			autorization
					.antMatchers("/cape/**").hasAnyAuthority(internalStaff)
					.antMatchers("/contracts/**").hasAnyAuthority(internalStaff)
					.antMatchers("/commercialContract/update").hasAnyAuthority(commercialAccess)
					.antMatchers("/commercialContract/pendingValidation").hasAuthority(RoleName.ROLE_USER.toString())
					.antMatchers("/commercialContract/addComment").hasAnyAuthority(commercialAccess)
					.antMatchers("/commercialContract/create").hasAnyAuthority(commercialAccess)
					.antMatchers("/commercialContract/createAmendment").hasAnyAuthority(commercialAccess)
					.antMatchers("/commercialContract/myContractCount").hasAnyAuthority(all)
					.antMatchers("/commercialContract/**").hasAnyAuthority(internalStaff)
					.antMatchers("/permanentContract/**").hasAnyAuthority(internalStaff)
					.antMatchers("/portageConvention/**").hasAnyAuthority(internalStaff)
					.antMatchers("/temporaryContract/**").hasAnyAuthority(internalStaff)
					.antMatchers("/enums/**").permitAll()
					.anyRequest().authenticated();
		});
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		return http.build();
	}

	/**
	 * @return The denied handler
	 */
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

	String[] internalStaff = new String[] { RoleName.ROLE_ACCOUNTANCY_MANAGER.toString(),
			RoleName.ROLE_ADMIN.toString(), RoleName.ROLE_COUNSELOR.toString(), RoleName.ROLE_DEVELOPMENT.toString(),
			RoleName.ROLE_DIRECTOR.toString(), RoleName.ROLE_EXPENSES.toString(), RoleName.ROLE_FSE.toString(),
			RoleName.ROLE_INVOICING.toString(), RoleName.ROLE_IT.toString(), RoleName.ROLE_PAYROLL_MANAGER.toString(),
			RoleName.ROLE_QUALIOPI.toString(), RoleName.ROLE_SHAREHOLDER.toString() };
	String[] internalUser = new String[] { RoleName.ROLE_ACCOUNTANCY_MANAGER.toString(), RoleName.ROLE_ADMIN.toString(),
			RoleName.ROLE_COUNSELOR.toString(), RoleName.ROLE_DEVELOPMENT.toString(), RoleName.ROLE_DIRECTOR.toString(),
			RoleName.ROLE_EXPENSES.toString(), RoleName.ROLE_FSE.toString(), RoleName.ROLE_INVOICING.toString(),
			RoleName.ROLE_IT.toString(), RoleName.ROLE_PAYROLL_MANAGER.toString(), RoleName.ROLE_QUALIOPI.toString(),
			RoleName.ROLE_SHAREHOLDER.toString(), RoleName.ROLE_USER.toString() };
	String[] support = new String[] { RoleName.ROLE_ADMIN.toString(), RoleName.ROLE_COUNSELOR.toString(),
			RoleName.ROLE_DEVELOPMENT.toString(), RoleName.ROLE_DIRECTOR.toString(), RoleName.ROLE_IT.toString() };
	String[] commercialAccess = new String[] { RoleName.ROLE_ADMIN.toString(), RoleName.ROLE_COUNSELOR.toString(),
			RoleName.ROLE_DEVELOPMENT.toString(), RoleName.ROLE_DIRECTOR.toString(), RoleName.ROLE_IT.toString(), RoleName.ROLE_USER.toString() };
	String[] modifier = new String[] { RoleName.ROLE_ADMIN.toString(), RoleName.ROLE_COUNSELOR.toString(),
			RoleName.ROLE_DEVELOPMENT.toString(), RoleName.ROLE_DIRECTOR.toString(), RoleName.ROLE_IT.toString(),
			RoleName.ROLE_PAYROLL_MANAGER.toString() };
	String[] adminIt = new String[] { RoleName.ROLE_ADMIN.toString(), RoleName.ROLE_IT.toString(),
			RoleName.ROLE_DIRECTOR.toString() };
	String[] partnerRoles = new String[] { RoleName.ROLE_ADMIN.toString(), RoleName.ROLE_DEVELOPMENT.toString(),
			RoleName.ROLE_DIRECTOR.toString(), RoleName.ROLE_IT.toString() };
	String[] all = new String[] { RoleName.ROLE_ACCOUNTANCY_MANAGER.toString(), RoleName.ROLE_ADMIN.toString(),
			RoleName.ROLE_COUNSELOR.toString(), RoleName.ROLE_DEVELOPMENT.toString(), RoleName.ROLE_DIRECTOR.toString(),
			RoleName.ROLE_EXPENSES.toString(), RoleName.ROLE_FSE.toString(), RoleName.ROLE_INVOICING.toString(),
			RoleName.ROLE_IT.toString(), RoleName.ROLE_PAYROLL_MANAGER.toString(), RoleName.ROLE_QUALIOPI.toString(),
			RoleName.ROLE_SHAREHOLDER.toString(), RoleName.ROLE_USER.toString(),
			RoleName.ROLE_EXTERNAL_TRAINER.toString(), RoleName.ROLE_INTERNAL_TRAINER.toString() };
	String[] allSecu = new String[] { RoleName.ROLE_ACCOUNTANCY_MANAGER.toString(), RoleName.ROLE_ADMIN.toString(),
			RoleName.ROLE_COUNSELOR.toString(), RoleName.ROLE_DEVELOPMENT.toString(), RoleName.ROLE_DIRECTOR.toString(),
			RoleName.ROLE_EXPENSES.toString(), RoleName.ROLE_FSE.toString(), RoleName.ROLE_INVOICING.toString(),
			RoleName.ROLE_IT.toString(), RoleName.ROLE_PAYROLL_MANAGER.toString(), RoleName.ROLE_QUALIOPI.toString(),
			RoleName.ROLE_SHAREHOLDER.toString(), RoleName.ROLE_APP.toString() };
}
