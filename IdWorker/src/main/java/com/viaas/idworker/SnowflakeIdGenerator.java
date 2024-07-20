package com.viaas.idworker;

public class SnowflakeIdGenerator implements IdWorker{
    private final long epoch = 1609459200000L; // 自定义纪元（例如：2021年1月1日）
    private final long machineIdBits = 5L;
    private final long sequenceBits = 12L;
    private final long maxMachineId = -1L ^ (-1L << machineIdBits);
    private final long maxSequence = -1L ^ (-1L << sequenceBits);
    private final long machineIdShift = sequenceBits;
    private final long timestampLeftShift = sequenceBits + machineIdBits;
    private final long sequenceMask = maxSequence;

    private long machineId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    @Override
    public long getWorkerId() {
        return machineId;
    }

    @Override
    public long nextId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards");
        }
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = waitForNextMillis(timestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - epoch) << timestampLeftShift) |
                (machineId << machineIdShift) |
                sequence;
    }


    public SnowflakeIdGenerator(long machineId) {
        if (machineId > maxMachineId || machineId < 0) {
            throw new IllegalArgumentException("Machine ID out of range");
        }
        this.machineId = machineId;
    }


    private long waitForNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}
