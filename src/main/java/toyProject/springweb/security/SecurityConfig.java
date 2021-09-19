package toyProject.springweb.security;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import toyProject.springweb.domain.Roles;

import javax.sql.DataSource;
import java.util.logging.Logger;

/**
 * SecurityConfig Class
 * 스프링 시큐리티 설정 클래스
 */
@Slf4j
@EnableWebSecurity //SecurityConfig 빈 등록
@EnableGlobalMethodSecurity(securedEnabled = true) //메서드 호철 전 권한 확인
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Load SecurityConfig.configure()");

        http.authorizeRequests().antMatchers("/board/list").permitAll()
                .antMatchers("/file/**").permitAll()
                .antMatchers("/board/view").permitAll()
                .antMatchers("/board/write").hasAnyRole(Roles.USER.toString(), Roles.MANAGER.toString(), Roles.ADMIN.toString());

        //로그인 페이지 커스텀
        http.formLogin().loginPage("/login").successHandler(new LoginSuccessHandler());

        //접근 거부 페이지 커스텀
        http.exceptionHandling().accessDeniedPage("/accessDeniedPage");

        //로그아웃 페이지 커스텀
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true);

        http.rememberMe()
                .key("yoondev")
                .userDetailsService(userDetailService)
                .tokenRepository(getJDBCRepository())
                .tokenValiditySeconds(60 * 60 * 24);

    }

    /**
     *  passwordEncoder()
     *  Password 암호화
     * @return BCryptPassword
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    private PersistentTokenRepository getJDBCRepository() {

        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }
}
