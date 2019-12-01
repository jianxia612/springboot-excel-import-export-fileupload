package org.spring.springboot.excel.poi.export.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.spring.springboot.excel.poi.export.utils.annotation.FileStorageProperties;
import org.spring.springboot.excel.poi.export.utils.common.ExcelImportByRequestInputStreamUtil;
import org.spring.springboot.excel.poi.export.utils.common.ExcelImportReaderUtils;
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
 * @Classname ExcelImportInputStreamController
 * @Description 直接使用Request获得文件流进行导入Excel内容
 * @Date 2019/11/9 11:41
 * @Created by jianxiapc
 */
@RestController
public class ExcelImportInputStreamController {

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @RequestMapping(value = "/importDataToExcelByRequestInputStream", method = RequestMethod.POST)
    public String importDataToExcelByRequestInputStream(HttpServletRequest request, HttpServletResponse response,@RequestParam("importExelFile") MultipartFile file) throws Exception {
        String uploadFileDirPath =fileStorageProperties.getUploadDir();
        String[] titleRows={"id","name","password"};
        /**
            InputStream excelInputStream =ExcelDirectRequestInputStreamUtil.getInputStreamByRequestParts(request,response,"importExelFile");
         */
        //InputStream excelInputStream =ExcelDirectRequestInputStreamUtil.getInputStreamByRequestAndFileInputName(request,response,"importExelFile");
        InputStream excelInputStream = ExcelImportByRequestInputStreamUtil.getInputStreamByMultipartHttpServletRequest(request,response);
        List<Map<String,Object>> readExcelFileRowsContentList= ExcelImportReaderUtils.readExcelFileByRequestInputStreamContentToList(excelInputStream,titleRows);

        JSONArray jsonArray = (JSONArray) JSONObject.toJSON(readExcelFileRowsContentList);
        return jsonArray.toString();
    }
}
