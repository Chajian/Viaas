package com.viaas.docker.entity.dto;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
 
/**
 * 简单的{@code float}原子操作类，实现了原子增减、原子替换操作。由于浮点运算本身具有精度问题，故本类并不能保证精确的浮点运算结果
 */
public class AtomicFloat extends Number {
    // volatile非常重要
    private volatile int intBits;
 
    private static final AtomicIntegerFieldUpdater<AtomicFloat> fieldUpdater;
    static {
        fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(AtomicFloat.class, "intBits");
    }
 
    /**
     * 创建一个初始值为{@code 0}的AtomicFloat
     */
    public AtomicFloat () {
        intBits = Float.floatToIntBits(0f); // 这里是为了便于理解，其实可以直接intBits = 0，因为Float.floatToIntBits(0f) = 0
    }
 
    /**
     * 创建一个指定初始值的AtomicFloat
     * @param initialValue 初始值
     */
    public AtomicFloat(float initialValue) {
        intBits = Float.floatToIntBits(initialValue);
    }
 
    /**
     * 获取当前值
     * @return 当前值
     */
    public final float get() {
        return Float.intBitsToFloat(intBits);
    }
 
    /**
     * 设置值
     * @param newValue 要设置成的值
     */
    public final void set(float newValue) {
        intBits = Float.floatToIntBits(newValue);
    }
 
    /**
     * 原子操作设值置，并返回之前的值
     * @param newValue
     * @return
     */
    public final float getAndSet(float newValue) {
        return getAndSetFloat(newValue);
    }
 
    /**
     * 原子操作加，并返回操作完成后的值
     * @param delta
     * @return
     */
    public final float getAndAdd(float delta) {
        return getAndAddFloat(delta);
    }
 
    /**
     * 原子操作加，并返回操作前的值
     * @param delta
     * @return
     */
    public final float addAndGet(float delta) {
        return getAndAddFloat(delta) + delta;
    }
 
    /**
     * CAS实现原子加，并返回操作之前的值
     * @param delta 要增加的值
     * @return
     */
    private float getAndAddFloat(float delta) {
        int oldBits, newBits;
        do {
            oldBits = intBits;
            newBits = Float.floatToIntBits(Float.intBitsToFloat(oldBits) + delta); // 如果需要获得精确的运算结果，可以改造此处
        } while (!fieldUpdater.compareAndSet(this, oldBits, newBits));
        return Float.intBitsToFloat(oldBits);
    }
 
    /**
     * CAS实现原子替换，并返回替换之前的值
     * @param newValue 要替换成的新值
     * @return
     */
    private float getAndSetFloat(float newValue) {
        int oldBits, newBits;
        do {
            oldBits = intBits;
            newBits = Float.floatToIntBits(newValue);
        } while (!fieldUpdater.compareAndSet(this, oldBits, newBits));
        return Float.intBitsToFloat(oldBits);
    }
 
    @Override
    public int intValue() {
        return (int)get();
    }
 
    @Override
    public long longValue() {
        return (long)get();
    }
 
    @Override
    public float floatValue() {
        return get();
    }
 
    @Override
    public double doubleValue() {
        return (double)get();
    }
 
    public String toString() {
        return Float.toString(get());
    }
}