package com.viaas.certification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.io.Serializable;
import java.util.Map;

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
//    private Map<String,Object> oidcUserinfo;

    public static UserDTO buildOf(UserDetails userDetails){
        return UserDTO.builder()
                .account(userDetails.getUsername())
                .pwd(userDetails.getPassword())
                .build();
    }
//    public Map<String,Object> getOidcUserInfo() {
//        if (oidcUserinfo == null) {
//            oidcUserinfo = OidcUserInfo.builder().name(account).picture(avatar).claim("id", id).build().getClaims();
//        }
//        return oidcUserinfo;
//    }
}
