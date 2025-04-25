package com.zhangjun_study.build.controller;
import org.apache.log4j.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//@RequestMapping("/api/pull")
//@RestController
public class Rule34XXXController {
    private static Logger logger  =  Logger.getLogger(Rule34XXXController. class );
    public static volatile CountDownLatch countDownLatch = null;      // 计数器


//    @GetMapping("/{findStr}/{threadNum}")
    // @PathVariable String findStr,@PathVariable Integer threadNum, HttpServletResponse res
    public String pullVideo(int threadNum,String findStr) throws InterruptedException, IOException {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        // 创建OkHttpClient实例
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                .connectTimeout(30, TimeUnit.MINUTES)
                .callTimeout(30,TimeUnit.MINUTES)
                .readTimeout(30,TimeUnit.MINUTES)
                .build();

        /**
         * 初始化本地目录
         */
        File file = new File("d:/rule34xxx/" + findStr);
        if (!file.exists()) file.mkdirs();
//        RestTemplate restTemplate = init();

        /**
         * 获取总页数
         */
        Integer page = 1;
        Integer downloadNumber = 0;
        while (true){
            URL url = new URL("https://rule34video.com/models/" + findStr + "/?mode=async&function=get_block&block_id=custom_list_videos_common_videos&sort_by=post_date&from=" + page + "&_=" + System.currentTimeMillis());
            Request request = new Request.Builder().url(url).get().build();
            String execute = client.newCall(request).execute().body().string();
            if (execute.equals("")){
                logger.info("已达到最终页");
                break;
            }
            page+=1;
        }
        logger.info("最终页为：" +page);
        countDownLatch = new CountDownLatch(page);

        for (Integer integer = 1; integer <= page; integer++) {
            logger.info("开始第"+integer+"页数据的解析。。。。。。");
            String url = "https://rule34video.com/models/"+findStr+"/?mode=async&function=get_block&block_id=custom_list_videos_common_videos&sort_by=post_date&from="+ integer + "&_="+System.currentTimeMillis();
            Elements item_thumbs = null;
            try {
                Request request = new Request.Builder().url(url).get().build();
                String execute = client.newCall(request).execute().body().string();
                Document doc = Jsoup.parseBodyFragment(execute);
                item_thumbs = doc.getElementById("custom_list_videos_common_videos_items").children();
            }catch (Exception e){
                logger.error("解析html错误，页面地址是：" + url);
                Request request = new Request.Builder().url(url).get().build();
                String execute = client.newCall(request).execute().body().string();
                Document doc = Jsoup.parseBodyFragment(execute);
                item_thumbs = doc.getElementById("custom_list_videos_common_videos_items").children();
            }
            int j = 0;
            for (Element item_thumb : item_thumbs) {
                Element a_link = null;
                try {
                    a_link = item_thumb.child(1);
                }catch (Exception e){
                    logger.info("当前是第" + integer+",页的第" + j + "条连接元素，该条元素非视频下载元素。。。。。。");
                    continue;
                }
                String href1 = a_link.attr("href");
                Request request2 = new Request.Builder().url(href1).get().build();
                String videoResponse=null;
                try {
                    videoResponse = client.newCall(request2).execute().body().string();
                }catch (Exception e ){
                    logger.info("执行下载地址错误，尝试重试："+ href1);
                }
                Element downloads_el = null;
                try {
                    downloads_el = Jsoup.parseBodyFragment(videoResponse).getElementsByClass("row row_spacer").get(1);

                }catch (Exception e ){
                    logger.error("错误地址："+ downloads_el);
                    continue;
                }

                Elements videoPix_els = downloads_el.getElementsByClass("tag_item");
                for (Element videoPix_el : videoPix_els) {
                    if (videoPix_el.text().contains("720p")) {
                        String href = videoPix_el.attr("href");
                        int start_index = href.indexOf("?");
                        int end_index = href.indexOf("&");
                        String fileName = href.substring(start_index + 19, end_index - 4);
                        logger.info("开始下载第"+integer+"页的第"+j+"个视频，地址是："+href);
//                        logger.error("文件：" + href + "下载失败.......");
                        executorService.submit(()->{{
                            boolean isDownload  = false;
                            try {
                                isDownload = fileDownload(client,file,fileName,href);
                                logger.info("文件：" + href + "下载成功.......");
                            }
                            catch (Exception e ){
                                logger.error("文件：" + href + "下载失败.......");
                            }
                        }

                        });
                        downloadNumber++;
                    }
                }
                j++;
            }
            countDownLatch.countDown();
            logger.info("第"+integer+"页数据的解析完毕。。。。。。");

        }
        countDownLatch.await();
        logger.info("--------------全部视频下载完成，下载总数为：" + downloadNumber);
        executorService.shutdown();
        return "ok";
    }


    boolean fileDownload(OkHttpClient okHttpClient,File baseRoot,String fileName,String videoUrl) throws IOException {
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




    public static void main(String[] args) throws InterruptedException, IOException {
        Rule34XXXController spliderController = new Rule34XXXController();
        spliderController.pullVideo(4,"andrey4k");

    }
}
