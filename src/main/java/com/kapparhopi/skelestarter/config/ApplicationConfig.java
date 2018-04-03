package com.kapparhopi.skelestarter.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Configuration
@EnableJpaRepositories(basePackages = "com.kapparhopi.skelestarter.backend.persistence.repositories")
@EntityScan(basePackages = "com.kapparhopi.skelestarter.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("application-common.properties")
@PropertySource("stripe.properties")
public class ApplicationConfig {

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Bean
    public AmazonS3 s3Client() {

        AWSCredentials credentials = new ProfileCredentialsProvider(awsProfileName).getCredentials();
        Region region = Region.getRegion(Regions.US_EAST_1);

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        s3Client.setRegion(region);

        return s3Client;
    }
}
