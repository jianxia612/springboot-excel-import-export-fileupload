package org.spring.springboot.excel.poi.export.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.spring.springboot.excel.poi.export.utils.annotation.FileStorageProperties;
import org.spring.springboot.excel.poi.export.utils.common.ExcelImportByRequestInputStreamUtil;
import org.spring.springboot.excel.poi.export.utils.common.ExcelImportReaderUtils;
import org.spring.springboot.excel.poi.export.utils.common.UploadFileCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @Classname UploadFileController
 * @Description 基本的文件上传实现测试
 * @Date 2019/11/9 11:42
 * @Created by jianxiapc
 */
@RestController
public class UploadFileController {

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("importExelFile") MultipartFile file) throws Exception {
        String uploadFileDirPath =fileStorageProperties.getUploadDir();

        //String storageFilePath=UploadFileCommonUtil.uploadFileByPartsHttpServletRequest(request,response,uploadFileDirPath,"importExelFile",null);
        String storageFilePath=UploadFileCommonUtil.uploadFileByMultipartHttpServletRequest(request,response,uploadFileDirPath,"importExelFile",null);

        return storageFilePath;
    }
}
