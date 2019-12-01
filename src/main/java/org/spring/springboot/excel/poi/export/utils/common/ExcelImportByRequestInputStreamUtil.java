package org.spring.springboot.excel.poi.export.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * @Classname ExcelDirectRequestInputStreamUtil
 * @Description 直接从Request之中获得导入的Excel文件流内容
 * @Date 2019/11/9 11:46
 * @Created by jianxiapc
 */
public class ExcelImportByRequestInputStreamUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelImportByRequestInputStreamUtil.class);

    /**
     * 获得上传文件未单个Excel文件时候 通过MultipartFile参数获得文件流信息
     * @param request
     * @param response
     * @param file
     * @return
     * @throws IOException
     */
    public static InputStream getInputStreamByMultipartFileParameter(HttpServletRequest request, HttpServletResponse response,MultipartFile file) throws IOException {
        InputStream  excelInputStream = file.getInputStream();
        int length=excelInputStream.available();
        logger.info("length： "+length);
        logger.info("excelInputStream： "+excelInputStream.toString());
        return excelInputStream;
    }

    /**
     * 通过request获得InputStream获得文件内容
     * @param request
     * @param response
     * @param frontEndFileInputName
     * @return
     * @throws IOException
     */
    public static InputStream getInputStreamByRequestAndFileInputName(HttpServletRequest request, HttpServletResponse response,String frontEndFileInputName) throws IOException {

        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        MultipartFile fileContent  = ((MultipartHttpServletRequest) request).getFiles(frontEndFileInputName).get(0);
        // fis 既是上传的文件流
        InputStream  excelInputStream = fileContent.getInputStream();

        int length=excelInputStream.available();
        logger.info("length： "+length);
        logger.info("excelInputStream： "+excelInputStream.toString());

        return excelInputStream;
    }


    /**
     * 通过request之中获得file的Parts
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public static InputStream getInputStreamByRequestParts(HttpServletRequest request, HttpServletResponse response,String frontEndFileInputName) throws IOException, ServletException {
        logger.info("request.getContentType(): " + request.getContentType());
        if (!request.getContentType().split(";")[0].equals("multipart/form-data"))
            return null;
        Collection<Part> parts = request.getParts();
        InputStream excelInputStream =null;
        //通过获得part信息组装为InputStream流用读取文件内容
        int i=0;
        for (Iterator<Part> iterator = parts.iterator(); iterator.hasNext(); ) {
            Part part = iterator.next();
            logger.info("-----类型名称------->" + part.getName());
            logger.info("-----类型------->" + part.getContentType());
            logger.info("-----提交的类型名称------->" + part.getSubmittedFileName());
            logger.info("----流-------->" + part.getInputStream());

            logger.info("循环次数=== "+i);
            excelInputStream=part.getInputStream();
            i++;
        }
        return excelInputStream;
    }

    /**
     * 使用 commons-fileupload 上传Excel文件返回文件流信息
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public static InputStream getInputStreamByMultipartHttpServletRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream excelInputStream =null;
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // 判断是否是多数据段提交格式
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            logger.info("iter.hasNext(): " + iter.hasNext());
            Integer fileCount = 0;
            while (iter.hasNext()) {
                MultipartFile multipartFile = multiRequest.getFile(iter.next());
                String fileName = multipartFile.getOriginalFilename();
                logger.info("upload filename: " + fileName);
                if (fileName == null || fileName.trim().equals("")) {
                    continue;
                }
                //20170207 针对IE环境下filename是整个文件路径的情况而做以下处理
                Integer index = fileName.lastIndexOf("\\");
                String newStr = "";
                if (index > -1) {
                    newStr = fileName.substring(index + 1);
                } else {
                    newStr = fileName;
                }
                if (!newStr.equals("")) {
                    fileName = newStr;
                }
                logger.info("new filename: " + fileName);

                if (multipartFile != null) {
                    excelInputStream= multipartFile.getInputStream();
                    long fileSize= multipartFile.getSize();
                    logger.info("fileSize: " + fileSize);
                }
        }

    }
        return excelInputStream;
    }
}
