package com.xray.netdisk.dao;

import com.xray.netdisk.pojo.User;

import java.sql.SQLException;

/**
 * 用户数据访问接口
 */
public interface IUserDAO {

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password) throws SQLException;

    /**
     * 通过id查询用户
     * @param userId
     * @return
     */
    User selectUserById(Long userId) throws SQLException;
}
