package com.ssm.utils;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 常用jxl的工具类
 * Created by wangbin on 2017/10/3 0003.
 */
public class JxlCommonUtil {

    /**
     * 数据导出到excel
     *
     * @param fileNameTmp 文件名
     * @param titleArr    表头字段名称
     * @param dataList    数据
     * @param response    HttpServletResponse
     */
    public static void exportReport(String fileNameTmp, String[] titleArr, List<String> dataList,
                                    HttpServletResponse response) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            // 用时间(yyyyMMddHHmmssSSS)命名导出数据名称
            String dataFormat = sdf.format(new Date());
            String fileName = fileNameTmp + "_" + dataFormat + ".xls";

            OutputStream os = response.getOutputStream();

            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 创建Excel文档 工作簿
            WritableWorkbook book = Workbook.createWorkbook(os);

            // 设置单元格的文字格式
            WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            // 创建标题单元格格式
            WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
            // 设置居中
            titleFormat.setAlignment(Alignment.CENTRE);

            // 设置单元格的文字格式
            WritableFont cellfont = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            // 创建普通单元格格式
            WritableCellFormat cellFormat = new WritableCellFormat(cellfont);
            // 设置居中
            cellFormat.setAlignment(Alignment.CENTRE);

            // 导出数据
            writeExcel(fileNameTmp, 0, book, titleFormat, cellFormat, titleArr, dataList);

            // 写入数据
            book.write();
            // 关闭文件
            book.close();
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入excel.
     *
     * @param titleName   标题
     * @param sheetIndex  第几页
     * @param book        Excel文档 工作簿
     * @param titleFormat 标题单元格格式
     * @param cellFormat  普通单元格格式
     * @param titleArr    表头
     * @param dataList    数据
     */
    private static void writeExcel(String titleName, int sheetIndex, WritableWorkbook book, WritableCellFormat titleFormat,
                                   WritableCellFormat cellFormat, String[] titleArr, List<String> dataList) {
        // 操作执行
        try {
            // 生成名为“第一页”的工作表，参数0表示这是第一页 创建工作表
            WritableSheet sheet = book.createSheet(titleName, sheetIndex);

            // 写入表头
            for (int i = 0; i < titleArr.length; i++) {
                // Label 的参数分别是单元格 列，单元格行，单元格内容，单元格格式
                sheet.addCell(new Label(i, 0, titleArr[i], titleFormat));
            }

            // list 迭代器
            Iterator<String> iter = dataList.iterator();
            int recCount = dataList.size() / titleArr.length;
            // 循环写入内容
            for (int i = 0; i < recCount; i++) {
                for (int j = 0; j < titleArr.length; j++) {
                    if (iter.hasNext()) {
                        // 向单元格中写内容
                        sheet.addCell(new Label(j, i + 1, (String) iter.next(), cellFormat));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
