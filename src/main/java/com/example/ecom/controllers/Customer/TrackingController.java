package com.example.ecom.controllers.Customer;

import com.example.ecom.dto.OrderDto;
import com.example.ecom.services.jwt.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer/")
public class TrackingController {

    private final CartService cartService;

    @GetMapping("order/{trackingId}")
    public ResponseEntity<OrderDto> searchOrderByTrackingId(@PathVariable UUID trackingId){
        OrderDto orderDto = cartService.searchOrderByTrackingId(trackingId);
        if(orderDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(orderDto);
    }
}
