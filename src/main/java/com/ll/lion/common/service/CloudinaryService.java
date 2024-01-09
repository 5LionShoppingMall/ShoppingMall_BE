package com.ll.lion.common.service;

import com.ll.lion.common.entity.Image;
import com.ll.lion.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    Map upload(MultipartFile file) throws IOException;
}
