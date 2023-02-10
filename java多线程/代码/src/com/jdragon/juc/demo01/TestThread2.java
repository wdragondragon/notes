package com.jdragon.juc.demo01;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.07 20:50
 * @Description: 创建线程方式二：实现runnable接口，重写run方法，执行线程需要丢入实现runnable接口接口实现类，调用start方法
 */
public class TestThread2 implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 2000; i++) {
            System.out.println("我在看代码---"+i);
        }
    }
    public static void main(String[] args) {
        //创建一个线程对象
        TestThread2 testThread2 = new TestThread2();
        //调用start()方法开启线程
        new Thread(testThread2).start();

        for (int i = 0; i < 2000; i++) {
            System.out.println("我在学习多线程--"+i);
        }
    }
}
