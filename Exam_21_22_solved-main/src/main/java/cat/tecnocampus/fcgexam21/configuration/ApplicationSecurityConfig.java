package cat.tecnocampus.fcgexam21.configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private DataSource dataSource;

    private static final String USERS_QUERY = "select username, password, enabled from user where username = ?";
    private static final String AUTHORITIES_QUERY = "select username, role from user_roles where username = ?";


    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, DataSource dataSource) {
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO 1 add matchers to acomplish the access permissions stated in the README.md file
        http
                .authorizeRequests()
                .antMatchers("/api/users/me", "/api/user/friends", "/api/user/favoriteJourney").hasRole("USER")
                .antMatchers("/api/user/favoriteJourneys", "/api/user/friends").hasRole("USER")
                .antMatchers("/api/users/friends").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/users", "/api/users/*").hasRole("ADMIN")
                .antMatchers("/api/stations", "/api/stations/*").permitAll()
                .anyRequest().authenticated()

                .and()
                .httpBasic()

                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //Configure authentication manager
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(USERS_QUERY)
                .authoritiesByUsernameQuery(AUTHORITIES_QUERY)
                .passwordEncoder(passwordEncoder);
    }
}
