package org.spring.springboot.excel.poi.export.utils.common;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Classname ImportExcelUtils
 * @Description 导入Excel相关数据，主要完成读取Excel文件之中的行列；
 * 转换为List形式可以用于往数据库之中插入数据
 * @Date 2019/11/8 10:39
 * @Created by jianxiapc
 */
public class ExcelImportReaderUtils {

    private static Logger logger = LoggerFactory.getLogger(ExcelImportReaderUtils.class);

    /**
     * 通过读取Excel的文件路径 读取 .xls or .xlsx 都行
     * @param excelFileDirPath
     * @param titleRow
     * @return
     */
    public static List<Map<String,Object>> readExcelFileContentToList(String excelFileDirPath,String titleRow[]) throws IOException, InvalidFormatException {
        //通过工厂模式创建Excel  Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(new File(excelFileDirPath));
        logger.info(" Excel Workbook 包含有 " + workbook.getNumberOfSheets() + " 张 Sheets : ");

        List<Map<String,Object>>  readExcelFileRowsContentList=new ArrayList<Map<String,Object>>();
         /*
           =============================================================
           迭代在workbook之中的所有的sheets (多种方式)
           =============================================================
        */
        //1、 获得一个sheetIterator 和 iterate 覆盖它
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        logger.info("使用 Iterator 返回 Sheets ");
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            logger.info("=> " + sheet.getSheetName());
        }
        /**
        //2、 使用 for-each loop 返回 workbook之中的 sheet
        logger.info("使用 for-each loop 返回 Sheets ");
        for(Sheet sheet: workbook) {
            logger.info("=> " + sheet.getSheetName());
        }

        // 3、使用 Java 8 forEach wih lambda
        logger.info("使用 Java 8 forEach with lambda 返回 Sheets ");
        workbook.forEach(sheet -> {
            logger.info("=> " + sheet.getSheetName());
        });
         */
         /*
           ==================================================================
            遍历工作表(Sheet)中的所有行(rows)和列(columns) (Multiple ways)
           ==================================================================
        */
        // 获得 索引为0 的 Sheet
        Sheet sheet = workbook.getSheetAt(0);

        // 创建一个数据格式器 目的是为 格式化 获得每cell的值为字符串
        DataFormatter dataFormatter = new DataFormatter();

        // 1、 你能够 obtain a rowIterator and columnIterator and iterate over them
        logger.info(" 使用 Iterator Iterating over Rows and Columns ");
        Iterator<Row> rowIterator = sheet.rowIterator();
        boolean isFirstRow=true;//是否是第一行
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Map<String,Object> rowMap= new HashMap<String,Object>();
            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();
            int titleIndex=0;
            if(!isFirstRow){
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue = dataFormatter.formatCellValue(cell);
                    rowMap.put(titleRow[titleIndex],cellValue);
                    logger.info(titleRow[titleIndex]+" ===>"+cellValue);
                    titleIndex++;
                }
                logger.info("row content: "+rowMap.toString());
                readExcelFileRowsContentList.add(rowMap);
            }
            isFirstRow=false;
        }

        /**
        // 2. Or you can use a for-each loop to iterate over the rows and columns
        logger.info(" Iterating over Rows and Columns using for-each loop ");
        for (Row row: sheet) {
            for(Cell cell: row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                logger.info(cellValue + "\t");
            }
            logger.info("");
        }

        // 3. Or you can use Java 8 forEach loop with lambda
        logger.info(" Iterating over Rows and Columns using Java 8 forEach with lambda ");
        sheet.forEach(row -> {
            row.forEach(cell -> {
                printCellValue(cell);
            });
            logger.info("");
        });
        */
        // Closing the workbook
        workbook.close();
        return readExcelFileRowsContentList;
    }

    public static List<Map<String,Object>> readExcelFileByRequestInputStreamContentToList(InputStream excelInputStream, String titleRow[]) throws IOException, InvalidFormatException, ServletException {
        //直接使用Request之中Excel文件流获得Excel文件信息
        Workbook workbook = WorkbookFactory.create(excelInputStream);
        logger.info(" Excel Workbook 包含有 " + workbook.getNumberOfSheets() + " 张 Sheets : ");
        List<Map<String,Object>>  readExcelFileRowsContentList=new ArrayList<Map<String,Object>>();
         /*
           =============================================================
           迭代在workbook之中的所有的sheets (多种方式)
           =============================================================
        */
        //1、 获得一个sheetIterator 和 iterate 覆盖它
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        logger.info("使用 Iterator 返回 Sheets ");
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            logger.info("=> " + sheet.getSheetName());
        }
         /*
           ==================================================================
            遍历工作表(Sheet)中的所有行(rows)和列(columns) (Multiple ways)
           ==================================================================
        */
        // 获得 索引为0 的 Sheet
        Sheet sheet = workbook.getSheetAt(0);

        // 创建一个数据格式器 目的是为 格式化 获得每cell的值为字符串
        DataFormatter dataFormatter = new DataFormatter();

        // 1、 你能够 obtain a rowIterator and columnIterator and iterate over them
        logger.info(" 使用 Iterator Iterating over Rows and Columns ");
        Iterator<Row> rowIterator = sheet.rowIterator();
        boolean isFirstRow=true;//是否是第一行
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Map<String,Object> rowMap= new HashMap<String,Object>();
            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();
            int titleIndex=0;
            if(!isFirstRow){
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue = dataFormatter.formatCellValue(cell);
                    rowMap.put(titleRow[titleIndex],cellValue);
                    logger.info(titleRow[titleIndex]+" ===>"+cellValue);
                    titleIndex++;
                }
                logger.info("row content: "+rowMap.toString());
                readExcelFileRowsContentList.add(rowMap);
            }
            isFirstRow=false;
        }
        // Closing the workbook
        workbook.close();
        return readExcelFileRowsContentList;
    }

        private static void printCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                logger.info(cell.getBooleanCellValue()+"");
                break;
            case STRING:
                logger.info(cell.getRichStringCellValue().getString());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    logger.info(cell.getDateCellValue().toString());
                } else {
                    logger.info(cell.getNumericCellValue()+"");
                }
                break;
            case FORMULA:
                logger.info(cell.getCellFormula());
                break;
            case BLANK:
                logger.info("");
                break;
            default:
                logger.info("");
        }

        logger.info("\t");
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        String excelFilePath="d:\\workbook.xls";
        String[] titleRows={"id","name","password"};
        List<Map<String,Object>>  readExcelFileRowsContentList=readExcelFileContentToList(excelFilePath,titleRows);

        logger.info(readExcelFileRowsContentList.toString());
    }
}
