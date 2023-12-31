package com.xray.netdisk.pojo;

/**
 * 文件实体类
 */
public class FileItem {

    private Long id;
    private String filename;
    private String path;
    private Long length;
    private Long user_id;
    private Integer open;
    private String create_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public FileItem(Long id, String filename, String path, Long length, Long user_id, Integer open, String create_time) {
        this.id = id;
        this.filename = filename;
        this.path = path;
        this.length = length;
        this.user_id = user_id;
        this.open = open;
        this.create_time = create_time;
    }

    public FileItem(Long id, String fileName, String filePath, Long length) {
        this.id = id;
        this.filename = fileName;
        this.path = filePath;
        this.length = length;
    }

    public FileItem() {
    }

    @Override
    public String toString() {
        return "FileItem{" +
                "id=" + id +
                ", fileName='" + filename + '\'' +
                ", filePath='" + path + '\'' +
                ", length=" + length +
                '}';
    }
}
