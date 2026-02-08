package com.ahmadcode.abousamrashops.service.product;

import com.ahmadcode.abousamrashops.dto.ImageDto;
import com.ahmadcode.abousamrashops.dto.ProductDto;
import com.ahmadcode.abousamrashops.exception.AlReadyExistsException;
import com.ahmadcode.abousamrashops.exception.ProductNotFoundException;
import com.ahmadcode.abousamrashops.model.Category;
import com.ahmadcode.abousamrashops.model.Image;
import com.ahmadcode.abousamrashops.model.Product;
import com.ahmadcode.abousamrashops.repository.CategoryRepository;
import com.ahmadcode.abousamrashops.repository.ImageRepository;
import com.ahmadcode.abousamrashops.repository.ProductRepository;
import com.ahmadcode.abousamrashops.request.product.AddProductRequest;
import com.ahmadcode.abousamrashops.request.product.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.AlreadyBoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request)  {
        if(productExists(request.getName(),request.getBrand())){
            throw new AlReadyExistsException(request.getBrand()+" , "+request.getName()+" already exists , you may update this product instead! ");
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request , category));
    }

    private boolean productExists(String name , String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }

    private Product createProduct(AddProductRequest productRequest , Category category){
         Product p = new Product(
                productRequest.getName(),
                productRequest.getBrand(),
                productRequest.getPrice(),
                productRequest.getInventory(),
                productRequest.getDescription(),
                category
        );
        //System.out.println(p.getDescription() + " "+p.getCategory().getName());
         return p;
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                ()->{ throw new ProductNotFoundException("Product Not Found!");});
    };

    @Override
    public Product updateProduct(ProductUpdateRequest  request, Long productId) {
        return  productRepository.findById(productId)
                .map(existingProduct ->{
                    updateExistingProduct(existingProduct,request);
                    existingProduct.setInventory(request.getInventory()+existingProduct.getInventory());
                    return  productRepository.save(existingProduct);
                }).orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

    }

    private Product updateExistingProduct(Product excp , ProductUpdateRequest request){
        excp.setName(request.getName());
        excp.setBrand(request.getBrand());
        excp.setPrice(request.getPrice());
        excp.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        excp.setCategory(category);

        return excp;

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }

    @Override
    public List<ProductDto> getConvertedproducts(List<Product> products){
        return products.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product,ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image,ImageDto.class))
                .toList();

        productDto.setImages(imageDtos);
        return productDto;

    }
}
