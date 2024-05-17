package com.example.ecom.services.jwt.Customer.Wishlist;

import com.example.ecom.dto.WishlistDto;
import com.example.ecom.entity.Product;
import com.example.ecom.entity.User;
import com.example.ecom.entity.Wishlist;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.repository.UserRespository;
import com.example.ecom.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WishlistServiceImpl implements WishlistService{

    private final UserRespository userRespository;

    private final ProductRepository productRepository;

    private final WishlistRepository wishlistRepository;

    public WishlistDto addProductToWishlist(WishlistDto wishlistDto){
        Optional<Product> optionalProduct = productRepository.findById(wishlistDto.getProductId());
        Optional<User> optionalUser = userRespository.findById(wishlistDto.getUserId());

        if (optionalProduct.isPresent() && optionalUser.isPresent()){

            Wishlist wishlist = new Wishlist();
            wishlist.setProduct(optionalProduct.get());
            wishlist.setUser(optionalUser.get());

            return wishlistRepository.save(wishlist).getWishlistDto();
        }
        return null;
    }

    public List<WishlistDto> getWishlistByUserId(Long userId){
        return wishlistRepository.findAllByUserId(userId).stream().map(Wishlist::getWishlistDto).collect(Collectors.toList());
    }
}
