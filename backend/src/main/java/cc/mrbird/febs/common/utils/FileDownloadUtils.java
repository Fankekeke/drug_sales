package cc.mrbird.febs.common.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author fank
 * @date 2021/6/9 17:00
 */
public class FileDownloadUtils {

    public static void downloadTemplate(HttpServletResponse response, String fileName) throws Exception {
         InputStream in = Files.newInputStream(Paths.get("G:/Project/药品销售平台/drug_sales/backend/src/main/resources/template/" + fileName));
//        Resource res = new ClassPathResource("template/" + fileName);
//        InputStream in = res.getInputStream();

        try (// 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(in);
            BufferedOutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            response.reset();
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            int len;
            while ((len = fis.read()) != -1) {
                toClient.write(len);
                toClient.flush();
            }
        } catch (IOException ex) {
            throw new Exception("下载文件失败！");
        }
    }
    public static void downloadPdf(HttpServletResponse response, String fileName) throws Exception {
        InputStream in = new FileInputStream(fileName);
        try (// 以流的形式下载文件。
             BufferedInputStream fis = new BufferedInputStream(in);
             BufferedOutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            response.reset();
            response.setContentType("application/json");
//            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "iso8859-1"));
            int len;
            while ((len = fis.read()) != -1) {
                toClient.write(len);
                toClient.flush();
            }
        } catch (IOException ex) {
            throw new Exception("下载文件失败！");
        }
    }
}
