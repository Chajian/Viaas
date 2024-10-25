package com.viaas.docker.task.event;

import com.viaas.docker.entity.Image;
import lombok.Data;

/**
 * 拉去镜像事件
 * @author supeng
 */
@Data
public class PullImageEvent extends Event<Image> {

    {
        setName("pullImage");
        setUpdateTimes(0);
    }
    /**拉取进度**/
    private int updateTimes;

}
