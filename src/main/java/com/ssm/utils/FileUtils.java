package com.ssm.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.*;

/**
 * 文件操作相关工具类.
 * Created by wangbin on 2017/10/25.
 */
public class FileUtils {

    /**
     * 上传和压缩图片文件.
     *
     * @param imgPath 图片文件路径
     */
    public static void handleImgFile(String imgPath) throws IOException {
        //获取文件的后缀名
        if (imgPath == null || "".equals(imgPath.trim())) {
            return;
        }
        //创建文件对象
        File file = new File(imgPath);
        String fileName = imgPath.substring(imgPath.lastIndexOf("//") + 2, imgPath.length());
        String stuffName = fileName.split("\\.")[1];
        //判断大小
        if (file.length() < 1024 * 1024) {
            //小于1M
            //直接上传不压缩
            Thumbnails.of(imgPath)
                    .scale(1f)
                    .outputQuality(0.1f)
                    .toFile("e://img" + System.currentTimeMillis() + "." + stuffName);
        } else {
            if ("jpeg".equalsIgnoreCase(stuffName) || "png".equalsIgnoreCase(stuffName)) {
                fileName = fileName.split("\\.")[0] + ".jpg";
            }
            //创建临时文件夹
            File tmpFile = new File("e://img//temp");
            if (!tmpFile.exists()) {
                tmpFile.mkdir();
            }
            //上传
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("e://img//temp//" + fileName));
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = bis.read(b, 0, b.length)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            bos.close();
            bis.close();

            //压缩
            Thumbnails.of("e://img//temp//" + fileName)
                    .scale(1f)
                    .outputQuality(0.1f)
                    .toFile("e://img//" + System.currentTimeMillis() + "." + stuffName);
        }
    }
}
