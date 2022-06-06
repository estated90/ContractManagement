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
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthEntryPoint unauthorizedHandler;
	private static final String BASE_URL = "/api/contractManagement";

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
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors();
		http.authorizeRequests()
				// Swagger security details
				.anyRequest().authenticated();
		// Swagger security details
		http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler()).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(BASE_URL+"/v3/api-docs", BASE_URL+"/swagger-ui.html", BASE_URL+"/swagger-ui/**");
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
