package com.ahmadcode.abousamrashops.controller;

import com.ahmadcode.abousamrashops.dto.ProductDto;
import com.ahmadcode.abousamrashops.exception.AlReadyExistsException;
import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.Product;
import com.ahmadcode.abousamrashops.request.product.AddProductRequest;
import com.ahmadcode.abousamrashops.request.product.ProductUpdateRequest;
import com.ahmadcode.abousamrashops.response.ApiResponse;
import com.ahmadcode.abousamrashops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/products")
@CrossOrigin
public class ProductController {
    @Autowired
    private  IProductService  productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        try{
            List<Product> products = productService.getAllProducts();
            List<ProductDto> convertedProducts = productService.getConvertedproducts(products);
            return ResponseEntity.ok(new ApiResponse("Success!",convertedProducts));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductsById(@PathVariable Long productId){
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Success!", productDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try {
            System.out.println(product.getCategory().getName());
            Product theproduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Add Product Success!",theproduct));
        }catch (AlReadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request , @PathVariable  Long productId){
        try {
            Product product = productService.updateProduct(request , productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Update product success!",productDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{ProductId}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete product sucess!",productId));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));

        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName , @PathVariable String productName){
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
            List<ProductDto> convertedProducts = productService.getConvertedproducts(products);
            if (products.isEmpty()){
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found with name: "+productName,null));
            }
            return ResponseEntity.ok(new ApiResponse("success",convertedProducts));
        } catch (Exception e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProducByCategoryAndBrand(@RequestParam String category , @RequestParam String brand){
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category,brand);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products found" , null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedproducts(products);
            return ResponseEntity.ok(new ApiResponse("Success!",convertedProducts));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error" , e.getMessage()));
        }
    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name){
        try {
            List<Product> products = productService.getProductsByName(name);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products found" , null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedproducts(products);
            return ResponseEntity.ok(new ApiResponse("Success!",convertedProducts));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error" , e.getMessage()));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand){
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products found" , null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedproducts(products);
            return ResponseEntity.ok(new ApiResponse("Product found!",convertedProducts));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error" , e.getMessage()));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category){
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products found" , null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!",products));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error" , e.getMessage()));
        }
    }

    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand , @RequestParam String name){
        try{
            var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!",productCount));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));

        }
    }

}
