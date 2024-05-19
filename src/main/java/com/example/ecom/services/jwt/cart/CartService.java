package com.example.ecom.services.jwt.cart;

import com.example.ecom.dto.AddProductInCartDto;
import com.example.ecom.dto.OrderDto;
import com.example.ecom.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {

    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);

    OrderDto getCartByUserID(Long userId);

    OrderDto applyCoupon(Long userId, String code);

    OrderDto IncreaseProductQuantity(AddProductInCartDto addProductInCartDto);

    public OrderDto DecreaseProductQuantity(AddProductInCartDto addProductInCartDto);

    OrderDto placeOrder(PlaceOrderDto placeOrderDto);

    List<OrderDto> getMyPlacedOrders(Long userId);

    OrderDto searchOrderByTrackingId(UUID trackingId);
}
