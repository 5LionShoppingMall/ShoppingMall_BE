package com.ll.lion.product.controller;

import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.product.dto.ProductDto;
import com.ll.lion.product.dto.ProductPageDto;
import com.ll.lion.product.dto.ProductRequestDto;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> productMain(Pageable pageable) {
        log.info(pageable.toString());
        try {
            //List<Product> productEntities = productService.findListAll();
            Page<Product> productEntities = productService.findPageList(pageable);
            List<ProductDto> productDtos = productEntities.stream().map(ProductDto::new).toList();
            Page<ProductDto> pageProduct = new PageImpl<>(productDtos, pageable, productEntities.getTotalElements());

            return ResponseEntity.ok(new ResponseDto<>(
                    HttpStatus.OK.value(),
                    "리스트 조회 성공", null,
                    null, new ProductPageDto<>(pageProduct)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseDto<>(
                            HttpStatus.NOT_FOUND.value(),
                            null, e.getMessage(),
                            null, null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerProduct(@Valid @RequestBody ProductRequestDto reqDto) {
        log.info(reqDto.toString());
        try {
            Product productEntity = productService.create(ProductDto.toEntity(new ProductDto(reqDto)));

            return ResponseEntity.ok(new ResponseDto<>(
                    HttpStatus.OK.value(),
                    "상품 등록 성공", null,
                    null, new ProductDto(productEntity)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ResponseDto<>(
                            HttpStatus.BAD_REQUEST.value(),
                            null, e.getMessage(),
                            null, null));
        }
    }
}
