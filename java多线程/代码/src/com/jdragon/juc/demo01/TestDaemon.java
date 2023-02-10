package com.jdragon.juc.demo01;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.08 21:20
 * @Description: 测试守护线程
 */
public class TestDaemon {
    public static void main(String[] args) {
        God god = new God();
        Your your = new Your();

        Thread thread = new Thread(god);
        thread.setDaemon(true);
        thread.start();


        new Thread(your).start();
    }
}

class God implements Runnable{

    @Override
    public void run() {
        while(true){
            System.out.println("上帝保佑着你");
        }
    }
}

class Your implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 36500; i++) {

        }
        System.out.println("-=====goodbye！world！=====-");
    }
}
