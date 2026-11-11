package com.qpn.tumblrclone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {
    @Autowired private S3Client s3Client;
    @Autowired private S3Presigner s3Presigner;
    @Value("${s3.bucket}") String S3_BUCKET;

    public String generateFileName(Integer userId, String postId,MultipartFile file){
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        return String.format("%d/%s/%s", userId, postId, fileName);
    }

    // Refactor here
    public void uploadToS3(String fileName, MultipartFile file) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(S3_BUCKET)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
    }

    public String generatePresignedUrl(String key) {
        // ????????????????????????????????????????????????????
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(S3_BUCKET)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .getObjectRequest(getRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
