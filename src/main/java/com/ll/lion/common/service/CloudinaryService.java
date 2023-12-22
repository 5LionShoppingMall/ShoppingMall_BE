package com.ll.lion.common.service;

import com.ll.lion.common.entity.Image;
import com.ll.lion.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    public Map upload(MultipartFile file) throws IOException;
    //public Image upload(MultipartFile file, Product product) throws IOException;
    //public String upload(MultipartFile file) throws IOException;
    //public Map upload(MultipartFile multipartFile) throws IOException;
}
