package com.jdragon.juc.guigujuc.pool;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.20 14:56
 * @Description: 线程调度
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
public class TestScheduled {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

        for (int i = 0; i < 6; i++) {
            Future<Integer> result = pool.schedule(() -> {
                int num = new Random().nextInt(100);
                System.out.println(Thread.currentThread().getName() + ":" + num);
                return num;
            },1, TimeUnit.SECONDS);
            System.out.println(result.get());
        }

        pool.shutdown();
    }
}
