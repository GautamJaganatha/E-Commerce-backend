package com.example.ecom.controllers.admin;

import com.example.ecom.dto.FAQDto;
import com.example.ecom.dto.ProductDto;
import com.example.ecom.entity.Product;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.services.jwt.admin.category.FAQ.FAQService;
import com.example.ecom.services.jwt.adminProduct.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/")
@CrossOrigin("*")
public class AdminProductController {

    private final AdminProductService adminProductService;

    private final ProductRepository productRepository;

    private final FAQService faqService;

    private static final Logger log = LoggerFactory.getLogger(AdminProductController.class);



    @PostMapping("addProducts")
    public ResponseEntity<ProductDto> addProduct(@ModelAttribute ProductDto productDto) throws IOException {
        ProductDto productDto1 = adminProductService.addProduct(productDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(productDto1);
    }

    @GetMapping("getProducts")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> productDtos = adminProductService.getAllProducts();

        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("searchProducts/{name}")
    public ResponseEntity<List<ProductDto>> getAllProductsByName(@PathVariable String name){
        List<ProductDto> productDtos = adminProductService.getAllProductsByName(name);

        return ResponseEntity.ok(productDtos);
    }

    @DeleteMapping("deleteProducts/{id}")
    public  ResponseEntity<Void> deleteProducts(@PathVariable  Long id){
       boolean deleted = adminProductService.deleteProducts(id);
       if (deleted){
           return ResponseEntity.noContent().build();
       }
       return ResponseEntity.notFound().build();
    }

    @PostMapping("faq/{productId}")
    public ResponseEntity<FAQDto> postFAQ(@PathVariable Long productId, @RequestBody FAQDto faqDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.postFAQ(productId, faqDto));
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) throws IOException {
        ProductDto productDto = adminProductService.getProductById(productId);
        if (productDto != null) {
            return ResponseEntity.ok(productDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        @PutMapping("updateProduct/{productId}")
        public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId,@ModelAttribute ProductDto productDto) throws IOException {
            log.debug("Received request to update product with id: {}", productId);

            System.out.println("Received ProductDTO: " + productDto);


            ProductDto updatedProduct = adminProductService.updateProduct(productId, productDto);
            if(updatedProduct != null){
                return ResponseEntity.ok(updatedProduct);
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }

}
