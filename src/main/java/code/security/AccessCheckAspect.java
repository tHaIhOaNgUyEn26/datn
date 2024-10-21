package code.security;

import code.entity.User;
import code.exception.ForbiddenException;
import code.service.customer.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Aspect
@Component
public class AccessCheckAspect {
  private UserService userService;

  private AccessCheckAspect(UserService userService){
    this.userService = userService;
  }

  @Around("@annotation(CheckUserAccess) && args(userId,..)")
  public Object checkAccess(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
    this.userService.checkIdCustomer(userId);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();

    // Lấy thông tin người dùng dựa trên tên đăng nhập
    User currentUser = userService.findByEmail(currentUsername);

    // Kiểm tra xem userId có trùng với userId của người dùng hiện tại không
    if (currentUser.getId() != (userId)) {
      throw new ForbiddenException("Access Denied"); // Hoặc trả về một mã lỗi
    }

    return joinPoint.proceed();
  }
}
