package com.viaas.docker.task;

import com.viaas.docker.task.event.Event;

/**
 * 异步-事件驱动
 *
 */
public class EventTask extends BaseTask<Event>{
    public EventTask(int time) {
        super(time);
    }

    @Override
    public void start() {

    }

    /**
     * 异步start方法
     * 只在init状态时执行
     */
    public void asyncStart(){

    }

    @Override
    public void run() {
        if(getStatus()==TaskStatus.INIT){
            asyncStart();
        }
        super.run();
    }
}
