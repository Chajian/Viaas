package com.viaas.docker.task;

import com.viaas.docker.entity.Order;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public abstract class BaseTask<T> implements DTask,Comparable<BaseTask>{
    int time;//定时器单位秒
    TaskStatus status;//任务状态
    private long id = 0;
    private static long coun = 0;
    protected T t;
    private ReentrantLock reentrantLock = new ReentrantLock();
    public BaseTask(int time) {
        reentrantLock.lock();
        try{
            id += coun++;
            this.time = time;
            status=TaskStatus.INIT;
            start();
            showStatus();
        }finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public  void run() {
        reentrantLock.lock();
        try {
            status=TaskStatus.RUNNING;
            showStatus();
            if(time>0){
                time--;//减少活动时间
            }
            else{
                recall();//回调
                death();//死亡
            }
        }finally {
            reentrantLock.unlock();
        }

    }

    @Override
    public void recall() {
        reentrantLock.lock();
        try {
            status = TaskStatus.WAITING;
            showStatus();
        }finally {
            reentrantLock.unlock();
        }


    }

    @Override
    public  void death() {
        status = TaskStatus.DEATH;
        showStatus();
   }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    /**
     * 优先队列优先级比较
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(@NotNull BaseTask o) {
        return 0;
    }

    public void showStatus(){
        if(t!=null&&t instanceof Order)
            log.info("Task"+t!=null?t.toString():""+":"+getStatus().toString());
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    public int getTime() {
        return time;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
