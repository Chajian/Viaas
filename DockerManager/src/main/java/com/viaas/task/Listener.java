package com.viaas.task;

/**
 * 监听器
 */
public interface Listener {
    public void onListen(Event event);
//    添加监听的触发器
    public void addDriver(Driver driver);
}
