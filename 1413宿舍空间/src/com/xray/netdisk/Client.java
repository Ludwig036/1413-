package com.xray.netdisk;

import com.xray.netdisk.pojo.FileItem;
import com.xray.netdisk.utils.NetworkUtils;

import java.util.List;
import java.util.Scanner;

/**
 * 网盘客户端
 */
public class Client {

    private Scanner scanner = new Scanner(System.in);

    /**
     * 显示文件列表
     */
    public void showFileList(){
        List<FileItem> fileItems = NetworkUtils.getFileItems();
        System.out.println("文件列表：");
        fileItems.forEach(fileItem -> {
            System.out.printf("编号:%d\t文件名:%s\t长度:%d\n",fileItem.getId(),fileItem.getFilename(),fileItem.getLength());
        });
    }

    /**
     * 上传文件
     */
    public void uploadFile(){
        System.out.println("输入完整的文件路径：");
        String filePath = scanner.next();
        NetworkUtils.uploadFile(filePath);
    }

    /**
     * 下载文件
     */
    public void downloadFile(){
        System.out.println("输入文件名：");
        String fileName = scanner.next();
        NetworkUtils.downloadFile(fileName);
    }

    /**
     *  删除文件
     */
    public void deleteFile(){
        System.out.println("输入文件名：");
        String fileName = scanner.next();
        NetworkUtils.deleteFile(fileName);
    }

    /**
     * 启动客户端
     */
    public void start(){
        for(;;){
            System.out.println("请选择操作:1、查询 2、上传 3、下载 4、删除 其他数字退出");
            int type = scanner.nextInt();
            switch(type){
                case 1:
                    showFileList();
                    break;
                case 2:
                    uploadFile();
                    break;
                case 3:
                    downloadFile();
                    break;
                case 4:
                    deleteFile();
                    break;
                default:
                    return;
            }
        }
    }

    public static void main(String[] args) {
        new Client().start();
    }
}
