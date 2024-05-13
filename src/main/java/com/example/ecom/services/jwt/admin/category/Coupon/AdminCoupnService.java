package com.example.ecom.services.jwt.admin.category.Coupon;

import com.example.ecom.entity.Coupon;

import java.util.List;

public interface AdminCoupnService {

    Coupon createCoupon(Coupon coupon);

    List<Coupon> getAllCoupons();
}
