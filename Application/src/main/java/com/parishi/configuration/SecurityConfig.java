package com.parishi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("com.parishi.repository")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private DataSource dataSource;

    @Autowired
    private  UserDetailsService userDetailsService;
    @Autowired
    private  PasswordEncoder passwordEncoder;



//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("SELECT username, password, enabled FROM users_nsbt_automation_project WHERE username=?")
//                .authoritiesByUsernameQuery("SELECT username, CONCAT('ROLE_', role) as role FROM authorities_nsbt_automation_project WHERE username=?")
//                .passwordEncoder(passwordEncoder());
//    }

    // run these queries after running the application and before login
//    select * from track_users;
//    insert into track_users values ('admin', 1,'$2a$12$yysPhlNi8sJcuCDAnfiQbe68fQ3jsxCyV9DHkFPlu.JTfJqd77/sC');
//
//    select * from user_roles;
//    insert into user_roles values ('admin', 'ADMIN');


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").hasAnyRole("ADMIN", "TRAINER","TRAINEE")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout=true")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and()
                .csrf().disable();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
