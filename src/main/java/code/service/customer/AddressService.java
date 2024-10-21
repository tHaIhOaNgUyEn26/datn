package code.service.customer;

import code.entity.Address;
import code.entity.User;
import code.exception.NotFoundException;
import code.repository.AddressRepository;
import code.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

  private AddressRepository addressRepository;
  private UserRepository userRepository;

  private AddressService(AddressRepository addressRepository,UserRepository userRepository) {
    this.addressRepository = addressRepository;
    this.userRepository = userRepository;
  }

  private void checkIdCustomer(Long user_id){
    Optional<User> optionalUser = this.userRepository.findById(user_id);
    if (!optionalUser.isPresent()) {
      throw new NotFoundException("Không tìm thấy người dùng với id: " + user_id);
    }
  }

  public List<Address> getAddressesByUserId(long user_id){
    checkIdCustomer(user_id);
    return this.addressRepository.findByUser(this.userRepository.findById(user_id).get());
  }
}
