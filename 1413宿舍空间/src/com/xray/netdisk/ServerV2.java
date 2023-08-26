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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.xray.netdisk.utils.FileUtils.*;

/**
 * 网盘服务器
 */
public class ServerV2 {

    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //创建线程池
    private ExecutorService threadPool = Executors.newFixedThreadPool(CPU_COUNT * 2);

    private IUserDAO userDAO = new UserDAOImpl();

    private IFileDAO fileDAO = new FileDAOImpl();

    /**
     * 处理文件的查询
     */
    public synchronized void handleListFiles(DataInputStream inputStream,DataOutputStream outputStream) throws IOException {
        //接受用户的id
        long userId = inputStream.readLong();
        try {
            //查询用户的相关文件
            List<FileItem> fileItems = fileDAO.selectFileItemsByUserId(userId);
            //获得文件集合的JSON
            String json = listToJSON(fileItems);
            //发送给客户端
            outputStream.writeUTF(json);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理文件上传
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public synchronized void handleUpload(DataInputStream inputStream,DataOutputStream outputStream) throws IOException{
        //接受文件名
        String fileName = inputStream.readUTF();
        if("error".equals(fileName)){
            System.out.println("客户端文件错误");
            return;
        }
        //接受用户ID
        long userId = inputStream.readLong();
        //查询用户名
        try {
            User user = userDAO.selectUserById(userId);
            //判断该用户的上传目录是否存在，不存在就创建
            File file = new File(UPLOAD_DIR + "\\" + user.getUsername());
            if(!file.exists()){
                file.mkdirs();
            }
            File uploadFile = new File(UPLOAD_DIR +
                    "\\" + user.getUsername() + "\\" + fileName);
            //创建文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(uploadFile);
            //从客户端输入流读取字节，写入到本地文件中
            FileUtils.io(inputStream,fileOutputStream);
            //判断该用户的文件在数据库是否存在
            FileItem fileItem = fileDAO.selectFileItemByUserIdAndFilename(userId, fileName);
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            //不存在就插入新数据
            if(fileItem == null){
                fileItem = new FileItem(0L,uploadFile.getName(),uploadFile.getAbsolutePath(),
                        uploadFile.length(),userId,0,time);
                fileDAO.insertFileItem(fileItem);
            }else{
                //存在就更新长度和上传时间
                fileItem.setLength(uploadFile.length());
                fileItem.setCreate_time(time);
                fileDAO.updateFileItem(fileItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理下载
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public synchronized void handleDownload(DataInputStream inputStream,DataOutputStream outputStream) throws IOException{
        //接受文件ID
        Long fileId = inputStream.readLong();
        //判断服务器上是否存在该文件
        try {
            FileItem fileItem = fileDAO.selectFileItemById(fileId);
            File file = new File(fileItem.getPath());
            if(file.exists()){
                //如果存在，就发送文件名给客户端
                outputStream.writeUTF(file.getName());
                //读取本地磁盘文件，发送给客户端
                FileInputStream fileInputStream = new FileInputStream(file);
                FileUtils.io(fileInputStream,outputStream);
            }else{
                //不存在就发送错误信息
                outputStream.writeUTF("error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理删除
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public synchronized void handleDelete(DataInputStream inputStream,DataOutputStream outputStream) throws IOException{
        //接受文件ID
        Long fileId = inputStream.readLong();
        //判断服务器上是否存在该文件
        try {
            FileItem fileItem = fileDAO.selectFileItemById(fileId);
            File file = new File(fileItem.getPath());
            if(file.exists()){
                //如果存在，就发送文件名给客户端
                outputStream.writeUTF(file.getName());
                //删除数据库和磁盘上的文件
                fileDAO.deleteFileItem(fileId);
                file.delete();
            }else{
                //不存在就发送错误信息
                outputStream.writeUTF("error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void handleLogin(DataInputStream inputStream,DataOutputStream outputStream) throws IOException{
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
                //使用线程池，启动线程完成客户端的操作
//                threadPool.execute(() -> {
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
//                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServerV2().start();
    }
}
