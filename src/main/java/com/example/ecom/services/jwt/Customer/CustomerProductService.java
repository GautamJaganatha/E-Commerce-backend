package com.example.ecom.services.jwt.Customer;

import com.example.ecom.dto.ProductDto;


import java.util.List;


public interface CustomerProductService {


    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsByName(String name);

}
