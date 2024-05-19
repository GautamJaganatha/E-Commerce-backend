package com.example.ecom.services.jwt.admin.category.adminOrder;

import com.example.ecom.dto.AnalyticsResponse;
import com.example.ecom.dto.OrderDto;
import com.example.ecom.entity.Order;
import com.example.ecom.enums.OrderStatus;
import com.example.ecom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
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



    public AnalyticsResponse calculateAnalytics(){
        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonthDate = currentDate.minusMonths(1);

        Long currentMonthOrders = getTotalOrderForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previousMonthOrders = getTotalOrderForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());

        Long currentMonthEarnings = getTotalEarningsForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previousMonthEarnings = getTotalEarningsForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());

        Long placed = orderRepository.countByOrderStatus(OrderStatus.PLACED);
        Long shipped = orderRepository.countByOrderStatus(OrderStatus.SHIPPED);
        Long delivered = orderRepository.countByOrderStatus(OrderStatus.DELIVERED);

        return new AnalyticsResponse(placed, shipped, delivered, currentMonthOrders, previousMonthOrders, currentMonthEarnings, previousMonthEarnings );

    }

    private Long getTotalEarningsForMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endOfMonth = calendar.getTime();

        List<Order> orders = orderRepository.findByDateBetweenAndOrderStatus(startOfMonth, endOfMonth, OrderStatus.SHIPPED);

        Long sum = 0L;
        for (Order order: orders){
            sum +=order.getAmount();
        }
        return sum;
    }

    public Long getTotalOrderForMonth(int month, int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endOfMonth = calendar.getTime();

        List<Order> orders = orderRepository.findByDateBetweenAndOrderStatus(startOfMonth, endOfMonth, OrderStatus.SHIPPED);

        return (long) orders.size();
    }


}
