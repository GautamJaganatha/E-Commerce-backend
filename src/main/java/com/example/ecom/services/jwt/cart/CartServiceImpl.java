package com.example.ecom.services.jwt.cart;

import com.example.ecom.dto.AddProductInCartDto;
import com.example.ecom.dto.CartItemsDto;
import com.example.ecom.dto.OrderDto;
import com.example.ecom.dto.PlaceOrderDto;
import com.example.ecom.entity.*;
import com.example.ecom.enums.OrderStatus;
import com.example.ecom.exception.ValidationException;
import com.example.ecom.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRespository userRespository;

    private final CartItemRepository cartItemRepository;

    public final CouponRepository couponRepository;

    @Override
    public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto){

        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.PENDING);
        System.out.println(activeOrder);
        Optional<CartItems> optionalCartItems = cartItemRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(),activeOrder.getId(), addProductInCartDto.getUserId()
        );

        if(optionalCartItems.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }else {
            Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
            Optional<User> optionalUser = userRespository.findById(addProductInCartDto.getUserId());
            if (optionalProduct.isPresent() && optionalUser.isPresent()){
                CartItems cart = new CartItems();
                cart.setProduct(optionalProduct.get());
                cart.setPrice(optionalProduct.get().getPrice());
                cart.setQuantity(1L);
                cart.setUser(optionalUser.get());
                cart.setOrder(activeOrder);

                CartItems updatedCart = cartItemRepository.save(cart);

                activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
                activeOrder.setTotalAmount(activeOrder.getAmount() + cart.getPrice());
                activeOrder.getCartItems().add(cart);



                orderRepository.save(activeOrder);


                return ResponseEntity.status(HttpStatus.CREATED).body(cart);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or product not found");
            }
        }
    }

    public OrderDto getCartByUserID(Long userId){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        List<CartItemsDto> cartItemsDtoList = activeOrder.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList());
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());

        orderDto.setCartItems(cartItemsDtoList);
        if (activeOrder.getCoupon() != null){
            orderDto.setCouponName(activeOrder.getCoupon().getName());
        }

        return orderDto;
    }

    public OrderDto applyCoupon(Long userId, String code){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        Coupon coupon = (Coupon) couponRepository.findByCode(code).orElseThrow(() -> new ValidationException("Coupon not found"));

        if (couponIsExpired(coupon)){
            throw new ValidationException("Coupon has expired");
        }
        double discountAmount = ((coupon.getDiscount() / 100.00) * activeOrder.getTotalAmount());
        double netAmount = activeOrder.getTotalAmount() - discountAmount;

        activeOrder.setAmount((long) netAmount);
        activeOrder.setDiscount((long) discountAmount);
        activeOrder.setCoupon(coupon);

        orderRepository.save(activeOrder);

        return activeOrder.getOrderDto();

    }

    private boolean couponIsExpired(Coupon coupon){
        Date currentDate = new Date();
        Date expirationDate = coupon.getExpirationDate();

        return expirationDate != null && currentDate.after(expirationDate);
    }
    public OrderDto IncreaseProductQuantity(AddProductInCartDto addProductInCartDto){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.PENDING);
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

        Optional<CartItems> optionalCartItems = cartItemRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId()
        );
        if (optionalProduct.isPresent() && optionalCartItems.isPresent()){
            CartItems cartItems = optionalCartItems.get();
            Product product = optionalProduct.get();

            activeOrder.setAmount(activeOrder.getAmount() + product.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + product.getPrice());


            cartItems.setQuantity(cartItems.getQuantity() + 1);

            if (activeOrder.getCoupon() != null){
                double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.00) * activeOrder.getTotalAmount());
                double netAmount = activeOrder.getTotalAmount() - discountAmount;
            }

            cartItemRepository.save(cartItems);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    public OrderDto DecreaseProductQuantity(AddProductInCartDto addProductInCartDto){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.PENDING);
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

        Optional<CartItems> optionalCartItems = cartItemRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId()
        );
        if (optionalProduct.isPresent() && optionalCartItems.isPresent()){
            CartItems cartItems = optionalCartItems.get();
            Product product = optionalProduct.get();

            activeOrder.setAmount(activeOrder.getAmount() - product.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() - product.getPrice());


            cartItems.setQuantity(cartItems.getQuantity() - 1);

            if (activeOrder.getCoupon() != null){
                double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.00) * activeOrder.getTotalAmount());
                double netAmount = activeOrder.getTotalAmount() - discountAmount;
            }

            cartItemRepository.save(cartItems);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    public OrderDto placeOrder(PlaceOrderDto placeOrderDto){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.PENDING);

        if (activeOrder == null) {
            // Handle the case where no pending order exists for the user
            // This could involve creating a new order or throwing an exception
            throw new IllegalArgumentException("No pending order found for user ID: " + placeOrderDto.getUserId());
        }
        Optional<User>  optionalUser = userRespository.findById(placeOrderDto.getUserId());

        if (optionalUser.isPresent()){
            activeOrder.setOrderDescription(placeOrderDto.getDescription());
            activeOrder.setAddress(placeOrderDto.getAddress());
            activeOrder.setDate(new Date());
            activeOrder.setOrderStatus(OrderStatus.PLACED);
            activeOrder.setTrackingId(UUID.randomUUID());

            orderRepository.save(activeOrder);

            Order order = new Order();
            order.setAmount(0L);
            order.setTotalAmount(0L);
            order.setDiscount(0L);
            order.setUser(optionalUser.get());
            order.setOrderStatus(OrderStatus.PENDING);
            orderRepository.save(order);

            return activeOrder.getOrderDto();
        }
        return null;
    }

    public List<OrderDto> getMyPlacedOrders(Long userId){
        return orderRepository.findByUserIdAndOrderStatusIn(userId, List.of(OrderStatus.PLACED, OrderStatus.SHIPPED, OrderStatus.DELIVERED)).stream().map(Order::getOrderDto).collect(Collectors.toList());
    }

    public OrderDto searchOrderByTrackingId(UUID trackingId){
        Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);
        if (optionalOrder.isPresent()){
            return  optionalOrder.get().getOrderDto();
        }
        return null;
    }
}
