package br.com.astrosoft.framework.spring

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.util.matcher.RequestMatcher

@EnableWebSecurity
@Configuration
class SecurityConfiguration: WebSecurityConfigurerAdapter() {
  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {
    http.csrf()
      .disable()
      .requestCache()
      .requestCache(CustomRequestCache())
      .and()
      .authorizeRequests()
      .requestMatchers(RequestMatcher {request ->
        SecurityUtils.isFrameworkInternalRequest(request)
      })
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .formLogin()
      .loginPage(LOGIN_URL)
      .permitAll()
      .loginProcessingUrl(LOGIN_PROCESSING_URL)
      .failureUrl(LOGIN_FAILURE_URL)
      .and()
      .logout()
      .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
  }
  
  @Bean
  public override fun userDetailsService(): UserDetailsService {
    return UserSaciDetailsService()
  }
  
  @Bean
  fun passwordEncoder(): PasswordEncoder? {
    return passwordNoEncoder
  }
  
  override fun configure(web: WebSecurity) {
    web.ignoring()
      .antMatchers(
        "/VAADIN/**",
        "/favicon.ico",
        "/robots.txt",
        "/manifest.webmanifest",
        "/sw.js",
        "/offline.html",
        "/icons/**",
        "/images/**",
        "/styles/**",
        "/h2-console/**")
  }
  
  companion object {
    private const val LOGIN_PROCESSING_URL = "/login"
    private const val LOGIN_FAILURE_URL = "/login?error"
    private const val LOGIN_URL = "/login"
    private const val LOGOUT_SUCCESS_URL = "/login"
    private val passwordNoEncoder = PasswordNoEncoder()
  }
}

class PasswordNoEncoder: PasswordEncoder {
  override fun encode(rawPassword: CharSequence?) = rawPassword?.toString()
  override fun matches(rawPassword: CharSequence?, encodedPassword: String?) = rawPassword.toString() == encodedPassword
}