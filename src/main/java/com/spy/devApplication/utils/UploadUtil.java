package com.spy.devApplication.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.spy.devApplication.common.ErrorCode;
import com.spy.devApplication.exception.BusinessException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 文章上传工具类
 */
public class UploadUtil {

    // 阿里云OSS服务配置
    public static final String ALI_DOMAIN = "**";
    // 地域节点
    public static final String endpoint = "**";
    public static final String accessKeyId = "**";
    public static final String accessKeySecret = "**";

    

    /**
     * 阿里云OSS文件上传
     * @param file
     * @return
     */
    public static String uploadImageOss(MultipartFile file) {
        if(file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String ext = FilenameUtils.getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");

        if(!(ext.equals("png") || ext.equals("jpg"))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "格式错误");
        }
        String fileName = uuid + "." + ext;

        //OSS客户端对象
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            oss.putObject(
                    "dev-coff",
                    fileName,
                    file.getInputStream()
            );
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传失败");
        }

        // 关闭
        oss.shutdown();
        return ALI_DOMAIN + fileName;
    }


}
