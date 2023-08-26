package com.xray.netdisk.dao.impl;

import com.xray.netdisk.dao.IFileDAO;
import com.xray.netdisk.pojo.FileItem;
import com.xray.netdisk.utils.JDBCUtilsV2;
import com.xray.netdisk.utils.JDBCUtilsV2;

import java.sql.SQLException;
import java.util.List;

/**
 * 文件数据访问实现类
 */
public class FileDAOImpl implements IFileDAO {


    @Override
    public List<FileItem> selectFileItemsByUserId(Long userId) throws SQLException {
        return JDBCUtilsV2.query(FileItem.class,"select * from t_file where user_id = ?",userId);
    }

    @Override
    public FileItem selectFileItemByUserIdAndFilename(Long userId, String filename) throws SQLException {
        List<FileItem> list = JDBCUtilsV2.query(FileItem.class, "select * from t_file where user_id = ? and filename=?", userId, filename);
        if(list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public FileItem selectFileItemById(Long id) throws SQLException {
        List<FileItem> list = JDBCUtilsV2.query(FileItem.class, "select * from t_file where id = ?", id);
        if(list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void insertFileItem(FileItem fileItem) throws SQLException {
        JDBCUtilsV2.update("insert into t_file(filename,path,length,user_id,open,create_time) values(?,?,?,?,?,?)",
                fileItem.getFilename(),fileItem.getPath(),fileItem.getLength(),
                fileItem.getUser_id(),fileItem.getOpen(),fileItem.getCreate_time());
    }

    @Override
    public void updateFileItem(FileItem fileItem) throws SQLException {
        JDBCUtilsV2.update("update t_file set filename=?,path=?,length=?,user_id=?,open=?,create_time=? where id=?",
                fileItem.getFilename(), fileItem.getPath(), fileItem.getLength(), fileItem.getUser_id(),
                fileItem.getOpen(), fileItem.getCreate_time(), fileItem.getId());

    }

    @Override
    public void deleteFileItem(Long id) throws SQLException {
        JDBCUtilsV2.update("delete from t_file where id=?",id);
    }
}
