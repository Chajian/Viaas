package com.viaas.docker.task;

/**
 * 定时任务接口
 * @author Yanglin
 */
public interface DTask {

    /**
     * 同步-任务初始化
     * 主线程运行
     */
    public void start();


    /**
     * 异步-运行方法
     * 子线程运行
     */
    public void run();

    /**
     * 异步-回调函数
     * 子线程运行
     */
    public void recall();

    /**
     * 异步-任务死亡的任务
     * 子线程运行
     */
    public void death();

    /**
     * 获取任务状态
     * @return
     */
    public TaskStatus getStatus();

    public void setStatus(TaskStatus status);

    public long getId();


}
