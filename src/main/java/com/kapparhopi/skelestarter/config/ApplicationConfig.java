package com.kapparhopi.skelestarter.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudsearchdomain.model.Bucket;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Configuration
@EnableJpaRepositories(basePackages = "com.kapparhopi.skelestarter.backend.persistence.repositories")
@EntityScan(basePackages = "com.kapparhopi.skelestarter.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("classpath:application-common.properties")
@PropertySource("classpath:stripe.properties")
public class ApplicationConfig {

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Value("${aws.s3.secret}")
    private String secretKey;

    @Bean
    public AmazonS3 s3Client() {

        AWSCredentials credentials = new BasicAWSCredentials(
                awsProfileName,
                secretKey
        );


        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();

        return s3Client;
    }
}
