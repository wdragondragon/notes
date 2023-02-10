package com.jdragon.juc.demo01.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.10 11:44
 * @Description:
 */
public class TestLock {
    public static void main(String[] args) {
        TestLock2 testLock2 = new TestLock2();

        new Thread(testLock2,"1").start();
        new Thread(testLock2,"2").start();
        new Thread(testLock2,"3").start();
    }
}

class TestLock2  implements Runnable{

    int ticketNums = 10;

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
                lock.lock();
                if(ticketNums>0){
                    System.out.println(Thread.currentThread().getName()+":"+ticketNums--);
                }else{
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
}
