package code.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

  private String SECRET_KEY = "c40aec4acbcab493c441be817c3c153fca2e94454be091b4f16837d88d8db33f"; // Khóa bí mật của bạn
  private int EXPIRATION_TIME = 1000 * 60 * 60; // Thời gian hết hạn trong 1 giờ

  public String generateToken(String username, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", role);
    return createToken(claims, username);
  }

  private String createToken(Map<String, Object> claims, String subject) {
    // Chuyển SECRET_KEY thành Key sử dụng thuật toán HMAC-SHA
    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//        custom them o day
        .signWith(key, SignatureAlgorithm.HS256)  // Sử dụng key và thuật toán HS256
        .compact();
  }

  public boolean validateToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    // Chuyển đổi chuỗi SECRET_KEY thành Key
    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    // Phân tích token
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key) // Thiết lập khóa
        .build()
        .parseClaimsJws(token) // Phân tích JWT
        .getBody();
    // Lấy thời gian hết hạn
    return claims.getExpiration();
  }

  public String extractUsername(String token) {
    // Chuyển SECRET_KEY thành Key để sử dụng với JJWT mới
    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Sử dụng parserBuilder để phân tích JWT
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)  // Cung cấp khóa bí mật
        .build()
        .parseClaimsJws(token) // Phân tích token
        .getBody();

    // Trả về subject, là email hoặc tên người dùng
    return claims.getSubject();
//    return "dskngksdg";
  }

  public Claims getClaimsFromToken(String token) {
    try {
      Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody(); // Lấy Claims từ token
    } catch (Exception e) {
      return null; // Nếu có lỗi khi parse token, trả về null
    }
  }

  public List<String> getRolesFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    if (claims != null) {
      // Giả sử roles được lưu trong claim có key là "roles"
      return (List<String>) claims.get("roles");
    }
    return Collections.emptyList(); // Nếu không có roles, trả về list rỗng
  }


}