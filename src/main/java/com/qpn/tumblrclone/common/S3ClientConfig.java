package com.qpn.tumblrclone.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3ClientConfig {
    @Value("${s3.endpoint}") private String S3_ENDPOINT;
    @Value("${s3.accessKey}") private String ACCESS_KEY;
    @Value("${s3.secretKey}") private String SECRET_KEY;

    @Bean
    public S3Client s3Client(){
        AwsBasicCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);

        return S3Client.builder()
                .endpointOverride(URI.create(S3_ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.US_EAST_1)
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @Bean // ???????????????????????????/
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .endpointOverride(URI.create("http://localhost:9000")) // host mà TRÌNH DUYỆT truy cập được
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
//                        AwsBasicCredentials.create("minioadmin", "minioadmin"))) Cái tội copy vô tội vạ mà ko review lại đây
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))

                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }
}
