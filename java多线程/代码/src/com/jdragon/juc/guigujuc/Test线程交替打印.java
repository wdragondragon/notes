package com.jdragon.juc.guigujuc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.20 12:34
 * @Description: 编写一个程序，开启3个线成，这三个线程的ID分别为A、B、C，每个线程将自己的ID在屏幕上
 * 打印10遍，要求输出的结果必须按顺序显示。
 * 如：ABCABCABC……
 */
public class Test线程交替打印 {
    public static void main(String[] args) {
        AlternateDemo ad = new AlternateDemo();
        new Thread(() -> {
            for (int i = 1; i <=20; i++) {
                ad.loopA(i);
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 1; i <=20; i++) {
                ad.loopB(i);
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 1; i <=20; i++) {
                ad.loopC(i);
            }
        }, "C").start();
    }
}

class AlternateDemo {

    private int number = 1;//当前正在执行的线程的标记

    private Lock lock = new ReentrantLock();

    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void loopA(int totalLoop) {
        lock.lock();
        try {
            //1.判断
            if (number != 1) {
                conditionA.await();
            }
            //2.否则就等于打印
            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName()
                        + "\t" + i + "\t" + totalLoop);
            }
            //3.唤醒
            number = 2;
            conditionB.signal();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopB(int totalLoop) {
        lock.lock();
        try {
            //1.判断
            if (number != 2) {
                conditionB.await();
            }
            //2.否则就等于打印
            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName()
                        + "\t" + i + "\t" + totalLoop);
            }
            //3.唤醒
            number = 3;
            conditionC.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopC(int totalLoop) {
        lock.lock();
        try {
            //1.判断
            if (number != 3) {
                conditionC.await();
            }
            //2.否则就等于打印
            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName()
                        + "\t" + i + "\t" + totalLoop);
            }
            System.out.println("------------------------------");
            //3.唤醒
            number = 1;
            conditionA.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
