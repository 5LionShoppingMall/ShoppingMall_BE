package com.ll.lion.product.controller;

import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.product.dto.ProductDto;
import com.ll.lion.product.dto.ProductRequestDto;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<?> productMain() {
        ResponseDto<ProductDto> responseDto;
        try {
            List<Product> productEntities = productService.findListAll();
            List<ProductDto> productDtos = productEntities.stream().map(ProductDto::new).toList();
            responseDto = ResponseDto.<ProductDto>builder().listData(productDtos).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto = ResponseDto.<ProductDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerProduct(@RequestBody ProductRequestDto reqDto) {
        ResponseDto<ProductDto> responseDto;
        try {
            Product productEntity = productService.create(ProductDto.toEntity(reqDto.requestObjectValidate(reqDto)));
            responseDto = ResponseDto.<ProductDto>builder().objData(new ProductDto(productEntity)).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto = ResponseDto.<ProductDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }
}
