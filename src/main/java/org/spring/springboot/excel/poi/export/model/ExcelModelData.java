package org.spring.springboot.excel.poi.export.model;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname ExcelModelData
 * @Description Excel的基本数据模型
 * @Date 2019/11/7 14:37
 * @Created by jianxiapc
 */
public class ExcelModelData implements Serializable {

    private static final long serialVersionUID = 4444017239100620999L;

    // 表头
    private List<String> headTitles;

    // 数据
    private List<List<Object>> rows;

    // 页签名称
    private String name;

    public List<String> getHeadTitles() {
        return headTitles;
    }

    public void setHeadTitles(List<String> headTitles) {
        this.headTitles = headTitles;
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    public void setRows(List<List<Object>> rows) {
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
