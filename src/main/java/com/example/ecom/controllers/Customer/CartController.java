package com.example.ecom.controllers.Customer;

import com.example.ecom.dto.AddProductInCartDto;
import com.example.ecom.dto.OrderDto;
import com.example.ecom.dto.PlaceOrderDto;
import com.example.ecom.exception.ValidationException;
import com.example.ecom.services.jwt.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;



    @PostMapping("cart")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductInCartDto addProductInCartDto){
        System.out.println(addProductInCartDto);
        return  cartService.addProductToCart(addProductInCartDto);

    }


    @GetMapping("cart/{userID}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Long userID){
        OrderDto orderDto = cartService.getCartByUserID(userID);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);

    }

    @GetMapping("/coupon/{userId}/{code}")
    public  ResponseEntity<?> applyCoupon(@PathVariable Long userId, @PathVariable String code){
        try {
            OrderDto orderDto = cartService.applyCoupon(userId, code);
            return ResponseEntity.ok(orderDto);
        } catch (ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("addition")
    public ResponseEntity<OrderDto> IncreaseProductQuantity(@RequestBody AddProductInCartDto addProductInCartDto){
        //
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.IncreaseProductQuantity(addProductInCartDto));

    }

    @PostMapping("deduction")
    public ResponseEntity<OrderDto> DecreaseProductQuantity(@RequestBody AddProductInCartDto addProductInCartDto){
        //
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.DecreaseProductQuantity(addProductInCartDto));

    }

    @PostMapping("placeOrder")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderDto placeOrderDto){
        //
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.placeOrder(placeOrderDto));

    }
}
