package com.viaas.docker.task;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 定时任务线程池
 * 采用时间片轮转
 * @author Yanglin
 */
@Slf4j
public class TaskThread implements Runnable {
    /*任务队列*/
    private List<DTask> list;
    private boolean live;
    List<String> logs;
    /*线程池最大容量*/
    int max = 20;

    public TaskThread() {
        init();
    }

    public TaskThread(int max) {
        this.max = max;
        init();
    }

    public void init(){
        live = true;
        list = new ArrayList<>();
        logs = new ArrayList<>();
    }

    @Override
    public void run() {
        //TODO 加锁提升线程安全
            synchronized(this) {
                Iterator<DTask> iterator = list.iterator();
                while (iterator.hasNext()) {
                    DTask task = iterator.next();
                    //iterator.remove();会导致异常报错，原因在多线程情况下，list没有上锁就进行删除操作
                    if (task.getStatus() == TaskStatus.DEATH) {
                        iterator.remove();
                    } else {
                        try {
                            task.run();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                String info = "当前线程名:" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId();
                log.info(info);
            }
    }

    public synchronized boolean add(DTask task){
        if(list.size() < max) {
            list.add(task);
            return true;
        }
        return false;
    }

    public boolean isLive() {
        return live;
    }


    /**
     * 通过id获取任务
     * @param id
     * @return
     */
    public synchronized DTask getDTaskById(long id){
        for(DTask t : list){
            if(t.getId()==id)
                return t;
        }
        return null;
    }

    public synchronized float getDensity(){
        return list.size()/max;
    }
}
