package com.haodong.util;

import com.haodong.service.IExcutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 模拟高并发请求
 */
@Component
public class HighConcurrencyExcutor {

    /**
     *
     * @param excutor
     */
    public void run(IExcutor excutor, int threadTotal, int clientTotal){
        ExecutorService executorService = Executors.newFixedThreadPool(1500);

        //信号量，此处用于控制并发的线程数
        final Semaphore semaphore = new Semaphore(threadTotal);
        //闭锁，可实现计数器递减
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal ; i++) {
            executorService.execute(() -> {
                try {
                    //执行此方法用于获取执行许可，当总计未释放的许可数不超过200时，
                    //允许通行，否则线程阻塞等待，直到获取到许可。
                    semaphore.acquire();
                    excutor.excutor();
                    //释放许可
                    semaphore.release();
                } catch (Exception e) {
                    //log.error("exception", e);
                    e.printStackTrace();
                }
                //闭锁减一
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();//线程阻塞，直到闭锁值为0时，阻塞才释放，继续往下执行
        }catch (Exception e){
            e.printStackTrace();
        }

        executorService.shutdown();
    }
}
