package code.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;
  private final JwtRequestFilter jwtRequestFilter;

  public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
    this.customUserDetailsService = customUserDetailsService;
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF (không cần cho API)
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/auth/**","api/home/**").permitAll() // Cho phép truy cập công khai tới các API xác thực (login, register, v.v.)
            .requestMatchers("/api/admin/**").hasRole("admin") // Chỉ admin có thể truy cập
            .requestMatchers("/api/customer/**").hasRole("customer") // Chỉ customer có thể truy cập
            .anyRequest().authenticated() // Các yêu cầu khác phải được xác thực
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sử dụng JWT, không tạo session
        )
        // Thêm filter JWT trước filter xác thực mặc định
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
    ;

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // Mã hóa mật khẩu
  }

  // Khai báo AuthenticationManager để sử dụng cho việc xác thực
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
