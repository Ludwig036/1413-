package com.xray.netdisk.ui;

import com.xray.netdisk.pojo.FileItem;
import com.xray.netdisk.utils.NetworkUtils;
import com.xray.netdisk.utils.NetworkUtilsV2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class ClientForm {
    private JTable tbFile;
    private JButton btRefresh;
    private JButton btUpload;
    private JButton btDownload;
    private JButton btDelete;
    JPanel panel1;
    //文件id
    private Long fileId;
    //用户id
    private Long userId;

    public ClientForm(Long userId) {
        //从登录界面把用户id传过来
        this.userId = userId;
        refreshFileList();
        //添加刷新按钮监听
        btRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshFileList();
            }
        });
        //添加上传按钮的监听
        btUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = JOptionPane.showInputDialog("输入上传文件路径");
                if(path.isEmpty()){
                    JOptionPane.showMessageDialog(null,"请输入路径");
                    return;
                }
                NetworkUtilsV2.uploadFile(userId,path);
                JOptionPane.showMessageDialog(null,"上传完毕");
            }
        });
        //下载按钮的监听器
        btDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileId == null){
                    JOptionPane.showMessageDialog(null,"请选择下载的文件");
                    return;
                }
                NetworkUtilsV2.downloadFile(fileId);
                JOptionPane.showMessageDialog(null,"下载完毕");
                fileId = null;
            }
        });
        //表格的鼠标点击事件
        tbFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //获得选中的文件ID
                int row = tbFile.getSelectedRow();
                fileId = Long.valueOf(tbFile.getModel().getValueAt(row, 0).toString());
            }
        });
        //删除按钮监听
        btDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileId == null){
                    JOptionPane.showMessageDialog(null,"请选择删除的文件");
                    return;
                }
                NetworkUtilsV2.deleteFile(fileId);
                JOptionPane.showMessageDialog(null,"删除完毕");
                fileId = null;
            }
        });
    }

    public void refreshFileList(){
        //链接服务器获得文件列表
        List<FileItem> fileItems = NetworkUtilsV2.getFileItems(userId);
        //将文件集合转换为二维数组
        Object[][] data = new Object[fileItems.size()][3];
        for(int i = 0;i < data.length;i++){
            FileItem fileItem = fileItems.get(i);
            data[i][0] = fileItem.getId();
            data[i][1] = fileItem.getFilename();
            data[i][2] = fileItem.getLength();
        }
        //将数据显示到表格中
        Object[] columnNames = {"编号","文件名","长度"};
        tbFile.setModel(new DefaultTableModel(data,columnNames));
    }

}
