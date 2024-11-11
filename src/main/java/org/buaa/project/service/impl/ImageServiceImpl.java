package org.buaa.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.exception.ServiceException;
import org.buaa.project.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.buaa.project.common.enums.ServiceErrorCodeEnum.IMAGE_UPLOAD_ERROR;

/**
 * 图片接口实现层
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${spring.aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${spring.aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${spring.aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${spring.aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * oss 上传图片
     */
    public String ossUploadImage(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = createNewFileName(file.getOriginalFilename());
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.putObject(bucketName, fileName, inputStream);
            ossClient.shutdown();
            return fileName;
        } catch (IOException e) {
            throw new ServiceException(IMAGE_UPLOAD_ERROR);
        }
    }

    private String createNewFileName(String originalFilename) {
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        String name = UUID.randomUUID().toString();
        String datePath = java.time.LocalDate.now().toString().replace("-", "/");
        return StrUtil.format("{}/{}.{}", datePath, name, suffix);
    }

}
