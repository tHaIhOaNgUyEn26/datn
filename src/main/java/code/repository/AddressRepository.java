package code.repository;

import code.entity.Address;
import code.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByUser(User user);
}
