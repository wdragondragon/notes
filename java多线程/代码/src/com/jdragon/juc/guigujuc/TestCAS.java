package com.jdragon.juc.guigujuc;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.14 15:52
 * @Description: test Compare And Swap
 */
public class TestCAS {
    public static void main(String[] args) {
        final CompareAndSwap cas = new CompareAndSwap();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                //取出预估内存值
                int expectedValue = cas.get();
                boolean b = cas.compareAndSet(expectedValue, (int) (Math.random() * 101));
                System.out.print(b+" ");
            }).start();
        }
    }
}

class CompareAndSwap{
    private int value;
    public synchronized int get(){
        return value;
    }
    public synchronized int compareAndSwap(int expectedValue,int newValue){
        //取出内存旧值
        int oldValue = value;
        //旧值与之前取出的预估值对比，相同才赋值
        if(oldValue == expectedValue){
            this.value = newValue;
        }
        return oldValue;
    }
    public synchronized boolean compareAndSet(int expectedValue,int newValue){
        return expectedValue == compareAndSwap(expectedValue,newValue);
    }
}
