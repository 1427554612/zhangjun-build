package com.zhangjun_study.build.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class XvideosController {

    private static Logger logger  =  Logger.getLogger(XvideosController. class );
    public static volatile CountDownLatch countDownLatch = null;      // 计数器

    /**
     * 拉取视频
     * @param threadNum
     * @param findStr
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public String pullVideo(int threadNum,String findStr) throws InterruptedException, IOException {
        String token = "session_token=bfaf1573ace188d2WLha6F4Ddmn-9YSW3RzC1vXeRd7Nqq5H9iwSD8eE_G5FnkQcxE7Ubfv0UTwbVNBCGmaCm1a7D8Y_Yl2CjdLTTr09Yq6O0d6gtcc2H4aeuPxfccmq4hoN0jBvnIqbFCd_o0Z6aUwdScI-OI-g90WPCUh7wm0OHCrpXpNXPYDkFbE4oiZqP9Mi33h9cfvg4akmBpzANQ8tzdDweLO2Y0W-sWCMMYkp0JOwFzZ-2hDjltQCM3ICw0n4yRE4Y8aE0EYj8YEmdqkcv1gfChha4wLMLOGtsaw8ajFjaW1T7IWqbv8v8hHM0HLSA7OS_yLs62w8AGG5nMdya9Y-cVagZWzrX4rpLIWTz0TE1JliESUk_ae87RF0bgoygY29TOKwBpP-j_pUzNIMb-cHPMsJgN7JBoQF2XlxdLL3PNUCH5_SGiW1lErQw5BfmpSmZ79ZzqBXfRZ-Y7EDmpapgjdI9ZJCHdH_yCT2-WaEQHBGJhAteTZzbQVA8gihcJXAUjcfOnB8sPcd0Vfrk6835IXbe3hzkw%3D%3D; expires=Fri, 29 May 2026 08:49:39 GMT; Max-Age=34560000; path=/; domain=.xvideos.com; secure; SameSite=None";
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        /**
         * 初始化本地目录
         */
        File file = new File("d:/xvideos/" + findStr);
        if (!file.exists()) file.mkdirs();
        // https://gcore-vid.xvideos-cdn.com/wDMM8Hkhgz01DqUrhUW0Eg==,1745493277/videos/hls/90/a6/7b/90a67bf677757309f98d91e71a3cd9d2/hls-1080p-28192.m3u8

        // 创建OkHttpClient实例
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                .connectTimeout(30, TimeUnit.MINUTES)
                .callTimeout(30, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .build();
        // 第一页地址
        String baseUrl = "https://www.xvideos.com";
        String url = "https://www.xvideos.com/?k="+findStr+"&p=";
        Request request = new Request.Builder().url(url).header("set-cookie",token).get().build();
        ResponseBody body = client.newCall(request).execute().body();
        Element element = Jsoup.parse(body.string()).getElementsByClass("mozaique cust-nb-cols").get(0);
        Elements thumb = element.getElementsByClass("thumb");
        for (Element element1 : thumb) {
            Element a = element1.getElementsByTag("a").get(0);
            System.out.println("a标签：" + a);
            if (!a.toString().contains("video.")) {
                continue;
            }
            String videoUrl = baseUrl+a.attr("href");
            System.out.println("视频页地址：" + videoUrl);
            Request context = new Request.Builder().url(videoUrl).header("set-cookie",token).get().build();
            ResponseBody contextBody = client.newCall(context).execute().body();
            System.out.println(contextBody.string());


        }
        // html5player.setVideoHLS
        return null;
    }

    /**
     * 下载接口
     * @param okHttpClient
     * @param baseRoot
     * @param fileName
     * @param videoUrl
     * @return
     * @throws IOException
     */
    boolean fileDownload(OkHttpClient okHttpClient, File baseRoot, String fileName, String videoUrl) throws IOException {
        Request request = new Request.Builder().url(videoUrl).get().build();
        InputStream inputStream = okHttpClient.newCall(request).execute().body().byteStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(baseRoot+"\\"+fileName+".mp4");
        while ((len = inputStream.read(bytes)) != -1){
            fileOutputStream.write(bytes,0,len);
        }
        fileOutputStream.close();
        inputStream.close();
        return true;

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new XvideosController().pullVideo(1,"VeronikavonK");
    }
}
