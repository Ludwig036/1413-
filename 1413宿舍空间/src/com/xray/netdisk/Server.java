package com.xray.netdisk;

import com.xray.netdisk.dao.IFileDAO;
import com.xray.netdisk.dao.IUserDAO;
import com.xray.netdisk.dao.impl.FileDAOImpl;
import com.xray.netdisk.dao.impl.UserDAOImpl;
import com.xray.netdisk.pojo.FileItem;
import com.xray.netdisk.pojo.User;
import com.xray.netdisk.utils.FileUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

import static com.xray.netdisk.utils.FileUtils.*;

/**
 * 网盘服务器
 */
public class Server {

    private IUserDAO userDAO = new UserDAOImpl();

    private IFileDAO fileDAO = new FileDAOImpl();

    /**
     * 处理文件的查询
     */
    public void handleListFiles(DataInputStream inputStream,DataOutputStream outputStream) throws IOException {
        //获得文件集合的JSON
        List<FileItem> uploadFiles = getUploadFiles();
        String json = listToJSON(uploadFiles);
        //发送给客户端
        outputStream.writeUTF(json);
    }

    /**
     * 处理文件上传
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public void handleUpload(DataInputStream inputStream,DataOutputStream outputStream) throws IOException {
        //接受文件名
        String fileName = inputStream.readUTF();
        if("error".equals(fileName)){
            System.out.println("客户端文件错误");
            return;
        }
        //创建文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(UPLOAD_DIR + "\\" + fileName);
        //从客户端输入流读取字节，写入到本地文件中
        FileUtils.io(inputStream,fileOutputStream);
    }

    /**
     * 处理下载
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public void handleDownload(DataInputStream inputStream,DataOutputStream outputStream) throws IOException{
        //接受文件名
        String fileName = inputStream.readUTF();
        //判断服务器上是否存在该文件
        File file = new File(UPLOAD_DIR + "\\" + fileName);
        if(file.exists()){
            //如果存在，就发送文件名给客户端
            outputStream.writeUTF(fileName);
            //读取本地磁盘文件，发送给客户端
            FileInputStream fileInputStream = new FileInputStream(file);
            FileUtils.io(fileInputStream,outputStream);
        }else{
            //不存在就发送错误信息
            outputStream.writeUTF("error");
        }
    }

    /**
     * 处理删除
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public void handleDelete(DataInputStream inputStream,DataOutputStream outputStream) throws IOException{
        //接受文件名
        String fileName = inputStream.readUTF();
        //判断服务器上是否存在该文件
        File file = new File(UPLOAD_DIR + "\\" + fileName);
        if(file.exists()){
            //如果存在，就发送文件名给客户端
            outputStream.writeUTF(fileName);
            //删除文件
            file.delete();
        }else{
            //不存在就发送错误信息
            outputStream.writeUTF("error");
        }
    }

    public void handleLogin(DataInputStream inputStream,DataOutputStream outputStream) throws IOException{
        //接受账号和密码
        String username = inputStream.readUTF();
        String password = inputStream.readUTF();
        //在数据库中登录验证
        try {
            User user = userDAO.login(username, password);
            if(user == null){
                //用户不存在，返回-1
                outputStream.writeLong(-1L);
                return;
            }
            //返回用户id给客户端
            outputStream.writeLong(user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动服务器
     */
    public void start()  {
        System.out.println("网盘服务器启动了");
        //创建服务器Socket
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            //接受客户端连接
            for(;;){
                Socket socket = serverSocket.accept();
                //获得IO流
                try(DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());){
                    //获得客户端的命令类型
                    int type = inputStream.readInt();
                    switch (type){
                        case TYPE_LIST:
                            System.out.printf("客户端%s执行查询\n",socket.getInetAddress());
                            handleListFiles(inputStream,outputStream);
                            break;
                        case TYPE_UPLOAD:
                            System.out.printf("客户端%s执行上传\n",socket.getInetAddress());
                            handleUpload(inputStream,outputStream);
                            break;
                        case TYPE_DOWNLOAD:
                            System.out.printf("客户端%s执行下载\n",socket.getInetAddress());
                            handleDownload(inputStream,outputStream);
                            break;
                        case TYPE_DELETE:
                            System.out.printf("客户端%s执行删除\n",socket.getInetAddress());
                            handleDelete(inputStream,outputStream);
                            break;
                        case TYPE_LOGIN:
                            System.out.printf("客户端%s执行登录\n",socket.getInetAddress());
                            handleLogin(inputStream,outputStream);
                            break;
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
