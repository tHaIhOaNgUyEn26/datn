package code.controller.customer;

import code.entity.Address;
import code.model.dto.CustomerInfoDTO;
import code.model.request.ChangePasswordRequest;
import code.model.request.UpdateCustomerRequest;
import code.security.CheckUserAccess;
import code.service.customer.AddressService;
import code.service.customer.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class InformationController {
  private UserService userService;
  private AddressService addressService;
  public InformationController(UserService userService,AddressService addressService){
    this.userService = userService;
    this.addressService = addressService;
  }
  @GetMapping("/{user_id}/profile")
  public ResponseEntity<?> getCustomerInfo(@PathVariable Long user_id) {
    CustomerInfoDTO customerInfoDTO = userService.getCustomerInfo(user_id);
    return ResponseEntity.ok(customerInfoDTO);
  }

  @PutMapping("/profile")
  public ResponseEntity<?> updateCustomerInfo(@RequestBody UpdateCustomerRequest updateCustomerRequest){
    CustomerInfoDTO customerInfoDTO = userService.updateCustomerInfo(updateCustomerRequest);
    return ResponseEntity.ok(customerInfoDTO);
  }

  @PutMapping("/profile/password")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
    String status = userService.changePassword(changePasswordRequest);
    return ResponseEntity.ok(status);
  }

  @GetMapping("/{user_id}/address")
  @CheckUserAccess
  public ResponseEntity<?> getAddress(@PathVariable long user_id){
    List<Address> addresses = this.addressService.getAddressesByUserId(user_id);
    return ResponseEntity.ok(addresses);
  }

}
