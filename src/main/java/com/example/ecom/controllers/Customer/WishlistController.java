package com.example.ecom.controllers.Customer;

import com.example.ecom.dto.WishlistDto;
import com.example.ecom.services.jwt.Customer.Wishlist.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer/")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("Wishlist")
    public ResponseEntity<?> addProductToWishlist(@RequestBody WishlistDto wishlistDto){
        WishlistDto postedWishlistDto = wishlistService.addProductToWishlist(wishlistDto);
        if(postedWishlistDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something Went Wrong Sorry!.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(postedWishlistDto);
    }

    @GetMapping("Wishlist/{userId}")
    public ResponseEntity<List<WishlistDto>> getWishlistByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }
}
