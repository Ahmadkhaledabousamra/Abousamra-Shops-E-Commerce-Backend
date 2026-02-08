package com.ahmadcode.abousamrashops.service.category;

import com.ahmadcode.abousamrashops.exception.AlReadyExistsException;
import com.ahmadcode.abousamrashops.exception.CategoryNotFoundException;
import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.Category;
import com.ahmadcode.abousamrashops.model.Product;
import com.ahmadcode.abousamrashops.repository.CategoryRepository;
import com.ahmadcode.abousamrashops.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService{

    @Autowired
    private  CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("Category Not Found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.ofNullable(category)
                .filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new AlReadyExistsException(category.getName()+" Already exists!"));
    }

    @Override
    public Category updateCategory(Category category , Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                })
                .orElseThrow(() -> new CategoryNotFoundException("Category Not Found!!"));
    }


    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
       categoryRepository.findById(id)
               .ifPresentOrElse(categoryRepository::delete,()-> {
                   throw new ResourceNotFoundException("Category Not Found!");
               });
    }
}
