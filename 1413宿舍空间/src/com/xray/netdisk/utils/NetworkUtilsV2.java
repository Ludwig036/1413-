package com.xray.netdisk.utils;

import com.xray.netdisk.pojo.FileItem;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import static com.xray.netdisk.utils.FileUtils.*;

/**
 * 客户端使用的网络通信工具类 V2
 */
public class NetworkUtilsV2 {

    /**
     * 登录服务器
     * @param username
     * @param password
     * @return
     */
    public static Long login(String username,String password){
        //连接服务器
        try(Socket socket = new Socket(IP,PORT);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())){
            //发送登录命令
            outputStream.writeInt(TYPE_LOGIN);
            //发送账号
            outputStream.writeUTF(username);
            //发密码
            outputStream.writeUTF(password);
            //接受用户id
            long userId = inputStream.readLong();
            return userId;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得服务器端的文件列表
     * @return
     */
    public static List<FileItem> getFileItems(Long userId){
        //连接服务器
        try(Socket socket = new Socket(IP,PORT);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())){
            //发送查询命令
            outputStream.writeInt(TYPE_LIST);
            //发送用户id
            outputStream.writeLong(userId);
            //接受服务器发送的JSON
            String json = inputStream.readUTF();
            //解析JSON,返回集合
            return FileUtils.jsonToList(json);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件上传
     * @param path
     */
    public static void uploadFile(Long userId,String path){
        //连接服务器
        try(Socket socket = new Socket(IP,PORT);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())){
            //发送查询命令
            outputStream.writeInt(TYPE_UPLOAD);
            //创建文件对象
            File file = new File(path);
            //判断文件是否存在
            if(!file.exists()){
                outputStream.writeUTF("error");
                System.out.println("文件不存在");
                return;
            }
            //发送文件名
            outputStream.writeUTF(file.getName());
            //发送用户ID
            outputStream.writeLong(userId);
            //创建文件输入流
            FileInputStream fileInputStream = new FileInputStream(path);
            //读取文件流，发送给服务器
            FileUtils.io(fileInputStream,outputStream);
            System.out.println("上传完毕");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件下载
     * @param fileId
     */
    public static void downloadFile(Long fileId){
        //连接服务器
        try(Socket socket = new Socket(IP,PORT);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())){
            //发送下载命令
            outputStream.writeInt(TYPE_DOWNLOAD);
            //发送文件ID
            outputStream.writeLong(fileId);
            //接受服务器端的消息
            String name = inputStream.readUTF();
            if("error".equals(name)){
                System.out.println("服务器不存在该文件");
                return;
            }
            //判断下载目录如果不存在就创建
            File file = new File(DOWNLOAD_DIR);
            if(!file.exists()){
                file.mkdirs();
            }
            //读取服务器端的数据，写入磁盘文件
            FileOutputStream fileOutputStream = new FileOutputStream(DOWNLOAD_DIR + "\\" + name);
            FileUtils.io(inputStream,fileOutputStream);
            System.out.println("下载完成");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件删除
     * @param fileId
     */
    public static void deleteFile(Long fileId){
        //连接服务器
        try(Socket socket = new Socket(IP,PORT);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())){
            //发送删除命令
            outputStream.writeInt(TYPE_DELETE);
            //发送文件ID
            outputStream.writeLong(fileId);
            //接受服务器端的消息
            String name = inputStream.readUTF();
            if("error".equals(name)){
                System.out.println("服务器不存在该文件");
                return;
            }
            System.out.println("删除完成");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
