package com.example.ecom.controllers.admin;

import com.example.ecom.entity.Coupon;
import com.example.ecom.exception.ValidationException;
import com.example.ecom.services.jwt.admin.category.Coupon.AdminCoupnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/")
public class AdminCouponController {

    public final AdminCoupnService adminCoupnService;

    @PostMapping("addCoupons")
    public ResponseEntity<?> createCoupon(@RequestBody Coupon coupon){
        try {
            Coupon createdCoupon = adminCoupnService.createCoupon(coupon);
            return ResponseEntity.ok(createdCoupon);
        }
        catch (ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("getCoupons")
    public ResponseEntity<List<Coupon>> getAllCoupons(){
        return ResponseEntity.ok(adminCoupnService.getAllCoupons());
    }
}
