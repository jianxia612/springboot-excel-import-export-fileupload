package org.spring.springboot.excel.poi.export.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * @Classname RequestGetFileInputStream
 * @Description 针对上传文件的相关操作 使用http request方式
 * @Date 2019/11/8 18:29
 * @Created by jianxiapc
 */
public class UploadFileCommonUtil {

    private static Logger logger = LoggerFactory.getLogger(UploadFileCommonUtil.class);


    /**
     * 通过request之中获得file的Parts 实现文件的 上传
     * @param request
     * @param response
     * @param uploadFileDirPath
     * @param frontEndFileInputName
     * @param renameFileName
     * @throws IOException
     * @throws ServletException
     */
    public static String uploadFileByPartsHttpServletRequest(HttpServletRequest request, HttpServletResponse response,
          String uploadFileDirPath, String frontEndFileInputName,String renameFileName) throws IOException, ServletException {
        logger.info("request.getContentType(): " + request.getContentType());
        if (!request.getContentType().split(";")[0].equals("multipart/form-data")){
            return null;
        }
        Collection<Part> parts = request.getParts();
        /**
         for(Part part:parts){
         System.out.println(part);
         String uploadFileDirPath="";
         }
         */
        String uploadFileWholeDirPath="";
        for (Iterator<Part> iterator = parts.iterator(); iterator.hasNext(); ) {
            Part part = iterator.next();
            logger.info("-----类型名称------->" + part.getName());
            logger.info("-----类型------->" + part.getContentType());
            logger.info("-----提交的类型名称------->" + part.getSubmittedFileName());
            logger.info("----流-------->" + part.getInputStream());
            uploadFileWholeDirPath= uploadFileProcess(part, uploadFileDirPath, frontEndFileInputName,renameFileName);
        }
        return uploadFileWholeDirPath;
    }

    /**
     * 通过MultipartHttpServletRequest 实现文件的上传
     * @param request
     * @param response
     * @param uploadFileDirPath
     * @param frontEndFileInputName
     * @param renameFileName
     * @return
     * @throws IOException
     */
    public static String uploadFileByMultipartHttpServletRequest(HttpServletRequest request, HttpServletResponse response,
           String uploadFileDirPath, String frontEndFileInputName,String renameFileName) throws IOException {
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        String uploadFileWholeDirPath="";
        // 判断是否是多数据段提交格式
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            logger.info("iter.hasNext(): " + iter.hasNext());
            Integer fileCount = 0;
            //文件流
            FileOutputStream fileOutputStream = null;
            while (iter.hasNext()) {
                MultipartFile  multipartFile = multiRequest.getFile(iter.next());
                String fileName = multipartFile.getOriginalFilename();
                logger.info("upload filename: " + fileName);
                if (fileName == null || fileName.trim().equals("")) {
                    continue;
                }
                //20170207 针对IE环境下filename是整个文件路径的情况而做以下处理
                Integer index = fileName.lastIndexOf("\\");
                String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
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
                    if(null!=renameFileName&&!"".equals(renameFileName)){
                        uploadFileWholeDirPath=uploadFileDirPath+renameFileName+ext;
                    }else{
                        uploadFileWholeDirPath=uploadFileDirPath+fileName;
                    }
                    File storageFile = new File(uploadFileWholeDirPath);
                    if(!storageFile.getParentFile().exists()){
                        storageFile.getParentFile().mkdirs();
                    }
                    //获取输出流
                    OutputStream os=new FileOutputStream(uploadFileWholeDirPath);
                    //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
                    InputStream is=multipartFile.getInputStream();
                    int temp;
                    //一个一个字节的读取并写入
                    while((temp=is.read())!=(-1))
                    {
                        os.write(temp);
                    }
                    os.flush();
                    os.close();
                    is.close();
             }
            }
        }
        return uploadFileWholeDirPath;
    }

    /**
     * 上传文件的处理函数
     * @param part
     * @param uploadFileDirPath
     * @param frontEndFileInputName
     * @param renameFileName 是否需要重新命名文件名称
     * @return
     * @throws IOException
     */
    private static String uploadFileProcess(Part part, String uploadFileDirPath, String frontEndFileInputName,String renameFileName) throws IOException {

        String uploadFileWholeDirPath="";
        if (part.getName().equals(frontEndFileInputName)) {
            String cd = part.getHeader("Content-Disposition");
            String[] cds = cd.split(";");
            String filename = cds[2].substring(cds[2].indexOf("=") + 1).substring(cds[2].lastIndexOf("//") + 1).replace("\"", "");
            String ext = filename.substring(filename.lastIndexOf(".") + 1);

            logger.info("filename:" + filename);
            logger.info("ext:" + ext);

            InputStream is = part.getInputStream();

            if (Arrays.binarySearch(ImageIO.getReaderFormatNames(), ext) >= 0)
                uploadImageProcess(uploadFileDirPath, filename, ext, is);
            else {
                uploadCommonFileProcess(uploadFileDirPath, filename, is);
            }
            if(null!=renameFileName&&!"".equals(renameFileName)){
                uploadFileWholeDirPath=uploadFileDirPath+renameFileName+ext;
            }else{
                uploadFileWholeDirPath=uploadFileDirPath+filename;
            }
        }
        return uploadFileWholeDirPath;
    }

    /**
     * 通用文件上传处理
     *
     * @param uploadFileDirPath
     * @param filename
     * @param is
     */
    private static void uploadCommonFileProcess(String uploadFileDirPath, String filename, InputStream is) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(uploadFileDirPath + filename));
            int b = 0;
            while ((b = is.read()) != -1) {
                fos.write(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 图片文件上传处理
     *
     * @param uploadFileDirPath
     * @param filename
     * @param ext
     * @param is
     * @throws IOException
     */
    public static void uploadImageProcess(String uploadFileDirPath, String filename, String ext, InputStream is) throws IOException {
        Iterator<ImageReader> irs = ImageIO.getImageReadersByFormatName(ext);
        ImageReader ir = irs.hasNext() ? irs.next() : null;
        if (ir == null)
            return;
        //必须转换为ImageInputStream，否则异常
        ir.setInput(ImageIO.createImageInputStream(is));

        ImageReadParam rp = ir.getDefaultReadParam();
        Rectangle rect = new Rectangle(0, 0, 200, 200);
        rp.setSourceRegion(rect);

        //allowSearch必须为true，否则有些图片格式imageNum为-1。
        int imageNum = ir.getNumImages(true);
        logger.info("imageNum:" + imageNum);
        for (int imageIndex = 0; imageIndex < imageNum; imageIndex++) {
            BufferedImage bi = ir.read(imageIndex, rp);
            ImageIO.write(bi, ext, new File(uploadFileDirPath + filename));
        }
    }
}
