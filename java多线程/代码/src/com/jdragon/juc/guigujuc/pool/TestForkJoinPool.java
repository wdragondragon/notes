package com.jdragon.juc.guigujuc.pool;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.20 15:07
 * @Description: 分支/合并框架，工作窃取
 */
public class TestForkJoinPool {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> task =  new ForkJoinSumCalculate(0L,10000000000L);

        Instant start = Instant.now();

        Long sum = pool.invoke(task);

        Instant end = Instant.now();

        System.out.println(sum);

        System.out.println("耗时："+ Duration.between(start,end).toMillis());
    }
    @Test
    public void test1(){
        long sum = 0L;

        Instant start = Instant.now();

        for (long i = 0; i <= 10000000000L; i++) {
            sum+=i;
        }

        Instant end = Instant.now();

        System.out.println(sum);

        System.out.println("耗时："+ Duration.between(start,end).toMillis());
    }
    @Test
    public void test2(){
        Instant start = Instant.now();

        Long sum = LongStream.rangeClosed(0L,10000000000L)
                .parallel()
                .reduce(0L,Long::sum);

        Instant end = Instant.now();

        System.out.println(sum);

        System.out.println("耗时："+ Duration.between(start,end).toMillis());
    }
}
class ForkJoinSumCalculate extends RecursiveTask<Long> {

    private static final long serialVersionUID = -3837386534160506908L;

    private long start;

    private long end;

    private static final long THURSHOLD = 10000L;//临界值

    public ForkJoinSumCalculate(long start,long end){
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long length = end - start;
        if(length <= THURSHOLD){
            long sum = 0L;
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        }else{
            long mid = (start+end)/2;
            ForkJoinSumCalculate left = new ForkJoinSumCalculate(start,mid);
            ForkJoinSumCalculate right = new ForkJoinSumCalculate(mid+1,end);
            left.fork();//进行拆分，同时压入线程队列
            right.fork();
            //结果合并
            return left.join()+right.join();
        }
    }
}
