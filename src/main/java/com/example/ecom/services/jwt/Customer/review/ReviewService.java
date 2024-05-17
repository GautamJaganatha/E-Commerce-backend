package com.example.ecom.services.jwt.Customer.review;

import com.example.ecom.dto.OrderedProductsResponseDto;
import com.example.ecom.dto.ReviewDto;

import java.io.IOException;

public interface ReviewService {

    OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId);

    ReviewDto giveReview(ReviewDto reviewDto) throws IOException;
}
