package com.viaas.certification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author Chajian
 */

@TableName("users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends TimeRecord implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String account;
    private String pwd;
    /*头像*/
    private String avatar;

    public static UserDTO buildOf(UserDetails userDetails){
        return UserDTO.builder()
                .account(userDetails.getUsername())
                .pwd(userDetails.getPassword())
                .build();
    }
}
