package com.viaas.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 触发器基类
 */
public class BaseDriver implements Driver {
    private List<Listener> obs = new ArrayList<>();

    /**
     * 触发事件
     * @param event
     */
    @Override
    public void Triger(Event event) {
        for(Listener listening:obs)
            listening.onListen(event);
    }

    @Override
    public boolean addListener(Listener listener) {
        obs.add(listener);
        return true;
    }
}
