package com.jdragon.juc.demo01;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.08 20:00
 * @Description: 模拟网络延时
 */
public class TestSleep implements Runnable{
    private int ticketNums = 10;
    @Override
    public void run() {
        while (ticketNums>0) {
            System.out.println(Thread.currentThread().getName() + "拿到了第" + ticketNums-- + "张票");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        TestSleep ticket = new TestSleep();

        new Thread(ticket,"小明").start();
        new Thread(ticket,"老师").start();
        new Thread(ticket,"黄牛党").start();
    }
}
