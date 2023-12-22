package com.ll.lion.product.controller;

import com.ll.lion.common.dto.ImageDto;
import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.common.service.CloudinaryService;
import com.ll.lion.product.dto.ProductDto;
import com.ll.lion.product.dto.ProductPageDto;
import com.ll.lion.product.dto.ProductRequestDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;

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

    /*@PostMapping("/register")
    public ResponseEntity<?> registerProduct(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestParam MultipartFile[] files,
                                             @Valid @RequestBody ProductRequestDto reqDto) {*/
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerProduct(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestPart(value = "files") MultipartFile[] files,
                                             @RequestPart(value = "productInfo") @Valid ProductRequestDto reqDto) {
        log.info("상품등록컨트롤러");
        log.info(reqDto.toString());
        log.info("인증유저? : {}", userDetails.getUsername());
        log.info("파일? : {}", Arrays.toString(files));
        try {
            if (userDetails.getUsername().isEmpty()) {
                throw new UsernameNotFoundException("로그인 정보가 없습니다.");
            }

            User user = userService.getUserByEmail(userDetails.getUsername()).get();

            /*User user = User.builder()
                    .email(userInfoDto.getEmail())
                    .phoneNumber(userInfoDto.getPhoneNumber())
                    .address(userInfoDto.getAddress())
                    .build();*/

            List<Map> results = new ArrayList<>();

            ProductDto productDto = new ProductDto(reqDto);

            List<ImageDto> imageDtoList = new ArrayList<>();

            for (MultipartFile file : files) {
                Map result = cloudinaryService.upload(file);

                ImageDto imageDto = ImageDto.builder()
                        .imageId(result.get("public_id").toString())
                        .name(result.get("original_filename").toString())
                        .url(result.get("url").toString())
                        .size(Long.parseLong(result.get("bytes").toString()))
                        .build();

                imageDtoList.add(imageDto);
                results.add(result);
            }

            productDto.setImages(imageDtoList);

            Product entity = ProductDto.toEntity(productDto);

            Product productEntity = productService.create(results, entity, user);

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
