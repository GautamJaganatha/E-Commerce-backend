package com.example.ecom.services.jwt.Customer.Wishlist;

import com.example.ecom.dto.WishlistDto;

import java.util.List;

public interface WishlistService {


    WishlistDto addProductToWishlist(WishlistDto wishlistDto);

    List<WishlistDto> getWishlistByUserId(Long userId);


}
