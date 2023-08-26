package com.xray.netdisk.dao.impl;

import com.xray.netdisk.dao.IUserDAO;
import com.xray.netdisk.pojo.User;
import com.xray.netdisk.utils.JDBCUtils;
import com.xray.netdisk.utils.JDBCUtilsV2;

import java.sql.SQLException;
import java.util.List;

/**
 * 用户数据访问实现类
 */
public class UserDAOImpl implements IUserDAO {

    @Override
    public User login(String username, String password) throws SQLException {
        List<User> users = JDBCUtilsV2.query(User.class,
                "select * from t_user where username=? and password=?", username, password);
        if(users.isEmpty()){
            return null;
        }
        return users.get(0);
    }

    @Override
    public User selectUserById(Long userId) throws SQLException {
        List<User> users = JDBCUtilsV2.query(User.class,
                "select * from t_user where id=?", userId);
        if(users.isEmpty()){
            return null;
        }
        return users.get(0);
    }
}
