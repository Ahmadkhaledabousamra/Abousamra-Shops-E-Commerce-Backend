package com.ahmadcode.abousamrashops.service.image;

import com.ahmadcode.abousamrashops.dto.ImageDto;
import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.Image;
import com.ahmadcode.abousamrashops.model.Product;
import com.ahmadcode.abousamrashops.repository.ImageRepository;
import com.ahmadcode.abousamrashops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.lang.module.ResolutionException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Service

public class ImageService implements IImageService {
    @Autowired
    private  ImageRepository imageRepository;
    @Autowired
    private  IProductService productService;
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(()->new ResolutionException("No Image Found with id: "+id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository::delete,()->{
                    throw new ResourceNotFoundException("No Image Found with id: "+id);
                });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product  product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files){
             try {
                    Image image = new Image();
                    image.setFileName(file.getOriginalFilename());
                    image.setFileType(file.getContentType());
                    image.setImage(new SerialBlob(file.getBytes()));
                    image.setProduct(product);
                    String buildDownloadUrl = "/api/v1/images/image/download/";
                    String downloadUrl = buildDownloadUrl+image.getId();
                    image.setDownloadUrl(downloadUrl);
                    Image savedImage = imageRepository.save(image);

                    savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
                    imageRepository.save(savedImage);

                    ImageDto imageDto = new ImageDto();
                    imageDto.setImageId(savedImage.getId());
                    imageDto.setImageName(savedImage.getFileName());
                    imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                    savedImageDto.add(imageDto);

             }catch (IOException | SQLException e){
                    throw new RuntimeException(e.getMessage());
             }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (SQLException | IOException e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
