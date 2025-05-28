//package com.WhiteCloud.SpringBootTest.Utils.Config;
//
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.catalina.filters.CorsFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsConfigurationSource;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//
//@EnableWebSecurity
//@Configuration
//@EnableMethodSecurity
//public class SpringSecurityConfig{
//    private UserDetailsService userDetailsService;
//    @Autowired
//    public SpringSecurityConfig(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
// //测试阶段可以不加密
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // 返回一个不进行加密的 PasswordEncoder 实例，仅测试时使用
//        return new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence rawPassword) {
//                return rawPassword.toString();  // 不加密，直接返回
//            }
//
//            @Override
//            public boolean matches(CharSequence rawPassword, String encodedPassword) {
//                return rawPassword.toString().equals(encodedPassword);  // 明文比对
//            }
//        };
//    }
//
////    @Bean
////    public PasswordEncoder passwordEncoder(){
////        return new BCryptPasswordEncoder();
////    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//        return authenticationManagerBuilder.build();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable);
//
//        http.authorizeHttpRequests(authz -> authz
//                .requestMatchers("/login").permitAll()  // 登录页无需认证
//                .requestMatchers("/index").authenticated()  // 登录成功后访问
//                .anyRequest().authenticated()
//        );
//
//        http.formLogin(form -> form
//                .loginPage("/login")                 // 自定义登录页面
//                .defaultSuccessUrl("/index", true)   // 登录成功跳转
//                .failureUrl("/login?error=true")     // 登录失败跳转
//                .permitAll()
//        );
//
//        http.logout(logout -> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?logout=true")
//                .permitAll()
//        );
//
//        // 认证异常处理
//        http.exceptionHandling(exception -> exception
//                .authenticationEntryPoint((request, response, authException) ->
//                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未认证，请登录"))
//                .accessDeniedHandler((request, response, accessDeniedException) ->
//                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "权限不足，禁止访问"))
//                .accessDeniedPage("/denied")
//        );
//        // 开启跨域配置 (如需前后端分离，请启用)
//        // 直接配置跨域，无需额外方法返回 CorsConfigurationSource
////        http.cors(cors -> cors.configurationSource(request -> {
////            CorsConfiguration config = new CorsConfiguration();
////            config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));  // 允许的前端地址
////            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
////            config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
////            config.setAllowCredentials(true);  // 允许携带 Cookie 进行跨域
////            return config;
////        }));
//
//        // 关闭 CSRF 保护（默认关闭，可选开启）
//        //http.csrf(AbstractHttpConfigurer::disable);
//
//        // 开启 CSRF 保护，如果需要则注释掉上面的关闭
//        /*
//        http.csrf(csrf -> csrf
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//        );
//        */
//
//        // 会话管理（无状态，用于 JWT 认证时）
////        http.sessionManagement(session ->
////                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////        );
//
//        // 默认会话管理（有状态，如需 session 认证请启用）
//        /*
//        http.sessionManagement(session -> session
//                .sessionFixation().migrateSession()
//                .maximumSessions(1)
//                .expiredUrl("/login?expired")
//        );
//        */
//
//        // 认证授权配置
////        http.authorizeHttpRequests(authz -> authz
////                .requestMatchers("/admin/**").hasRole("ADMIN")        // 管理员权限
////                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // 用户或管理员权限
////                .requestMatchers("/api/auth/**").permitAll()           // 认证接口无需登录
////                .anyRequest().authenticated()                           // 其他请求需要认证
////        );
//
//        // 表单登录配置
////        http.formLogin(form -> form
////                .loginPage("/login")                // 自定义登录页面
////                .defaultSuccessUrl("/home", true)   // 登录成功重定向
////                .permitAll()                        // 登录页面允许访问
////        );
//
//        // Basic 认证配置（如需要 Basic 认证启用）
//        /*
//        http.httpBasic(Customizer.withDefaults());
//        */
//
//        // JWT 认证过滤器（如有自定义 JWT 认证，请启用并配置过滤器）
//        /*
//        http.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        */
//
//        // 登出配置
////        http.logout(logout -> logout
////                .logoutUrl("/logout")
////                .logoutSuccessUrl("/login?logout")
////                .permitAll()
////        );
//        return http.build();
//    }
//}
