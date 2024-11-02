package org.buaa.project.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 图片接口层
 */
public interface ImageService {
    /**
     * 上传图片
     */
    String uploadImage(MultipartFile file);

}
