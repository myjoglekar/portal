/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.visumbu.vb.bean.ColumnDef;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsDataSet {

    final static Logger log = Logger.getLogger(XlsDataSet.class);

    public static Map XlsDataSet(String filename, String sheetName) throws FileNotFoundException, IOException {
        log.debug("Start function of XlsDataSet(String filename, String sheetName) in XlsDataSet class");

        Map returnMap = new HashMap();

        try {
            //FileOutputStream fos = new FileOutputStream(outputFile);

            // Get the workbook object for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filename));
            // Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheet(sheetName);
            Cell cell;
            Row row;
            // Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            List<ColumnDef> columnDefs = new ArrayList<>();
            List<Map<String, String>> data = new ArrayList<>();
            int rowCount = 0;
            List<String> header = new ArrayList<>();
            for (Iterator<Row> iterator = rowIterator; iterator.hasNext();) {
                row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellCount = 0;
                for (Iterator<Cell> iterator1 = cellIterator; iterator1.hasNext();) {
                    Cell currentCell = iterator1.next();
                    if (rowCount == 0) {
                        ColumnDef columnDef = new ColumnDef(currentCell.toString(), "string", currentCell.toString());
                        columnDefs.add(columnDef);
                        header.add(currentCell.toString());
                    } else {
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put(header.get(cellCount), currentCell.toString());
                        data.add(dataMap);
                    }
                    cellCount++;
                }
                rowCount++;
            }
            returnMap.put("columnDefs", columnDefs);
            returnMap.put("data", data);

            log.debug(returnMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("End function of XlsDataSet(String filename, String sheetName) in XlsDataSet class");
        return returnMap;
    }

    public static Map XlsDataSet(String filename, Integer sheetNo) throws FileNotFoundException, IOException {
        log.debug("Start function of XlsDataSet(String filename, Integer sheetNo) in XlsDataSet class");
        Map returnMap = new HashMap();

        try {
            //FileOutputStream fos = new FileOutputStream(outputFile);

            // Get the workbook object for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filename));
            // Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(sheetNo);
            Cell cell;
            Row row;
            // Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            List<ColumnDef> columnDefs = new ArrayList<>();
            List<Map<String, String>> data = new ArrayList<>();
            int rowCount = 0;
            List<String> header = new ArrayList<>();
            for (Iterator<Row> iterator = rowIterator; iterator.hasNext();) {
                row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellCount = 0;
                for (Iterator<Cell> iterator1 = cellIterator; iterator1.hasNext();) {
                    Cell currentCell = iterator1.next();
                    if (rowCount == 0) {
                        ColumnDef columnDef = new ColumnDef(currentCell.toString(), "string", currentCell.toString());
                        columnDefs.add(columnDef);
                        header.add(currentCell.toString());
                    } else {
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put(header.get(cellCount), currentCell.toString());
                        data.add(dataMap);
                    }
                    cellCount++;
                }
                rowCount++;
            }
            returnMap.put("columnDefs", columnDefs);
            returnMap.put("data", data);

            log.debug(returnMap);
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException in XlsDataSet function: " + e);
        } catch (IOException e) {
            log.error("IOException in XlsDataSet function: " + e);
        }
        log.debug("End function of XlsDataSet(String filename, Integer sheetNo) in XlsDataSet class");
        return returnMap;
    }

    public static Map XlsxDataSet(String filename, Integer sheetNo) throws FileNotFoundException, IOException {
        log.debug("Start function of XlsxDataSet(String filename, Integer sheetNo) in XlsDataSet class");
        Map returnMap = new HashMap();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filename));
            // Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(sheetNo);
            Cell cell;
            Row row;
            // Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            List<ColumnDef> columnDefs = new ArrayList<>();
            List<Map<String, String>> data = new ArrayList<>();
            int rowCount = 0;
            List<String> header = new ArrayList<>();
            for (Iterator<Row> iterator = rowIterator; iterator.hasNext();) {
                row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellCount = 0;
                for (Iterator<Cell> iterator1 = cellIterator; iterator1.hasNext();) {
                    Cell currentCell = iterator1.next();
                    if (rowCount == 0) {
                        ColumnDef columnDef = new ColumnDef(currentCell.toString(), "string", currentCell.toString());
                        columnDefs.add(columnDef);
                        header.add(currentCell.toString());
                    } else {
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put(header.get(cellCount), currentCell.toString());
                        data.add(dataMap);
                    }
                    cellCount++;
                }
                rowCount++;
            }
            returnMap.put("columnDefs", columnDefs);
            returnMap.put("data", data);
            log.debug(returnMap);
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException in XlsxDataSet function: " + e);
        } catch (IOException e) {
            log.error("IOException in XlsxDataSet function: " + e);
        }
        log.debug("End function of XlsxDataSet(String filename, Integer sheetNo) in XlsDataSet class");
        return returnMap;
    }

    public static void main(String[] argv) {
        log.debug("Start main function in XlsDataSet");
        try {
            XlsxDataSet("/tmp/test.xlsx", 0);
        } catch (IOException ex) {
            log.error("IOException in main function: " + ex);
        }
        log.debug("End main function in XlsDataSet");
    }
}
