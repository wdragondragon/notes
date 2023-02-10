package com.jdragon.juc.demo01;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.08 20:34
 * @Description:
 */
public class TestState {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("/////");
            }
        });
        //观察新创建
        Thread.State state = thread.getState();
        System.out.println(state);
        //观察启动后
        thread.start();
        System.out.println(state = thread.getState());
        //观察直到结束
        while(state!=Thread.State.TERMINATED){
            Thread.sleep(100);
            System.out.println(state = thread.getState());
        }
    }
}
