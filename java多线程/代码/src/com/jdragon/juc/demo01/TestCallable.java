package com.jdragon.juc.demo01;

import java.util.concurrent.Callable;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.07 22:23
 * @Description: 线程创建方式三：实现callable接口
 */
public class TestCallable implements Callable<Boolean> {
    @Override
    public Boolean call() throws Exception {



        return true;
    }
}
