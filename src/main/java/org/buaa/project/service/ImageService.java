package org.buaa.project.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 图片接口层
 */
public interface ImageService {

    /**
     * oss上传图片
     */
    String ossUploadImage(MultipartFile file);

}
