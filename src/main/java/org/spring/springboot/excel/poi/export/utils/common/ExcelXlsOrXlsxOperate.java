package org.spring.springboot.excel.poi.export.utils.common;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * @Classname CreateExcelFile
 * @Description 生产Excel文件 包括(.xls 和 .xlsx)
 * 此类主要两种不同类型Excel文件的传统操作方式
 * @Date 2019/11/7 11:54
 * @Created by jianxiapc
 */
public class ExcelXlsOrXlsxOperate {

    /**
     * 判断文件的sheet是否存在.
     * @param excelSuffixType 文件(.xls 和 .xlsx)
     * @param fileDir   文件路径
     * @param sheetName  表格索引名
     * @return boolean
     */
    public static boolean excelSheetIsExist(String excelSuffixType,String fileDir, String sheetName){

        boolean flag = false;
        File file = new File(fileDir);
        Workbook workbook=null;
        if (file.exists()) {
            //文件存在，创建workbook
            try {
                if(".xls".equals(excelSuffixType)){
                    workbook = new HSSFWorkbook(new FileInputStream(file));
                }else if(".xlsx".equals(excelSuffixType)){
                    workbook = new XSSFWorkbook(new FileInputStream(file));
                }
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet!=null) {
                    //文件存在，sheet存在
                    flag = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //文件不存在
            flag = false;
        }
        return flag;
    }

    /**
     * 创建新excel(xls).
     * @param excelSuffixType 文件(.xls 和 .xlsx)
     * @param fileDir excel的路径
     * @param sheetNames 要创建的表格索引列表
     * @param titleRow  excel的第一行即表格头
     */
    public static void createExcelFileBySuffix(String excelSuffixType,String fileDir, List<String> sheetNames, String titleRow[]){

        Workbook workbook=null;
        //创建workbook
        if(".xls".equals(excelSuffixType)){
            workbook = new HSSFWorkbook();
        }else if(".xlsx".equals(excelSuffixType)){
            workbook = new XSSFWorkbook();
        }
        //新建文件
        FileOutputStream fileOutputStream = null;
        Row row = null;
        try {

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);

            //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
            for(int i = 0; i<sheetNames.size(); i++){
                workbook.createSheet(sheetNames.get(i));
                workbook.getSheet(sheetNames.get(i)).createRow(0);
                //添加表头, 创建第一行
                row = workbook.getSheet(sheetNames.get(i)).createRow(0);
                row.setHeight((short)(20*20));
                for (short j = 0; j < titleRow.length; j++) {

                    Cell cell = row.createCell(j, CellType.BLANK);
                    cell.setCellValue(titleRow[j]);
                    cell.setCellStyle(cellStyle);
                }
                fileOutputStream = new FileOutputStream(fileDir);
                workbook.write(fileOutputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 往excel(xls)中写入(已存在的数据无法写入).     *
     * @param excelSuffixType 文件(.xls 和 .xlsx)
     * @param fileDir    文件路径
     * @param sheetName  表格索引
     * @param mapList
     * @throws Exception
     */

    public static void writeToExcelFileBySuffix(String excelSuffixType,String fileDir, String sheetName, List<Map<String,String>> mapList) throws Exception{

        //创建workbook
        File file = new File(fileDir);
        Workbook workbook=null;

        try {
            //创建workbook
            if(".xls".equals(excelSuffixType)){
                workbook = new HSSFWorkbook(new FileInputStream(file));
            }else if(".xlsx".equals(excelSuffixType)){
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        //文件流
        FileOutputStream fileOutputStream = null;
        Sheet sheet = workbook.getSheet(sheetName);
        // 获取表格的总行数
        // int rowCount = sheet.getLastRowNum() + 1; // 需要加一
        //获取表头的列数
        int columnCount = sheet.getRow(0).getLastCellNum();

        try {
            // 获得表头行对象
            Row titleRow = sheet.getRow(0);
            //创建单元格显示样式
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);


            if(titleRow!=null){
                for(int rowId = 0; rowId < mapList.size(); rowId++){
                    Map<String,String> map = mapList.get(rowId);
                    Row newRow=sheet.createRow(rowId+1);
                    newRow.setHeight((short)(20*20));//设置行高  基数为20

                    for (short columnIndex = 0; columnIndex < columnCount; columnIndex++) {  //遍历表头
                        //trim()的方法是删除字符串中首尾的空格
                        String mapKey = titleRow.getCell(columnIndex).toString().trim();
                        Cell cell = newRow.createCell(columnIndex);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(map.get(mapKey)==null ? null : map.get(mapKey).toString());
                    }
                }
            }

            fileOutputStream = new FileOutputStream(fileDir);
            workbook.write(fileOutputStream);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        //String excelSuffix=".xls";
        //String fileDir = "d:\\workbook.xls";

        String excelSuffix=".xlsx";
        String fileDir = "d:\\workbook.xlsx";
        List<String> sheetName = new ArrayList<>();

        sheetName.add("A");
        sheetName.add("B");
        sheetName.add("C");

        System.out.println(sheetName);

        String[] title = {"id","name","password"};
        createExcelFileBySuffix(excelSuffix,fileDir, sheetName, title);

        List<Map<String,String>> userList1 = new ArrayList<Map<String,String>>();
        Map<String,String> map=new HashMap<String,String>();
        map.put("id", "111");
        map.put("name", "张三");
        map.put("password", "111！@#");

        Map<String,String> map2=new HashMap<String,String>();
        map2.put("id", "222");
        map2.put("name", "李四");
        map2.put("password", "222！@#");

        Map<String,String> map3=new HashMap<String,String>();
        map3.put("id", "33");
        map3.put("name", "王五");
        map3.put("password", "333！@#");
        userList1.add(map);
        userList1.add(map2);
        userList1.add(map3);

        Map<String, List<Map<String, String>>> users = new HashMap<>();

        users.put("A", userList1);

        List<Map<String,String>> userList2 = new ArrayList<Map<String,String>>();
        Map<String,String> map4=new HashMap<String,String>();
        map4.put("id", "111");
        map4.put("name", "张三");
        map4.put("password", "111！@#");

        Map<String,String> map5=new HashMap<String,String>();
        map5.put("id", "222");
        map5.put("name", "李四");
        map5.put("password", "222！@#");

        Map<String,String> map6=new HashMap<String,String>();
        map6.put("id", "33");
        map6.put("name", "王五");
        map6.put("password", "333！@#");
        userList2.add(map4);
        userList2.add(map5);
        userList2.add(map6);

        users.put("B", userList2);

        List<Map<String,String>> userList3 = new ArrayList<Map<String,String>>();

        users.put("C", userList3);
        System.out.println(sheetName.size());
        //删除List 集合中特定的元素
        for(Iterator<String> sheeNameIterator = sheetName.iterator();sheeNameIterator.hasNext();){

            String sheet = sheeNameIterator.next();
            //此时删除了第三张sheet
            if ( users.get(sheet).size() == 0) {

                sheeNameIterator.remove();

            }
        }

        System.out.println(sheetName.size());
        createExcelFileBySuffix(excelSuffix,fileDir, sheetName, title);
        for (int j = 0; j < sheetName.size(); j++) {
            try {
                writeToExcelFileBySuffix(excelSuffix,fileDir, sheetName.get(j), users.get(sheetName.get(j)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}