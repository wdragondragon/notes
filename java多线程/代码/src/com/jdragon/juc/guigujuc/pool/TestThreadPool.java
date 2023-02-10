package com.jdragon.juc.guigujuc.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.20 14:25
 * @Description: 线程池
 * 一、线程池：提供一个线程队列，队列中保存着所有等待状态的线程。避免了创建与销毁额外开销，提供了响应速度
 *
 * 二、线程池的体系结构
 *  java.util.concurrent.Executor：负责线程的使用与调度的根接口
 *      |--ExecutorService 子接口：线程池的主要接口
 *          |--ThreadPoolExecutor 实现类
 *          |--ScheduledExecutorService 子接口：负责线程调度
 *              |--ScheduledThreadPoolExecutor
 *                  继承了ThreadPoolExecutor 实现ScheduledExecutorService：
 *                  具备线程池功能，也能做线程调度
 *
 * 在文档中 ThreadPoolExecutor描述：使用Executors中更方便的工厂方法来生成线程池
 * 三、工具类：Executors
 *   ExecutorService newCachedThreadPool():无限大小的线程池，可以根据需求自动的更改数量。
 *   ExecutorService newFixedThreadPool():固定大小的线程池
 *   ExecutorService newSingleThreadExecutor():单例线程池
 *   ScheduledExecutorService newScheduledThreadPool():创建固定大小的线程池，可以延迟或定时的执行任务。
 *
 */
public class TestThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.创建固定线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);
        ThreadPoolDemo threadPoolDemo = new ThreadPoolDemo();
        //2.为线程池中的线程分配任务
        for (int i = 0; i < 10; i++) {
            pool.submit(threadPoolDemo);
        }

        List<Future<Integer>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Future<Integer> future = pool.submit(() -> {
                int sum = 0;
                for (int j = 0; j <= 100; j++) {
                    sum += j;
                }
                return sum;
            });
            list.add(future);
        }
        for (Future<Integer> future : list) {
            System.out.println(future.get());
        }
        //3.关闭线程池
        // shutdown所有任务完成前不再接受新任务，完成后再关闭，shutdownNow强制关闭
        pool.shutdown();
    }
}

class ThreadPoolDemo implements Runnable {
    private int i = 0;
    @Override
    public void run() {
        while (i <= 100) {
            System.out.println(Thread.currentThread().getName() + ":" + i++);
        }
    }
}