package org.jcut.gmall.manage.util;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {
    public static String uploadImage(MultipartFile multipartFile) {
        String imgUrl="http://47.98.183.175";
        //配置fdfs的全局连接地址
        String tracker = PmsUploadUtil.class.getResource("/tracker.conf").getPath();
        //初始化客户端
        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //得到tracker客户端实例
        TrackerClient trackerClient=new TrackerClient();
        //获得trackerServer的实例
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //通过trackerServer得到一个storageClient连接客户端
        StorageClient storageClient = new StorageClient(trackerServer, null);
        try {
            //将需要的文件转换为二进制数组
            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            int i=originalFilename.lastIndexOf(".");
            String exname = originalFilename.substring(i + 1);
            String[] uploadInfos = storageClient.upload_file(bytes, exname, null);
            for (String uploadInfo:uploadInfos) {
                imgUrl+="/"+uploadInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
}
