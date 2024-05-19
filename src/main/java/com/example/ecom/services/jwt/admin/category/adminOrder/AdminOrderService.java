package com.example.ecom.services.jwt.admin.category.adminOrder;

import com.example.ecom.dto.AnalyticsResponse;
import com.example.ecom.dto.OrderDto;

import java.util.List;

public interface AdminOrderService {

    List<OrderDto> getAllPlacedOrderDto();

    OrderDto changeOrderStatus(Long orderId, String status);

    AnalyticsResponse calculateAnalytics();
}
