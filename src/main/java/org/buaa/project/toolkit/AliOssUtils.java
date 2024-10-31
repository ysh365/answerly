package org.buaa.project.toolkit;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.buaa.project.config.OssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云 OSS 工具类
 */

@Component
public class AliOssUtils {
    @Autowired
    private OssConfig aliOSSProperties;

    public String upload(MultipartFile file) throws IOException {

        //获取阿里云OSS参数
        String endpoint = aliOSSProperties.getEndpoint();
        String accessKeyId = aliOSSProperties.getAccessKeyId();
        String accessKeySecret = aliOSSProperties.getAccessKeySecret();
        String bucketName = aliOSSProperties.getBucketName();

//        System.out.println("test " + endpoint + " " + accessKeyId + " " + accessKeySecret + " " + bucketName);


        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();


        // 避免文件覆盖，需要使用UUID将文件重命名
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;

        // 关闭ossClient
        ossClient.shutdown();

        // 把上传到oss的路径返回
        return url;

    }

}
