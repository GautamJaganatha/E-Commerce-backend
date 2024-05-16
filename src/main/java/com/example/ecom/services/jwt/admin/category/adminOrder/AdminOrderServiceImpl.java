package com.example.ecom.services.jwt.admin.category.adminOrder;

import com.example.ecom.dto.OrderDto;
import com.example.ecom.entity.Order;
import com.example.ecom.enums.OrderStatus;
import com.example.ecom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminOrderServiceImpl implements AdminOrderService{

    private final OrderRepository orderRepository;


    public List<OrderDto> getAllPlacedOrderDto(){
        List<Order> orderList = orderRepository.findAllByOrderStatusIn(List.of(OrderStatus.PLACED, OrderStatus.SHIPPED, OrderStatus.DELIVERED));
        return orderList.stream().map(Order::getOrderDto).collect(Collectors.toList());
    }

    public OrderDto changeOrderStatus(Long orderId, String status){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()){
            Order order = optionalOrder.get();

            if(Objects.equals(status, "Shipped")){
                order.setOrderStatus(OrderStatus.SHIPPED);
            }else if(Objects.equals(status, "Delivered")){
                order.setOrderStatus(OrderStatus.DELIVERED);
            }
            return orderRepository.save(order).getOrderDto();
        }
        return null;
    }


}
