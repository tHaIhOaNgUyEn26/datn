package code.service.customer;

import code.entity.User;
import code.exception.NotFoundException;
import code.model.dto.CustomerInfoDTO;
import code.model.request.ChangePasswordRequest;
import code.model.request.RegisterRequest;
import code.model.request.UpdateCustomerRequest;
import code.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public void checkIdCustomer(Long user_id){
    Optional<User> optionalUser = this.userRepository.findById(user_id);
    if (!optionalUser.isPresent()) {
      throw new NotFoundException("Không tìm thấy người dùng với id: " + user_id);
    }
  }
  public User registerNewUser(RegisterRequest registrationRequest) {
    // Kiểm tra xem email đã tồn tại hay chưa
    if (userRepository.findByEmail(registrationRequest.getEmail()) != null) {
      throw new RuntimeException("Email đã được sử dụng.");
    }

    // Kiểm tra định dạng email hợp lệ
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    if (!registrationRequest.getEmail().matches(emailRegex)) {
      throw new RuntimeException("Email không hợp lệ.");
    }
    // Kiểm tra độ dài mật khẩu
    if(registrationRequest.getPassword().length() < 6){
      throw new RuntimeException("Mật khẩu phải dài hơn 6 kí tự.");
    }
    // Tạo người dùng mới
    User newUser = new User();
    newUser.setEmail(registrationRequest.getEmail());
    newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword())); // Mã hóa mật khẩu
    newUser.setRole("customer");
    newUser.setStatus(1);

    // Lưu vào cơ sở dữ liệu
    return userRepository.save(newUser);
  }
  public User findByEmail(String email){
    return this.userRepository.findByEmail(email);
  }

  public CustomerInfoDTO getCustomerInfo(Long user_id) {
    CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO(); // Khởi tạo DTO ở đây
    Optional<User> optionalUser = this.userRepository.findById(user_id);
    this.checkIdCustomer(user_id);
    // Lấy User từ Optional
    User user = optionalUser.get();
    customerInfoDTO.setId(user.getId());
    customerInfoDTO.setName(Objects.requireNonNullElse(user.getName(), "Chưa đặt tên"));
    customerInfoDTO.setPhone(Objects.requireNonNullElse(user.getPhone(), "Chưa có sdt"));
    customerInfoDTO.setEmail(user.getEmail());
    customerInfoDTO.setStatus(user.getStatus());
    return customerInfoDTO; // Trả về DTO nếu không có lỗi
  }

  public CustomerInfoDTO updateCustomerInfo(UpdateCustomerRequest updateCustomerRequest){
    long user_id = updateCustomerRequest.getId();
    Optional<User> user = this.userRepository.findById(user_id);
    this.checkIdCustomer(user_id);
    if(updateCustomerRequest.getName().trim().length() < 4){
      throw new RuntimeException("Tên quá ngắn.");
    }
    if (!updateCustomerRequest.getPhone().matches("^0[0-9]{9,10}$")) {
      throw new RuntimeException("Số điện thoại không hợp lệ.");
    }

    User userPresent = user.get();
    userPresent.setName(updateCustomerRequest.getName());
    userPresent.setPhone(updateCustomerRequest.getPhone());
    userRepository.save(userPresent);
    return this.getCustomerInfo(user_id);
  }

  public String changePassword(ChangePasswordRequest changePasswordRequest){
    Optional<User> optionalUser = this.userRepository.findById(changePasswordRequest.getId());
    this.checkIdCustomer(changePasswordRequest.getId());
    User user = optionalUser.get();

    // So sánh mật khẩu cũ với mật khẩu đã mã hóa
    if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
      throw new RuntimeException("Mật khẩu cũ không đúng.");
    }

    if(changePasswordRequest.getNewPassword().length() < 6){
      throw new RuntimeException("Mật khẩu quá ngắn.");
    }
    // Mã hóa mật khẩu mới
    String encodedNewPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());

    // Cập nhật mật khẩu mới cho người dùng
    user.setPassword(encodedNewPassword);

    // Lưu người dùng với mật khẩu mới
    userRepository.save(user);

    return "Đổi mật khẩu thành công.";
  }
}
