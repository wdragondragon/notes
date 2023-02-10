package com.jdragon.juc.demo01;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.08 19:45
 * @Description: 测试stop线程
 * 建议线程正常停止
 * 建议使用标志位
 * 不要使用stop或者destory等过时或者JDK不建议使用的方法
 */
public class TestStop implements Runnable {
    // 1设置一个标志位
    private boolean flag = true;
    @Override
    public void run() {
        int i = 0;
        while (flag) {
            if (i > 900) {
                stop();
                System.out.println("线程停止");
            } else {
                System.out.println("run....Thread" + (i++));
            }
        }
    }
    // 2.设置一个公开的方法停止线成，转换标志位
    public void stop() {
        this.flag = false;
    }
    public static void main(String[] args) {
        TestStop testStop = new TestStop();
        new Thread(testStop).start();
    }
}
