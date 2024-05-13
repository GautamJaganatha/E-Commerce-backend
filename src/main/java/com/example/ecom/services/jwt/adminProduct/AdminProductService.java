package com.example.ecom.services.jwt.adminProduct;

import com.example.ecom.dto.ProductDto;

import java.io.IOException;
import java.util.List;

public interface AdminProductService {

    ProductDto addProduct(ProductDto productDto) throws IOException;




    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsByName(String name);

    boolean deleteProducts(Long id);
}
