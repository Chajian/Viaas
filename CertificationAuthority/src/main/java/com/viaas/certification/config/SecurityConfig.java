package com.viaas.certification.config;

import com.viaas.certification.encoder.MD5Encoder;
import com.viaas.certification.service.impl.AccountUserDetailService;
import com.viaas.idworker.IdWorker;
import com.viaas.idworker.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${snow.woker.id:1}")
    private Long workId;
//    @Autowired
//    private AuthenticationManager authenticationManager;
    /**
     * 自定义密码加密方式配置
     * @return 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new MD5Encoder();
    }

//    /**
//     * 获取AuthenticationManager（认证管理器），登录时认证使用
//     * @param authenticationConfiguration 认证配置
//     * @return 认证管理器
//     * @throws Exception 异常
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    /**
     * 配置安全过滤链
     * @param http HTTP安全配置
     * @return 安全过滤链
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 基于 token，不需要 csrf
                .csrf(csrf -> csrf.disable())
                // 开启跨域以便前端调用接口
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 指定某些接口不需要通过验证即可访问。登录接口肯定是不需要认证的
                        .requestMatchers("/admin/system/index/login").permitAll()
                        // 静态资源，可匿名访问
                        .requestMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**", "/doc.html").permitAll()
                        // 这里意思是其它所有接口需要认证才能访问
                        .anyRequest().authenticated()
                )
                // 基于 token，不需要 session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * 配置跨源访问(CORS)
     * @return 跨域配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public UserDetailsService users() {
        return new AccountUserDetailService();
    }

    //雪花id
    @Bean
    public IdWorker idWorker(){
        return new SnowflakeIdGenerator(workId);
    }
}
