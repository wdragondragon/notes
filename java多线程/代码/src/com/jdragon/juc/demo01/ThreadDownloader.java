package com.jdragon.juc.demo01;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.07 20:25
 * @Description: 多线程下载图片
 */
//Thread测试下载图片
public class ThreadDownloader extends Thread {

    private String url;
    private String name;

    public ThreadDownloader(String url, String name) {
        this.url = url;
        this.name = name;
    }

    @Override
    public void run() {
        WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloader(url, name);
        System.out.println("下载了文件名为：" + name);
    }

    public static void main(String[] args) {
        ThreadDownloader testThread1 = new ThreadDownloader("https://upload-images.jianshu.io/upload_images/14899865-02e80fcca1af7866.png?imageMogr2/auto-orient/strip|imageView2/2/w/558","1.jpg");
        ThreadDownloader testThread2 = new ThreadDownloader("https://upload-images.jianshu.io/upload_images/14899865-a265b14f3843827d.png?imageMogr2/auto-orient/strip|imageView2/2/w/611","2.jpg");
        ThreadDownloader testThread3 = new ThreadDownloader("https://upload-images.jianshu.io/upload_images/14899865-9fe9ac9ecb25b554.png?imageMogr2/auto-orient/strip|imageView2/2/w/853","3.jpg");

        testThread1.start();
        testThread2.start();
        testThread3.start();
    }
}
//runnable测试下载图片
class RunnableDownloader implements Runnable{
    private String url;
    private String name;

    public RunnableDownloader(String url, String name) {
        this.url = url;
        this.name = name;
    }
    @Override
    public void run() {
        WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloader(url, name);
        System.out.println("下载了文件名为：" + name);
    }

    public static void main(String[] args) {
        RunnableDownloader testRunnable1 = new RunnableDownloader("https://upload-images.jianshu.io/upload_images/14899865-02e80fcca1af7866.png?imageMogr2/auto-orient/strip|imageView2/2/w/558","1.jpg");
        RunnableDownloader testRunnable2 = new RunnableDownloader("https://upload-images.jianshu.io/upload_images/14899865-a265b14f3843827d.png?imageMogr2/auto-orient/strip|imageView2/2/w/611","2.jpg");
        RunnableDownloader testRunnable3 = new RunnableDownloader("https://upload-images.jianshu.io/upload_images/14899865-9fe9ac9ecb25b554.png?imageMogr2/auto-orient/strip|imageView2/2/w/853","3.jpg");

        new Thread(testRunnable1).start();
        new Thread(testRunnable2).start();
        new Thread(testRunnable3).start();
    }
}

class CallableDownloader implements Callable<Boolean>{
    private String url;
    private String name;

    public CallableDownloader(String url, String name) {
        this.url = url;
        this.name = name;
    }
    @Override
    public Boolean call() throws Exception {
        WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloader(url,name);
        System.out.println("下载了文件名为："+name);
        return true;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableDownloader testCallable1 = new CallableDownloader("https://upload-images.jianshu.io/upload_images/14899865-02e80fcca1af7866.png?imageMogr2/auto-orient/strip|imageView2/2/w/558","1.jpg");
        CallableDownloader testCallable2 = new CallableDownloader("https://upload-images.jianshu.io/upload_images/14899865-a265b14f3843827d.png?imageMogr2/auto-orient/strip|imageView2/2/w/611","2.jpg");
        CallableDownloader testCallable3 = new CallableDownloader("https://upload-images.jianshu.io/upload_images/14899865-9fe9ac9ecb25b554.png?imageMogr2/auto-orient/strip|imageView2/2/w/853","3.jpg");
        //创建执行服务：
        ExecutorService ser = Executors.newFixedThreadPool(3);
        //提交执行
        Future<Boolean> r1 = ser.submit(testCallable1);
        Future<Boolean> r2 = ser.submit(testCallable2);
        Future<Boolean> r3 = ser.submit(testCallable3);
        //获取结果
        boolean rs1 = r1.get();
        boolean rs2 = r2.get();
        boolean rs3 = r3.get();

        ser.shutdownNow();
    }
}


class WebDownloader {
    public void downloader(String url, String name) {
        try {
            FileUtils.copyURLToFile(new URL(url), new File(name));
        } catch (IOException e) {
            System.out.println("IO异常,downloader方法出现问题");
            e.printStackTrace();
        }
    }
}
