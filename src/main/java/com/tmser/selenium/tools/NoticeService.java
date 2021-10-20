package com.tmser.selenium.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@Service
public class NoticeService {
    private static final Logger logger = LoggerFactory.getLogger(NoticeService.class);

    @Value("${sys.dingdingNoticeUrl}")
    private String dingNoticeUrl;

    @Value("${sys.baseUrl}")
    private String baseUrl;

    public void setDingNoticeUrl(String dingNoticeUrl) {
        this.dingNoticeUrl = dingNoticeUrl;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param fileName
     *            成功后支付截图名称。
     * @return 所代表远程资源的响应结果
     */
    public String sendSuccessMsg(String fileName) {
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(dingNoticeUrl);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Edg/94.0.992.38");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write("{\n" +
                    "    \"msgtype\": \"actionCard\",\n" +
                    "    \"actionCard\": {\n" +
                    "        \"title\": \"抢购成功，支付通知\", \n" +
                    "        \"text\": \"![screenshot]("+baseUrl+fileName+") \\n #### TI结果通知： \\n 抢购成功，请扫码支付\",\n" +
                    "        \"singleTitle\" : \"点击查看大图\",\n" +
                    "        \"singleURL\" : \"#\"\n" +
                    "    }\n" +
                    "}");
            osw.flush();
            osw.close();
            os.close(); //don't forget to close the OutputStream
            conn.connect();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            logger.info("send message result: {}", result);
        } catch (Exception e) {
            logger.info("发送 POST 请求出现异常！", e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                  if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                logger.info("close failed", ex);
            }
        }
        return result;
    }

    /**
     * 库存通知
     *
     * @param msg
     *            成功后支付截图名称。
     * @return 所代表远程资源的响应结果
     */
    public String sendStorageMsg(String msg) {
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(dingNoticeUrl);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Edg/94.0.992.38");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write("{\"at\": {\"isAtAll\": true},\"text\":{\"content\":\"TI结果通知：发现库存商品\\n"+msg+"\"},\"msgtype\": \"text\"}");
            osw.flush();
            osw.close();
            os.close(); //don't forget to close the OutputStream
            conn.connect();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            logger.info("send storage result: {}", result);
        } catch (Exception e) {
            logger.info("发送 POST 请求出现异常！", e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                logger.info("close failed", ex);
            }
        }
        return result;
    }
}
