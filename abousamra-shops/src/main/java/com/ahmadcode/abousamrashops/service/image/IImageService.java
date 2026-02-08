package com.ahmadcode.abousamrashops.service.image;

import com.ahmadcode.abousamrashops.dto.ImageDto;
import com.ahmadcode.abousamrashops.model.Image;
import com.ahmadcode.abousamrashops.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files , Long productId);
    void updateImage(MultipartFile file , Long imageId);
}
