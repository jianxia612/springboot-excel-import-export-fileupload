package org.spring.springboot.excel.poi.export.controller;

import org.spring.springboot.excel.poi.export.model.ExcelModelData;
import org.spring.springboot.excel.poi.export.utils.common.ExcelWriterExportUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname ExcelController
 * @Description 对外提供接口的Excel导出功能
 * @Date 2019/11/7 15:30
 * @Created by jianxiapc
 */
@RestController
public class ExcelController {

    /**
     * 实现导出了
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportDataToExcel", method = RequestMethod.GET)
    public void exportDataToExcel(HttpServletResponse response) throws Exception {
        ExcelModelData data = new ExcelModelData();
        data.setName("hello");
        List<String> titles = new ArrayList();
        titles.add("a1");
        titles.add("a2");
        titles.add("a3");

        data.setHeadTitles(titles);
        List<List<Object>> rows = new ArrayList();
        List<Object> row = new ArrayList();
        row.add("11111111111");
        row.add("22222222222");
        row.add("3333333333");
        rows.add(row);

        data.setRows(rows);


        //生成本地
        /*File f = new File("c:/test.xlsx");
        FileOutputStream out = new FileOutputStream(f);
        ExportExcelUtils.exportExcel(data, out);
        out.close();*/
        ExcelWriterExportUtils.exportExcel(response,"hello.xlsx",data);
    }

    @RequestMapping(value = "/importDataToExcel", method = RequestMethod.GET)
    public void importDataToExcel(HttpServletResponse response) throws Exception {

    }
}
