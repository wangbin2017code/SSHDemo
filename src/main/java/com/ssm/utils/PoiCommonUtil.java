package com.ssm.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * 常用的poi工具类
 * Created by wangbin on 2017/10/4 0004.
 */
public class PoiCommonUtil {

    public static String exportExcelFile(String exportFilename, List<String> headers, Map<String, Object> map,
                                         int rowCount)
            throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        Sheet sheet = workbook.createSheet("报表");
        sheet.setDefaultColumnWidth(17);

        List<Map<String, String>> dataList = (List<Map<String, String>>) map.get("list");

        Row header = sheet.createRow(0);

        for (int i = 0; i < headers.size(); i++) {
            header.createCell(i).setCellValue(headers.get(i));
        }

        for (int j = 1; j < rowCount + 1; j++) {
            Row row = sheet.createRow(j);
            for (Integer k = 0; k < headers.size(); k++) {
                row.createCell(k.intValue()).setCellValue(dataList.get(j - 1).get(k.toString()));
            }
        }

        /**
         * 设置下载时客户端Excel的名称
         */
        String fileName = exportFilename;

        fileName = new String(fileName.getBytes("GB2312"), "gbk");
        System.out.println("****************" + fileName);
        String localAddress = "C:\\excel";
        Files.createDirectories(Paths.get(localAddress));
        String fullFilePath = localAddress.concat("\\\\").concat(fileName);

        try {
            System.out.println("****************" + fullFilePath);
            File excelExport = new File(fullFilePath);
            OutputStream os = new FileOutputStream(excelExport);
            workbook.write(os);
            os.flush();
            os.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return fullFilePath;
    }

    public static void downloadReport(File excelFile, HttpServletResponse response) throws IOException {
        if (excelFile.exists()) {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(excelFile.getName()
                    .getBytes("gb2312"), "ISO8859-1"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(excelFile);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
