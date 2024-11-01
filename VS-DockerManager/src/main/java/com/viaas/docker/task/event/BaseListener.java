package com.viaas.docker.task.event;

public class BaseListener<T> implements Listener {
    private T t;

    @Override
    public void onListen(Event event) {
    }


    @Override
    public void addDriver(Driver driver) {
        driver.addListener(this);
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }


}
