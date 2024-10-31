package org.buaa.project.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class OssConfig {
    @Value("${spring.aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${spring.aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${spring.aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${spring.aliyun.oss.bucketName}")
    private String bucketName;
}
