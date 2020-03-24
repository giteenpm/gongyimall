package org.jcut.gmall.manage;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/*
* 测试客户端上传命令
* */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageWebApplicationTests {

	@Test
	public void contextLoads() throws IOException, MyException {
		//配置fdfs的全局连接地址
		String tracker = GmallManageWebApplicationTests.class.getResource("/tracker.conf").getPath();
        //初始化客户端
		ClientGlobal.init(tracker);
		//得到tracker客户端实例
		TrackerClient trackerClient=new TrackerClient();
		//获得trackerServer的实例
		TrackerServer trackerServer = trackerClient.getConnection();
		//通过trackerServer得到一个storageClient连接客户端
		StorageClient storageClient = new StorageClient(trackerServer, null);

		String[] uploadInfos = storageClient.upload_file("d:/1.jpg", "jpg", null);
		String url="http://47.98.183.175";
		for (String uploadInfo:uploadInfos) {
			url+="/"+uploadInfo;
		}
		System.out.println(url);
	}

}
