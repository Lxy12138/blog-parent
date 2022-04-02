package com.blog.controller;

import com.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Value("${upload.path}")
    private String baseFolderPath;

    /*
    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){
        //原始文件名称 比如 aa.png
        String originalFilename = file.getOriginalFilename();
        //唯一的文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        //上传文件 上传到哪呢？ 七牛云 云服务器 按量付费 速度快 把图片发放到离用户最近的服务器上
        // 降低 我们自身应用服务器的带宽消耗

        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }*/
    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile image, HttpServletRequest request){

        System.out.println("1");
        //原始文件名称 比如 aa.png
        String originalFilename = image.getOriginalFilename();
        //唯一的文件名称
        String imgName = UUID.randomUUID() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        //上传文件夹的准备
        File baseFolder = new File(baseFolderPath);
        if (!baseFolder.exists()) {
            baseFolder.mkdirs();
        }
        StringBuffer url = new StringBuffer();
        url.append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath())
                .append("/")
                .append(imgName);
        try {
            File dest = new File(baseFolder, imgName);
            image.transferTo(dest);
            return Result.success(url);

        } catch (IOException e) {
            return Result.fail(20001,"上传失败");
        }
    }
}
