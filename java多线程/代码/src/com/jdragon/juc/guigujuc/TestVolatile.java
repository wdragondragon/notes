package com.jdragon.juc.guigujuc;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.10 14:44
 * @Description: volatile测试
 */
public class TestVolatile {
    public static void main(String[] args) {
        ThreadDemo td = new ThreadDemo();
        new Thread(td,"线程1").start();
        while (true){
            if(td.isFlag()){
                System.out.println("-------------------");
                break;
            }
        }
    }
}
class ThreadDemo implements Runnable{
    private volatile boolean flag = false;
    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}
        flag = true;
        System.out.println("flag="+ isFlag());
    }
    public boolean isFlag() {
        return flag;
    }
}
