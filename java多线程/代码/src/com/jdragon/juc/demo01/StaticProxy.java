package com.jdragon.juc.demo01;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.07 22:35
 * @Description: 静态代理
 */
public class StaticProxy {
    public static void main(String[] args) {

        new Thread(()->System.out.println("我爱你")).start();

        new WeddingCompany(new You()).happyMarry();
    }
}

interface Marry{
    void happyMarry();
}
class You implements Marry{

    @Override
    public void happyMarry() {
        System.out.println("秦老师要结婚了，超开心");
    }
}
class WeddingCompany implements Marry{

    private Marry target;

    public WeddingCompany(Marry target){
        this.target = target;
    }

    @Override
    public void happyMarry() {
        before();
        this.target.happyMarry();
        after();
    }
    private void after(){
        System.out.println("结婚之后，收尾款");
    }
    private void before(){
        System.out.println("结婚之前，布置现场");
    }
}