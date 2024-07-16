package com.viaas.task;

/**
 * 触发器
 */
public interface Driver {

    public void Triger(Event event);

    public boolean addListener(Listener listener);

}
