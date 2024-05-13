package com.example.ecom.controllers.Customer;

import com.example.ecom.dto.ProductDto;
import com.example.ecom.services.jwt.Customer.CustomerProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer")
public class CustomerProductController {

    private final CustomerProductService customerProductService;

    @GetMapping("getProducts")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> productDtos = customerProductService.getAllProducts();

        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("searchProducts/{name}")
    public ResponseEntity<List<ProductDto>> getAllProductsByName(@PathVariable String name){
        List<ProductDto> productDtos = customerProductService.getAllProductsByName(name);

        return ResponseEntity.ok(productDtos);
    }
}
