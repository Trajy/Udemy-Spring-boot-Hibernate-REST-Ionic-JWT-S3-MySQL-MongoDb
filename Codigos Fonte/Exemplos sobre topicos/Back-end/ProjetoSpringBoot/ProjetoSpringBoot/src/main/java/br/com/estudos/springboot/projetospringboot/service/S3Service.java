package br.com.estudos.springboot.projetospringboot.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class S3Service {

    private Logger LOG = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3Cliente;

    @Value("${s3.bucket}")
    private String bucketName;

    public URI uploadFile(MultipartFile multipartFile){
        try {
            String fileName = multipartFile.getOriginalFilename();
            InputStream inputStream = multipartFile.getInputStream();
            String contentType = multipartFile.getContentType();
            return uploadFile(inputStream, fileName, contentType);
        }
        catch (IOException e) {
            LOG.error("getInputStream(): " + e.getMessage());
        }
        return null;
    }

    public URI uploadFile(InputStream inputStream, String fileName, String contentType){

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);

        try{
            LOG.info("Uploand Iniciado");
            s3Cliente.putObject(bucketName, fileName, inputStream, objectMetadata);
            LOG.info("Upload finalizado");
            return s3Cliente.getUrl(bucketName, fileName).toURI();
        }
        catch (AmazonServiceException e){
            LOG.error("AmazonServiceException: " + e.getMessage());
            LOG.error("Status code: " + e.getErrorCode());
        }
        catch (AmazonClientException e) {
            LOG.error("AmazonClientException: " + e.getMessage());
        }
        catch (URISyntaxException e) {
            LOG.error("toURI(): " + e.getMessage());
        }
        return null;
    }
}
