package com.xray.netdisk.dao;

import com.xray.netdisk.pojo.FileItem;

import java.sql.SQLException;
import java.util.List;

/**
 * 文件数据访问接口
 */
public interface IFileDAO {

    /**
     * 通过用户id查询文件集合
     * @param userId
     * @return
     */
    List<FileItem> selectFileItemsByUserId(Long userId) throws SQLException;

    /**
     * 通过用户id和文件名查询文件
     * @param userId
     * @param filename
     * @return
     */
    FileItem selectFileItemByUserIdAndFilename(Long userId,String filename) throws SQLException;

    /**
     * 通过文件id查询文件
     * @param id
     * @return
     */
    FileItem selectFileItemById(Long id) throws SQLException;

    /**
     * 添加文件
     * @param fileItem
     */
    void insertFileItem(FileItem fileItem) throws SQLException;

    /**
     * 更新文件
     * @param fileItem
     */
    void updateFileItem(FileItem fileItem) throws SQLException;

    /**
     * 删除文件
     * @param id
     */
    void deleteFileItem(Long id) throws SQLException;
}
