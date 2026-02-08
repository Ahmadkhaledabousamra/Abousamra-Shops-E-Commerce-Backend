package com.ahmadcode.abousamrashops.controller;

import com.ahmadcode.abousamrashops.dto.ImageDto;
import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.Image;
import com.ahmadcode.abousamrashops.response.ApiResponse;
import com.ahmadcode.abousamrashops.service.image.IImageService;
import com.ahmadcode.abousamrashops.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/images")

public class ImageController {
    @Autowired
    private IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam  List<MultipartFile> files , @RequestParam Long productId){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload Success", imageDtos));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed!" , e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\""+image.getFileName()+"\"")
                .body(resource);
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId , @RequestBody MultipartFile file){
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Update Sucess!", null));
            }
        }catch (ResourceNotFoundException e){
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update Failed!", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId ){
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete Sucess!", null));
            }
        }catch (ResourceNotFoundException e){
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete Failed!", INTERNAL_SERVER_ERROR));
    }

}
