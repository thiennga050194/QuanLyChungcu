package com.example.quanlychungcu.config;

import com.example.quanlychungcu.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()

                // ===== PUBLIC APIs (không cần đăng nhập) =====
                .antMatchers("/", "/login.html", "/css/**", "/js/**", "/images/**", "/video/**", "/api/auth/**").permitAll()
                .antMatchers("/api/user/me").permitAll() 

                // ===== APIs PHÂN TRANG - Tất cả role đều xem được =====
                .antMatchers(HttpMethod.GET, "/api/cudan/paged").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/khu/paged").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/canho/paged").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/hopdong/paged").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/phuongtien/paged").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/phanhoi/paged").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/user/paged").hasRole("ADMIN") 

                // ===== APIs TÌM KIẾM & THỐNG KÊ =====
                .antMatchers(HttpMethod.GET, "/api/**/search/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/**/statistics").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/**/count/**").hasAnyRole("ADMIN", "STAFF")

                // ===== APIs CŨ (giữ nguyên) =====
                .antMatchers(HttpMethod.GET, "/api/khu/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/canho/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.PUT, "/api/canho/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.POST, "/api/canho/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/cudan/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/hopdong/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/phuongtien/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/phanhoi/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/tien/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/thongbao/**").hasAnyRole("ADMIN", "STAFF")

                // ===== APIs CHỈ ADMIN =====
                .antMatchers(HttpMethod.POST, "/api/khu/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/khu/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/khu/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/user/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/user/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/user/**").hasRole("ADMIN")

                // ===== APIs CHỈ STAFF =====
                .antMatchers(HttpMethod.DELETE, "/api/hopdong/**").hasRole("STAFF")
                .antMatchers(HttpMethod.DELETE, "/api/cudan/**").hasRole("STAFF")

                .antMatchers(HttpMethod.POST, "/api/tien/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.PUT, "/api/tien/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.DELETE, "/api/tien/**").hasAnyRole("ADMIN", "STAFF")

                // ===== APIs CHỨC NĂNG ĐẶC BIỆT =====
                .antMatchers("/api/khu/add-complex").hasRole("ADMIN")
                .antMatchers("/api/canho/empty").hasAnyRole("ADMIN", "STAFF")
                .antMatchers("/api/khu/top").hasAnyRole("ADMIN", "STAFF")

                // ===== PHÂN QUYỀN TRANG HTML =====
                .antMatchers("/quanly.html").hasRole("ADMIN")
                .antMatchers("/nhanvien.html").hasRole("STAFF")

                .anyRequest().authenticated()
                .and()

                // ===== JWT FILTER =====
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // ===== FORM LOGIN =====
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/perform_login")
                .successHandler(successHandler)
                .failureUrl("/login.html?error=true")
                .permitAll()
                .and()

                // ===== LOGOUT =====
                .logout()
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/login.html")
                .permitAll();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",
                "http://127.0.0.1:8080",
                "http://localhost:5500", 
                "http://127.0.0.1:5500"));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}