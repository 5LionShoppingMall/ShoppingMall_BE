package com.ll.lion.product.service;

import com.ll.lion.common.entity.Image;
import com.ll.lion.common.enums.FilePathType;
import com.ll.lion.common.exception.DataNotFoundException;
import com.ll.lion.common.exception.FileHandlingException;
import com.ll.lion.common.repository.ImageRepository;
import com.ll.lion.common.service.FileService;
import com.ll.lion.community.post.entity.Post;
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

    public Product findProduct(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 글을 찾을 수 없습니다."));
    }

    public Page<Product> findPageList(final Pageable pageable) {
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
    public Product createProduct(final Product product, final List<MultipartFile> files, final User user) {
        log.info("상품 등록 서비스");
        Product registeredProduct = product.toBuilder()
                .seller(user)
                .build();

        try {
            return saveProduct(productRepository.save(registeredProduct), files, null, null);
        } catch (DataAccessException e) {
            log.error("DataAccessException occurred while creating Product: " + e.getMessage());
            throw new DataInsertionFailureException("데이터 등록에 실패했습니다.", e);
        }
    }

    @Transactional
    public Product modifyProduct(final String email, final Product product, final List<MultipartFile> multipartFiles, final List<Image> images, final List<Image> deletedImages) {
        if (!product.getSeller().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        try {
            return saveProduct(product, multipartFiles, images, deletedImages);
        } catch (DataAccessException e) {
            log.error("DataAccessException occurred while creating Product: " + e.getMessage());
            throw new DataInsertionFailureException("데이터 수정에 실패했습니다.", e);
        }
    }

    private Product saveProduct(Product product, List<MultipartFile> multipartFiles, List<Image> images, List<Image> deletedImages) {
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

        for (Image newImage : newImages) {
            product.addImage(newImage);
        }

        if (deletedImages != null && !deletedImages.isEmpty()) {
            deletedImages(deletedImages);

            // 이미지 삭제 후 남은 이미지로 업데이트
            List<Image> remainingImages = imageRepository.findByProductId(product.getId());
            product.updateImages(remainingImages);
        }

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(final Product product, final String email) {
        if (!product.getSeller().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }

        if (product.getImages() != null && !product.getImages().isEmpty()) {
            deletedImages(product.getImages());
        }

        productRepository.delete(product);
    }

    private void deletedImages(List<Image> images) {
        for (Image deletedImage : images) {
            Image image = imageRepository.findById(deletedImage.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid image ID: " + deletedImage.getId()));
            fileService.deleteImage(image.getImageId());
            imageRepository.delete(image);
        }
    }

    public Page<Product> findPostsByKeyword(String keyword, Pageable pageable) {
        return productRepository.findAllByKeyword(pageable, keyword);
    }
}
