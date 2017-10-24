package com.ssm.controller;

import com.ssm.model.User;
import com.ssm.service.UserService;
import com.ssm.utils.JxlCommonUtil;
import com.ssm.utils.PoiCommonUtil;
import common.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbin on 2017/9/23 0023.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private Logger log = Logger.getLogger(UserController.class);
    @Resource
    private UserService userService;

    @RequestMapping("/showUser")
    public String showUser(HttpServletRequest request, Model model) {
        log.info("查询所有用户信息");
        List<User> userList = userService.getAllUser();
        model.addAttribute("userList", userList);
        return "showUser";
    }

    //jxl导出excel
    @RequestMapping(value = "/jxlExportExcel")
    public void jxlExportExcel(HttpServletResponse response) {
        //导出的报表名称
        String fileNameTmp = "用户报表";
        //将获取的用户数据列表转为数组类型
        List<User> userList = userService.getAllUser();
        String[] titleArr = new String[3];
        titleArr[0] = "姓名";
        titleArr[1] = "手机号";
        titleArr[2] = "邮箱";
        List<String> dataList = new ArrayList<String>();
        if (userList != null && userList.size() > 0) {
            //转为数组类型
            for (User user : userList) {
                dataList.add(user.getUserName());
                dataList.add(user.getUserPhone());
                dataList.add(user.getUserEmail());
            }
        }
        log.info("开始导出用户报表!");
        JxlCommonUtil.exportReport(fileNameTmp, titleArr, dataList, response);
        log.info("结束导出用户报表!");

    }

    //poi导出excel
    @RequestMapping("/poiExportExcel")
    public void poiExportExcel(HttpServletResponse response) {
        //标题
        List<String> headers = new ArrayList<String>();
        headers.add("姓名");
        headers.add("手机号");
        headers.add("邮箱");

        List<User> userList = userService.getAllUser();
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        for (User user : userList) {
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("0", user.getUserName());
            dataMap.put("1", user.getUserPhone());
            dataMap.put("2", user.getUserEmail());
            dataList.add(dataMap);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", dataList);
        String fileName = "用户报表" + LocalDate.now().toString() + ".xls";
        String excelExportPath = "";
        try {
            excelExportPath = PoiCommonUtil.exportExcelFile(fileName, headers, map, userList.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!excelExportPath.isEmpty()) {
            try {
                File excelFile = new File(excelExportPath);
                PoiCommonUtil.downloadReport(excelFile, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //下载excel模板
    @RequestMapping(value = "/downExcelTemplate")
    public void downloadExcelTemplate(HttpServletResponse response) throws IOException {
        String excelTemplateDir = "c:\\excel";
        File excelFile = new File(excelTemplateDir, "模板.xlsx");
        PoiCommonUtil.downloadReport(excelFile, response);
    }

    //excel批量导入数据
    @RequestMapping("/mulImportExcelData")
    public void importDataByExcel(MultipartFile excelFile) {
        String excelName = "";
        String excelTmpDir = "c:\\excel";
        try {
            if (excelFile.isEmpty()) {
                throw new Exception("文件为空！");
            } else {
                String[] str = excelFile.getOriginalFilename().split("\\.");
                excelName = str[0] + System.currentTimeMillis() + "." + str[1];
                File wristbandDataExcel = new File(excelTmpDir, excelName);
                excelFile.transferTo(wristbandDataExcel);
                POIFSFileSystem poifsFileSystem = new POIFSFileSystem(wristbandDataExcel);
                HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);

                int sheetNum = workbook.getNumberOfSheets();

                for (int i = 0; i < sheetNum; i++) {
                    HSSFSheet sheet = workbook.getSheetAt(i);
                    int firstRow = sheet.getFirstRowNum();
                    int lastRow = sheet.getLastRowNum();
                    boolean isEmpty = false;

                    if (firstRow == lastRow) {
                        isEmpty = true;
                    }

                    if (!isEmpty) {
                        List<User> users = new ArrayList<User>();
                        for (int j = firstRow + 1; j <= lastRow; j++) {
                            HSSFRow row = sheet.getRow(j);
                            int firstCell = row.getFirstCellNum();
                            int lastCell = row.getLastCellNum();

                            if (firstCell != lastCell) {

                                User user = new User();
                                boolean flag = true;

                                for (int k = firstCell, x = 0; k < lastCell; k++, x++) {
                                    //获取一个单元格
                                    HSSFCell cell = row.getCell(k);
                                    if (k != 4) {
                                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                    }
                                    Object value = null;

                                    int cellType = cell.getCellType();

                                    switch (cellType) {
                                        case 0:
                                            value = cell.getNumericCellValue();
                                            break;
                                        case 1:
                                            value = cell.getStringCellValue();
                                            break;
                                        case 2:
                                            value = cell.getCellFormula();
                                            break;
                                        case 4:
                                            value = cell.getBooleanCellValue();
                                            break;
                                        default:
                                            value = null;
                                            break;
                                    }

                                    if (value != null) {
                                        if (!"".equals(value.toString().trim())) {
                                            switch (x) {
                                                case 0:
                                                    user.setUserName(value.toString().trim());
                                                    break;
                                                case 1:
                                                    user.setUserPhone(value.toString().trim());
                                                    break;
                                                case 2:
                                                    user.setUserEmail(value.toString().trim());
                                                    break;
                                                default:
                                                    break;
                                            }
                                        } else {
                                            flag = false;
                                        }
                                    } else {
                                        flag = false;
                                        break;
                                    }
                                }

                                if (flag) {
                                    users.add(user);
                                }
                            }
                        }
                        if (users.size() != 0) {
                            //向数据库插入数据
                            //Map<String, Object> retMap = deviceService.addWristbands(users);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
