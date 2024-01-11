package com.ll.lion.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.lion.common.dto.ImageDto;
import com.ll.lion.common.dto.RequestImagesDto;
import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.common.entity.Image;
import com.ll.lion.common.service.CloudinaryService;
import com.ll.lion.common.service.FileService;
import com.ll.lion.community.post.entity.Post;
import com.ll.lion.product.dto.*;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.service.ProductService;
import com.ll.lion.user.dto.UserInfoDto;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ObjectMapper objectMapper;
    private final ProductService productService;
    private final UserService userService;

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> productDelete(@PathVariable("id") Long productId,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        User userEntity = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("인증 정보를 찾을 수 없습니다."));
        Product productEntity = productService.findProduct(productId);

        productService.deleteProduct(productEntity, userEntity.getEmail());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 삭제되었습니다.", null,
                        null, null
                )
        );
    }

    @PutMapping("/{id}/modify")
    public ResponseEntity<?> productModify(@PathVariable("id") Long productId,
                                           @AuthenticationPrincipal UserDetails userDetails,
                                           @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                                           @RequestParam(value = "imagesJson", required = false) String imagesJson,
                                           @RequestParam(value = "deletedImages", required = false) String deletedImagesJson,
                                           @RequestPart(value = "productInfo") @Valid ProductRequestDto reqDto)
            throws JsonProcessingException {

        List<Image> images = parseImagesJson(imagesJson);
        List<Image> deletedImages = parseImagesJson(deletedImagesJson);

        Product productEntity = productService.findProduct(productId);

        productEntity = productService.modifyProduct(
                userDetails.getUsername(),
                productEntity.toBuilder()
                        .title(reqDto.getTitle())
                        .price(reqDto.getPrice())
                        .description(reqDto.getDescription())
                        .status(reqDto.getStatus())
                        .build(),
                multipartFiles,
                images,
                deletedImages
        );

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "상품 수정 성공",
                null,
                null,
                ProductDetailDto.from(productEntity)
        ));
    }

    private List<Image> parseImagesJson(String imagesJson) throws JsonProcessingException {
        if (imagesJson == null || imagesJson.isBlank()) {
            return Collections.emptyList();
        }
        List<ImageDto> imageDtos = objectMapper.readValue(imagesJson, new TypeReference<>() {
        });
        return imageDtos.stream().map(ImageDto::toEntity).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> productDetail(@PathVariable("id") Long id) {
        Product product = productService.findProduct(id);
        return ResponseEntity.ok(
                new ResponseDto<>(HttpStatus.OK.value(),
                        "상품 정보 조회 성공", null,
                        null, ProductDetailDto.from(product)));
    }

    @GetMapping("/list")
    public ResponseEntity<?> productMain(Pageable pageable) {
        Page<Product> productEntities = productService.findPageList(pageable);
        List<ProductListDto> productDtos = productEntities.stream().map(ProductListDto::from)
                .collect(Collectors.toList());
        Page<ProductListDto> pageProduct = new PageImpl<>(productDtos, pageable, productEntities.getTotalElements());

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "리스트 조회 성공", null,
                null, new ProductPageDto<>(pageProduct)));
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerProduct(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestPart(value = "files", required = false) List<MultipartFile> multipartFile,
                                             @RequestPart(value = "productInfo") @Valid ProductRequestDto reqDto) {

        if (userDetails.getUsername().isEmpty()) {
            return handleUsernameNotFoundException();
        }

        User user = userService.getUserByEmail(userDetails.getUsername()).get();
        ProductDetailDto requestProductDto = ProductDetailDto.from(reqDto);
        Product productEntity = productService.createProduct(requestProductDto.toEntity(), multipartFile, user);

        ProductDetailDto productEntityToDto = ProductDetailDto.from(productEntity);

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "상품 등록 성공", null,
                null, productEntityToDto));
    }

    private ResponseEntity<?> handleUsernameNotFoundException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ResponseDto<>(
                        HttpStatus.UNAUTHORIZED.value(),
                        null, "로그인 정보가 없습니다.",
                        null, null));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getPostsByKeyword(@RequestParam String keyword,
                                                           @PageableDefault(size = 9) Pageable pageable) {
        Page<Product> products = productService.findPostsByKeyword(keyword, pageable);

        List<ProductListDto> productDtos = products.stream().map(ProductListDto::from)
                .collect(Collectors.toList());

        Page<ProductListDto> pageProduct = new PageImpl<>(productDtos, pageable, products.getTotalElements());

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "리스트 조회 성공", null,
                null, new ProductPageDto<>(pageProduct)));
    }
}
