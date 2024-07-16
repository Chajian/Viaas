package com.viaas.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event<T> {
    long eventId;
    String name;
    String desc;
    /**
     * 事件的状态
     */
    String status;
    T t;
}
