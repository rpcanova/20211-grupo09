package com.fatec.scc.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class ConfiguracaoDeSeguranca extends WebSecurityConfigurerAdapter{
	@Override
	 protected void configure(HttpSecurity http) throws Exception {
	 http.authorizeRequests()
	 .antMatchers("/aluno/cadastrar").hasAnyRole("ADMIN", "BIB")
	 .antMatchers("/aluno/cadastrar").hasRole("BIB")
	 .anyRequest().authenticated().and()
	 .formLogin().loginPage("/login").permitAll().and()
	 .logout().logoutSuccessUrl("/login?logout").permitAll();
	 }
	
	 @Override
	 public void configure(AuthenticationManagerBuilder auth) throws Exception {
	 auth.inMemoryAuthentication()
	 .withUser("user").password(pc().encode("123")).roles("ADMIN").and()
	 .withUser("douglas").password(pc().encode("456")).roles("BIB").and()
	 .withUser("rafael").password(pc().encode("789")).roles("BIB");
	 }
	 
	 @Bean
	 public BCryptPasswordEncoder pc() {
	 return new BCryptPasswordEncoder();
	 }
	 
	 @Override
	 public void configure(WebSecurity web) throws Exception {
	 web.ignoring().antMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/h2-console/**");
	 }
}