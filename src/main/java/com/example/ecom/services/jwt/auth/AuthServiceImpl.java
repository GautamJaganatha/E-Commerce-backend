package com.example.ecom.services.jwt.auth;

import com.example.ecom.dto.SignupRequest;
import com.example.ecom.dto.UserDto;
import com.example.ecom.entity.Order;
import com.example.ecom.entity.User;
import com.example.ecom.enums.OrderStatus;
import com.example.ecom.enums.UserRole;
import com.example.ecom.repository.OrderRepository;
import com.example.ecom.repository.UserRespository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRespository userRespository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final OrderRepository orderRepository;

    public UserDto createUser(SignupRequest signupRequest){
        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        User createUser = userRespository.save(user);

        Order order = new Order();
        order.setAmount(0L);
        order.setTotalAmount(0L);
        order.setDiscount(0L);
        order.setUser(createUser);
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.save(order);


        UserDto userDto = new UserDto();
        userDto.setId(createUser.getId());

        return userDto;
    }

    public Boolean hasUserWithEmail(String email){
        return userRespository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRespository.findByRole(UserRole.ADMIN);
        if (null == adminAccount){
            User user = new User();
            user.setEmail("Admin@test.com");
            user.setName("Admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("Admin"));
            userRespository.save(user);
        }
    }
}
