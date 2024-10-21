package code.controller.home;

import code.entity.User;
import code.model.request.RegisterRequest;
import code.security.JwtTokenUtil;
import code.service.customer.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
public class RegisterController {
  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;

  @Autowired
  public RegisterController(UserService userService) { // Constructor injection
    this.userService = userService;
  }
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
    User newUser = this.userService.registerNewUser(registerRequest);

    String token = jwtTokenUtil.generateToken(newUser.getEmail(),"customer");
    return ResponseEntity.ok(token);
  }
  @GetMapping("/test")
  public String get(){
    return "djnbgkjsdngkds";
  }
}
