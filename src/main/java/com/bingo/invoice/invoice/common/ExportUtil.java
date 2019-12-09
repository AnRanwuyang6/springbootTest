package com.bingo.invoice.invoice.common;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: lizk
 * @Date: 2019/5/31 16:21
 * @Description:导出excle
 */
public class ExportUtil {
    public static void exportExcel(HttpServletResponse response, String fileName, List<List<Object>> list, List<Integer[]>  mergeList) throws IOException {
        OutputStream output = export(response, fileName+System.currentTimeMillis(), "excel");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        sheet.setDefaultColumnWidth(15);
        HSSFRow row;
        HSSFCell cell;

        HSSFCellStyle style = wb.createCellStyle();
        HSSFCellStyle headStyle = wb.createCellStyle();
        HSSFFont headFont = wb.createFont();
        HSSFFont contentFont = wb.createFont();
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 14);
        headStyle.setFont(headFont);

        contentFont.setFontName("宋体");
        contentFont.setFontHeightInPoints((short) 10);
        style.setFont(contentFont);

        headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //合并
        if (mergeList != null && mergeList.size() > 0) {
            for (int i = 0; i < mergeList.size(); i++) {

                CellRangeAddress cra = new CellRangeAddress(mergeList.get(i)[0], mergeList.get(i)[1], mergeList.get(i)[2], mergeList.get(i)[3]);
                sheet.addMergedRegion(cra);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i);
            List<Object> column=list.get(i);
            //String[] column = list.get(i);
            for (int j = 0; j < column.size(); j++) {
                cell = row.createCell(j);
                if (i == 0) {
                    cell.setCellStyle(headStyle);
                } else {
                    cell.setCellStyle(style);
                }
                Object o=column.get(j);
                if(o!=null){
                    if(o instanceof BigDecimal){
                        cell.setCellValue(((BigDecimal) o).doubleValue());
                    } else{
                        cell.setCellValue(o.toString());
                    }
                }
            }
        }


        wb.write(output);
        output.flush();
        output.close();
    }


    public static OutputStream export(HttpServletResponse response, String fileName, String contentType) throws IOException {
        response.reset();
        if ("pdf".equals(contentType)) {
            response.setContentType("application/pdf;charset=UTF-8");
        } else {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso8859-1")+".xls");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        OutputStream output = response.getOutputStream();
        return output;
    }
}
