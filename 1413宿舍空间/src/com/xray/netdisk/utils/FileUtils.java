package com.xray.netdisk.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xray.netdisk.pojo.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtils {

    //IP
    public static final String IP = "127.0.0.1";
    //端口
    public static final int PORT = 8888;
    //服务器端上传目录
    public static final String UPLOAD_DIR = "D:\\upload";
    //客户端下载目录
    public static final String DOWNLOAD_DIR = "D:\\download";
    // 命令类型，依次是登录，查询，上传，下载，删除
    public static final int TYPE_LOGIN = 1;
    public static final int TYPE_LIST = 2;
    public static final int TYPE_UPLOAD = 3;
    public static final int TYPE_DOWNLOAD = 4;
    public static final int TYPE_DELETE = 5;

    /**
     * 获得上传的文件列表
     * @return
     */
    public static List<FileItem> getUploadFiles(){
        File file = new File(UPLOAD_DIR);
        //判断上传目录是否存在
        if(!file.exists()){
            //创建目录
            file.mkdirs();
            return null;
        }
        File[] files = file.listFiles();
        //将文件信息添加到文件集合中
        List<FileItem> list = new ArrayList<>();
        for(int i = 0;i < files.length;i++){
            File f = files[i];
            if(f.isFile()){
                FileItem fileItem = new FileItem(i+1L,f.getName(),f.getAbsolutePath(),f.length());
                list.add(fileItem);
            }
        }
        return list;
    }

    /**
     * 集合转换为JSON
     * @param fileItems
     * @return
     */
    public static String listToJSON(List<FileItem> fileItems){
        return new Gson().toJson(fileItems);
    }

    /**
     * JSON转换为集合
     * @param json
     * @return
     */
    public static List<FileItem> jsonToList(String json){
        return new Gson().fromJson(json,new TypeToken<List<FileItem>>(){}.getType());
    }

    /**
     * 文件IO
     * @param in
     * @param out
     */
    public static void io(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[1024];
        int len = 0;
        //从输入流读取字节
        while((len = in.read(buff)) != -1){
            //将字节写入到输出流
            out.write(buff,0,len);
        }
        in.close();
        out.close();
    }

    public static void main(String[] args) {
        List<FileItem> uploadFiles = FileUtils.getUploadFiles();
        String json = FileUtils.listToJSON(uploadFiles);
        System.out.println(json);
        List<FileItem> fileItems = FileUtils.jsonToList(json);
        System.out.println(fileItems);
    }
}
