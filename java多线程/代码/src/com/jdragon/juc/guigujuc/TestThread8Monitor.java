package com.jdragon.juc.guigujuc;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.20 13:31
 * @Description:
 *  1. 两个普通同步方法，两个线程，标准打印，打印 //one two
 *  2. 新赠Thread.sleep()给getOne(),       打印//one two
 *  3. 新增普通方法getThree(),                打印//three one two
 *  4. 两个普通的同步方法，两个Number对象，两个线程，打印//two one
 *  5. 修改getOne为静态同步方法，                 打印 //two one
 *  6. 修改两个方法均为静态同步方法                   打印//one two
 *  7. 一个静态同步方法，一个非静态同步方法，两个Number对象 //打印two one
 *  8. 两个静态同步方法，两个Number对象                  //one two
 */
public class TestThread8Monitor {
    public static void main(String[] args) {
        Number number = new Number();

//        Number number2 = new Number();

        new Thread(Number::getOne).start();

        new Thread(Number::getTwo).start();

//        new Thread(number::getThree).start();
    }
}
class Number{
    public static synchronized void getOne(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    public static synchronized void getTwo(){
        System.out.println("two");
    }

    public void getThree(){
        System.out.println("three");
    }
}