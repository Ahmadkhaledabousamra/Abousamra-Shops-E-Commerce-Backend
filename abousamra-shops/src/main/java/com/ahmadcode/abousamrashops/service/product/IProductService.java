package com.ahmadcode.abousamrashops.service.product;

import com.ahmadcode.abousamrashops.dto.ProductDto;
import com.ahmadcode.abousamrashops.model.Product;
import com.ahmadcode.abousamrashops.request.product.AddProductRequest;
import com.ahmadcode.abousamrashops.request.product.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category , String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand , String name);
    Long countProductsByBrandAndName(String brand , String name);

    List<ProductDto> getConvertedproducts(List<Product> products);

    ProductDto convertToDto(Product product);
}