package com.jdragon.juc.demo01;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.07 22:07
 * @Description: 多个线程同时操作同一个对象
 * 买火车票的例子
 */
public class TestThread3 implements Runnable{

    private int ticketNums = 10;

    @Override
    public void run() {
        while (ticketNums > 0) {
            System.out.println(Thread.currentThread().getName() + "拿到了第" + ticketNums-- + "张票");
        }
    }

    public static void main(String[] args) {
        TestThread3 ticket = new TestThread3();

        new Thread(ticket,"小明").start();
        new Thread(ticket,"老师").start();
        new Thread(ticket,"黄牛党").start();
    }
}
