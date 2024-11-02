package org.buaa.project.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.consts.SystemConstants;
import org.buaa.project.common.convention.exception.ServiceException;
import org.buaa.project.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.buaa.project.common.enums.ServiceErrorCodeEnum.IMAGE_UPLOAD_ERROR;

/**
 * 图片接口实现层
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    /**
     * 上传图片
     */
    public String uploadImage(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileName = createNewFileName(originalFilename);
            file.transferTo(new File(SystemConstants.IMAGE_UPLOAD_DIR, fileName));
            return fileName;
        } catch (IOException e) {
            throw new ServiceException(IMAGE_UPLOAD_ERROR);
        }
    }

    private String createNewFileName(String originalFilename) {
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        String name = UUID.randomUUID().toString();
        int hash = name.hashCode();
        int d1 = hash & 0xF;
        File dir = new File(SystemConstants.IMAGE_UPLOAD_DIR, StrUtil.format("/{}", d1));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return StrUtil.format("/{}/{}.{}", d1, name, suffix);
    }


}
