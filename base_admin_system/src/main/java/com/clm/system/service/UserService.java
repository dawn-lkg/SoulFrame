package com.clm.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clm.common.core.domain.entity.User;
import com.clm.system.domain.dto.UserDTO;
import com.clm.system.domain.param.UserQueryParam;
import com.clm.system.domain.vo.UserPageVO;
import com.clm.system.domain.vo.UserVO;

import java.util.List;

/**
 * 用户信息表(User)表服务接口
 *
 * @author 陈黎明
 * @since 2025-03-07 14:31:37
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    User getUserByUsername(String username);

    /**
     * 获取用户分页列表
     *
     * @param param 查询参数
     * @return 分页数据
     */
    IPage<UserPageVO> getUserPage(UserQueryParam param);

    /**
     * 获取用户详细信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserInfo(Long userId);

    /**
     * 新增用户
     *
     * @param userDTO 用户信息
     */
    void addUser(UserDTO userDTO);

    /**
     * 修改用户
     *
     * @param userDTO 用户信息
     */
    void updateUser(UserDTO userDTO);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    void deleteUser(Long userId);

    /**
     * 批量删除用户
     *
     * @param userIds 用户ID列表
     */
    void batchDeleteUser(List<Long> userIds);

    /**
     * 更新用户登录信息
     * @param userId 用户ID
     */
    void updateLoginInfo(Long userId);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @param userId 用户ID
     */
    boolean checkUsernameExists(String username, Long userId);

    /**
     * 重置密码
     * @param userId 用户ID
     */
    void resetPassword(Long userId);
}

