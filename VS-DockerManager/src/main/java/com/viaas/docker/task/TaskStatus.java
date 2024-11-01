package com.viaas.docker.task;

/**
 * 任务状态
 * @author Yanglin
 */
public enum TaskStatus {
    /*初始化状态*/
    INIT,
    /*等待状态*/
    WAITING,
    /*运行状态*/
    RUNNING,
    /*回调状态*/
    RECALL,
    /*死亡状态*/
    DEATH;
}
