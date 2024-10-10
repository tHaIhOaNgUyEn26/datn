package code.service;

import code.entity.User;
import code.model.request.RegisterRequest;
import code.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository; // Giả sử bạn đã có UserRepository

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User registerNewUser(RegisterRequest registrationRequest) {
    // Kiểm tra xem email đã tồn tại hay chưa
    if (userRepository.findByEmail(registrationRequest.getEmail()) != null) {
      throw new RuntimeException("Email đã được sử dụng.");
    }

    // Tạo người dùng mới
    User newUser = new User();
    newUser.setEmail(registrationRequest.getEmail());
    newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword())); // Mã hóa mật khẩu
    newUser.setRole("admin");

    // Lưu vào cơ sở dữ liệu
    return userRepository.save(newUser);
  }
}
