package com.viaas.entity;

import lombok.Data;

import java.util.Date;

/**
 * 公告类-时间记录
 * @author Chajian
 */
@Data
public class TimeRecord {
    private Date createdAt;
    private Date updatedAt;

}
