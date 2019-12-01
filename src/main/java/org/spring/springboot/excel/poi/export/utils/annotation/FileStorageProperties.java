package org.spring.springboot.excel.poi.export.utils.annotation;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @Classname FileStorageProperties
 * @Description 获得上传文件存储的路径
 * @Date 2019/11/9 11:37
 * @Created by jianxiapc
 */

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}