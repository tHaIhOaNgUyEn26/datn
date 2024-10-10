package code.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain)
      throws ServletException, IOException {

    final String authorizationHeader = request.getHeader("Authorization");

    String username = null;
    String token = null;

    if (request.getRequestURI().startsWith("/api/home")) {
      chain.doFilter(request, response); // Cho phép truy cập
      return; // Kết thúc phương thức
    }

    // Kiểm tra tiêu đề Authorization có tồn tại không
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      token = authorizationHeader.substring(7); // Lấy token từ tiêu đề
      username = jwtTokenUtil.extractUsername(token); // Trích xuất username từ token
    }

    // Nếu username không null và chưa có Authentication trong SecurityContext
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

      // Xác thực token
      if (jwtTokenUtil.validateToken(token, userDetails.getUsername())) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Lưu Authentication vào SecurityContext
      }
    }

    chain.doFilter(request, response); // Tiếp tục chuỗi filter

//    chain.doFilter(request, response);
  }

}