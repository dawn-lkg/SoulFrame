package com.clm.system.domain;


import com.clm.system.domain.vo.RoleSimpleVO;
import lombok.Data;

import java.util.List;

/**
 * @author 陈黎明
 * @date 2025/3/8 下午12:01
 */

@Data
public class UserInfo {

    private Long userId;

    private String userName;

    private String nickName;

    private String email;

    private String phone;

    private String sex;

    private String avatar;

    private List<RoleSimpleVO> roles;
}
