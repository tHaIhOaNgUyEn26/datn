package code.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
  @GetMapping("/api/admin/test")
  public ResponseEntity<?> get(){
    return ResponseEntity.ok("Admin");
  }

  @GetMapping("/api/customer/test")
  public ResponseEntity<?>  getCustomer(){
    return ResponseEntity.ok("Cus");
  }
}
