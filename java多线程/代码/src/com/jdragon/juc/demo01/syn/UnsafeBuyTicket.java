package com.jdragon.juc.demo01.syn;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.08 21:36
 * @Description: 不安全买票
 */
public class UnsafeBuyTicket {
    public static void main(String[] args) {
        BuyTicket station = new BuyTicket();

        new Thread(station,"牛逼的你们").start();
        new Thread(station,"可恶的黄牛党").start();
        new Thread(station,"苦逼的我").start();

    }
}

class BuyTicket implements Runnable{

    private int ticketNums = 10;

    boolean flag = true;

    @Override
    public void run() {
        while(flag){
            try {
                Thread.sleep(100);
                buy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void buy() {
        //判断是否有票
        if(ticketNums<=0){
            flag = false;
            return;
        }

        //买票
        System.out.println(Thread.currentThread().getName()+"拿到"+ticketNums--);
    }
}
