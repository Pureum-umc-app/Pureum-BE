package com.umc.pureum.global.utils;

import com.umc.pureum.global.config.Response.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.umc.pureum.global.config.Response.BaseResponseStatus.SERVER_ERROR;

public class FileCheck {
    public static boolean checkImage(MultipartFile image) throws BaseException {
        String originalName = image.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
        Path savePath = Paths.get(fileName);
        try {
            System.out.println(Files.probeContentType(savePath).startsWith("image"));
            return Files.probeContentType(savePath).startsWith("image");
        } catch (IOException e) {
            throw new BaseException(SERVER_ERROR);
        }
    }
}
