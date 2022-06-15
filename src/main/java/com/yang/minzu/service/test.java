//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.yang.minzu.service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class test {
    public test() {
    }

    public static void main(String[] args) {
//        InsrtPicToDB("./白族");
        InsrtPicToDB("D:\\test\\白族");
        InsrtPicToDB("D:\\test\\藏族");
        InsrtPicToDB("D:\\test\\傣族");
        InsrtPicToDB("D:\\test\\回族");
        InsrtPicToDB("D:\\test\\傈僳族");
        InsrtPicToDB("D:\\test\\蒙古族");
        InsrtPicToDB("D:\\test\\苗族");
        InsrtPicToDB("D:\\test\\土家族");
        InsrtPicToDB("D:\\test\\彝族");
        InsrtPicToDB("D:\\test\\壮族");
    }

    public static void InsrtPicToDB(String path) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "123456");
            String sql = "insert into minzu( minzuName, minzuUrl, minzuType, minzuSource, isDelete) values (?,?,?,?,?)";

            File file = new File(path);
            String[] filelist = file.list();
            String[] var6 = filelist;
            int i = filelist.length;

            for(int var8 = 0; var8 < i; ++var8) {
                String a = var6[var8];
                System.out.println(a);
            }

            pst = conn.prepareStatement(sql);
            int m = 0;

            for(i = 0; i < filelist.length; ++i) {

                int index = path.indexOf("\\");
                index = path.indexOf("\\", index + 1);


                String minzuType = path.substring(index + 1);

                pst.setString(1, filelist[i]);
                String minzuUrl = "http://124.221.66.151:9090/minzu" + "/" +minzuType + "/" + filelist[i];
                pst.setString(2, minzuUrl);


                pst.setString(3, minzuType);

                String suffix = filelist[i].substring(filelist[i].lastIndexOf(".") + 1);
                int minzuSource ;
                if (suffix.equals("qsv")||(suffix.equals("mp4"))) {
                    minzuSource = 2;
                } else if (suffix.equals("mp3")) {
                    minzuSource = 1;
                } else   {
                    minzuSource = 0;
                }

                pst.setInt(4, minzuSource);
//                String createTime = getFileCreateTime(path + "\\" + filelist[i]);
//                Date creatdateTime = strToDate(createTime);
//                pst.setDate(5, creatdateTime);
//                java.util.Date update = new java.util.Date();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//                dateFormat.format(update);
//                long date1 = update.getTime();
//                Date updateTime = new Date(date1);
//                pst.setDate(6, updateTime);
                int isDelete = 0;
                pst.setInt(5, isDelete);
                ++m;
                int n = pst.executeUpdate();
                System.out.println(n + "条记录已经插入");
            }

            System.out.println("本次一共导入" + m + "条");
        } catch (Exception var30) {
            var30.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }

                if (conn != null) {
                    conn.close();
                }

                System.out.println("数据库关闭");
            } catch (SQLException var29) {
                var29.printStackTrace();
            }

        }

    }

    public static Date strToDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        java.util.Date d = null;

        try {
            d = format.parse(str);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        Date date = new Date(d.getTime());
        return date;
    }

    public static String getFileCreateTime(String filePath) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        FileTime t = null;

        try {
            t = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class).creationTime();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        String createTime = dateFormat.format(t.toMillis());
        System.out.println("createTime = " + createTime);
        return createTime;
    }
}

