package com.example.ecom.services.jwt.admin.category.Coupon;

import com.example.ecom.entity.Coupon;
import com.example.ecom.exception.ValidationException;
import com.example.ecom.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminCouponServiceImpl implements AdminCoupnService{

    private final CouponRepository couponRepository;


    public Coupon createCoupon(Coupon coupon){
        if(couponRepository.existsByCode(coupon.getCode())){
            throw new ValidationException("Coupon code already exists" );
        }
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllCoupons(){
        return couponRepository.findAll();
    }
}
