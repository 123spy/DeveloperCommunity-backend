package com.spy.devApplication.controller;

import com.spy.devApplication.common.BaseResponse;
import com.spy.devApplication.common.ErrorCode;
import com.spy.devApplication.common.ResultUtils;
import com.spy.devApplication.exception.BusinessException;
import com.spy.devApplication.utils.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/util")
@Slf4j
public class UtilController {

    @PostMapping("uploadFileOss")
    public BaseResponse uploadFileOss(@RequestParam("file") MultipartFile file) {
        String uploadImage = UploadUtil.uploadImageOss(file);
        return ResultUtils.success(uploadImage);
    }

    @PostMapping("uploadFileLocal")
    public BaseResponse uploadFileLocal(@RequestParam("file") MultipartFile file) {
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

        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\avatar\\";
        String path = pre + fileName;
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传失败");
        }

        // 读取文件的路径
        ClassPathResource resource = new ClassPathResource("static/images/avatar/" + fileName);
        Path imgPathUrl;
        try {
            imgPathUrl = Paths.get(resource.getURI());
        } catch (IOException e) {
            // 处理异常
        }
        String Url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/avatar/" + fileName)
                .build()
                .toString();

        return ResultUtils.success(Url);
    }
}
