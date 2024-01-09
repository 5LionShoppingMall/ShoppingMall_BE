package com.ll.lion.common.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.ll.lion.common.config.NcpS3Properties;
import com.ll.lion.common.entity.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileService {
    private final NcpS3Properties ncpS3Properties;
    private final AmazonS3Client amazonS3Client;
    private final CloudinaryService cloudinaryService;

    // Ncp
    public List<Image> uploadImages(List<MultipartFile> multipartFiles, String filePath, Long id) {
        List<Image> images = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            String originalFileName = multipartFile.getOriginalFilename();
            String newFileName = filePath + "_" + id + "_" + UUID.randomUUID() + "_" + originalFileName;
            String folderName = createFolderNameWithTodayDate();
            String keyName = filePath + "/" + folderName + "/" + newFileName;
            String uploadUrl = "";

            try (InputStream inputStream = multipartFile.getInputStream()) {

                amazonS3Client.putObject(
                        new PutObjectRequest(
                                ncpS3Properties.getS3().getBucketName(), keyName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                uploadUrl = ncpS3Properties.getS3().getEndPoint() + "/" + ncpS3Properties.getS3().getBucketName() + "/" + keyName;

            } catch (SdkClientException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Image image = Image.builder()
                    .imageId(keyName)
                    .name(newFileName)
                    .url(uploadUrl)
                    .size(multipartFile.getSize())
                    .build();

            images.add(image);
        }

        return images;
    }

    private String createRandomFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID() + "." + ext;
    }

    private String createFolderNameWithTodayDate() {
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());

        return year + "/" + month;
    }

    // Cloudinary
    /*public List<Image> uploadImages(List<MultipartFile> files) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Map map = cloudinaryService.upload(file);
                Image image = Image.builder()
                        .imageId((String) map.get("public_id"))
                        .name(file.getOriginalFilename() + UUID.randomUUID())
                        .url((String) map.get("url"))
                        .size(Long.parseLong(map.get("bytes").toString()))
                        .build();
                images.add(image);
            } catch (IOException e) {
                log.error("파일 업로드 실패");
                log.error(e.getStackTrace());
            }
        }
        return images;
    }*/
}
