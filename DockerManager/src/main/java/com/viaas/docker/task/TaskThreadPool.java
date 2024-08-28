package com.viaas.docker.task;


import com.viaas.docker.entity.Order;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;

/**
 * 定时器任务线程池
 * 单例模式
 * @author Yanglin
 */
@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
public class TaskThreadPool {

    ScheduledExecutorService scheduledExecutorService;
    ThreadPoolExecutor executor;
    /*
     * orderId,TaskId
     * get Task by OrderId
     */
    Map<Integer,Long> orderToTask = new HashMap<>();
    List<TaskThread> cache;
    /*最大线程任务容量*/
    private int maxTasks;
    int corePoolSize = Runtime.getRuntime().availableProcessors(); // cpu理想的任务边界
    int maximumPoolSize = corePoolSize * 2; // 调整任务需求
    long keepAliveTime = Long.MAX_VALUE; // 任务存活时间
    int queueCapacity = 100; // 队列长度

    @PostConstruct
    public void init(){
        executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                new ThreadPoolExecutor.CallerRunsPolicy()
        ){
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                super.beforeExecute(t, r);
                log.info("Thread:"+t.toString()+"Runnable:"+r.toString());
            }
        };
        scheduledExecutorService = Executors.newScheduledThreadPool(corePoolSize);
        cache = new ArrayList<>();
    }

    public synchronized void addTaskThread(TaskThread taskThread){
        cache.add(taskThread);
    }

    public synchronized void addThread(Runnable runnable){
        executor.submit(runnable);
    }

    @Scheduled(fixedRate = 1000)
    public void allCacheRun(){
        synchronized (cache){
            Iterator<TaskThread> iterator = cache.iterator();
            while(iterator.hasNext()){
                TaskThread taskThread = iterator.next();
                taskThread.run();
            }
        }
    }


    /**
     * 添加订单任务
     *
     * @param task 任务
     */
    public synchronized void addOrderTask(DTask task, Order order) {
        Iterator<TaskThread> iterator = cache.iterator();
        while(iterator.hasNext()){
            TaskThread taskThread = iterator.next();
            if(taskThread.isLive())
                if(taskThread.add(task)) {
                    orderToTask.put(order.getId(), task.getId());
                    return;
                }
        }
        TaskThread taskThread = new TaskThread();
        taskThread.add(task);
        orderToTask.put(order.getId(), task.getId());
        addTaskThread(taskThread);
    }

    public synchronized void addTask(DTask task){
        Iterator<TaskThread> iterator = cache.iterator();
        while(iterator.hasNext()){
            TaskThread taskThread = iterator.next();
            if(taskThread.isLive())
                if(taskThread.add(task)) {
                    return;
                }
        }
        TaskThread taskThread = new TaskThread();
        taskThread.add(task);
        addTaskThread(taskThread);
    }


    /**
     * 获取定时任务通过orderId
     * @param orderId
     * @return
     */
    public synchronized DTask getTaskFromOrder(int orderId){
        long taskId = orderToTask.get(orderId);
        Iterator<TaskThread> iterator = cache.iterator();

        while(iterator.hasNext()){
            TaskThread taskThread = iterator.next();
            DTask task = taskThread.getDTaskById(taskId);
            if(task!=null)
                return task;
        }
        return null;
    }
    public synchronized void awak(){
        if(executor.getTaskCount()>0)
            executor.notifyAll();
    }
}
