package com.example.ecom.controllers.Customer;

import com.example.ecom.dto.OrderedProductsResponseDto;
import com.example.ecom.dto.ReviewDto;
import com.example.ecom.services.jwt.Customer.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer/")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("ordered-products/{orderId}")
    public ResponseEntity<OrderedProductsResponseDto> getOrderedProductsDetailsByOrderId(@PathVariable Long orderId){
        return ResponseEntity.ok(reviewService.getOrderedProductsDetailsByOrderId(orderId));
    }

    @PostMapping("review")
    public ResponseEntity<?> giveReview(@ModelAttribute ReviewDto reviewDto) throws IOException {
        ReviewDto reviewDto1 = reviewService.giveReview(reviewDto);
        if (reviewDto1 == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something Went Wrong, Sorry");
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDto1);
    }
}
