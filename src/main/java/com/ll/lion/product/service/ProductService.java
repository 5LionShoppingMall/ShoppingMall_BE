package com.ll.lion.product.service;

import com.ll.lion.common.entity.Image;
import com.ll.lion.common.enums.FilePathType;
import com.ll.lion.common.exception.DataNotFoundException;
import com.ll.lion.common.exception.FileHandlingException;
import com.ll.lion.common.repository.ImageRepository;
import com.ll.lion.common.service.FileService;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.exception.DataInsertionFailureException;
import com.ll.lion.product.repository.ProductRepository;
import com.ll.lion.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final FileService fileService;

    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 글을 찾을 수 없습니다."));
    }

    public Page<Product> findPageList(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending());

        Page<Product> pageProduct = productRepository.findAllByOrderByCreatedAtDesc(sortedPageable);

        return Optional.of(pageProduct)
                .filter(Slice::hasContent)
                .orElseThrow(() -> new DataNotFoundException("등록된 상품이 없습니다."));
    }

    public List<Product> findListAll() {
        return Optional.of(productRepository.findAll())
                .filter(products -> !products.isEmpty())
                .orElseThrow(() -> new NoSuchElementException(""));
    }

    @Transactional
    public Product create(final Product product, final List<MultipartFile> files, final User user) {
        log.info("상품 등록 서비스");
        Product registeredProduct = product.toBuilder()
                .seller(user)
                .build();

        try {
            return saveProduct(productRepository.save(registeredProduct), files, null);
        } catch (DataAccessException e) {
            log.error("DataAccessException occurred while creating Product: " + e.getMessage());
            throw new DataInsertionFailureException("데이터 등록에 실패했습니다.", e);
        }
    }

    @Transactional
    public Product modifyProduct(String email, Product product, List<MultipartFile> multipartFiles, List<Image> images) {
        if (!product.getSeller().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        try {
            return saveProduct(product, multipartFiles, images);
        } catch (DataAccessException e) {
            log.error("DataAccessException occurred while creating Product: " + e.getMessage());
            throw new DataInsertionFailureException("데이터 수정에 실패했습니다.", e);
        }
    }

    private Product saveProduct(Product product, List<MultipartFile> multipartFiles, List<Image> images) {
        List<Image> newImages = new ArrayList<>();

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            log.info("상품 등록 파일이 있다면?");
            newImages = fileService.uploadImages(multipartFiles, FilePathType.PRODUCT.getValue(), product.getId());

        }

        if (images != null && !images.isEmpty()) {
            for (Image reqImage : images) {
                Image image = imageRepository.findById(reqImage.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid image ID: " + reqImage.getId()));
                Image saveImage = imageRepository.save(image.toBuilder().url(reqImage.getUrl()).build());
                newImages.add(saveImage);
            }
        }

        for (Image image : newImages) {
            product.addImage(image);
        }

        return productRepository.save(product);
    }
}
