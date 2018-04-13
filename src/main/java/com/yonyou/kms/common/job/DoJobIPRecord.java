package com.yonyou.kms.common.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yonyou.kms.common.utils.GetMacAddr;


/**
 * 
 * 定时执行任务
 * @author yinshh3
 *
 */
@Service
@Lazy(false)
public class DoJobIPRecord {
	//测试数据,10秒发一次
	//@Scheduled(cron="10 * * * * ?")
	@Scheduled(cron="0 0 1 * * ?")//每天早上1点执行一次,发送一次请求.
	
	public void doJob() throws Exception{
		
//System.out.println("定时任务1111");
readContentFromGet();
		 
}
	//外网IP,端口9000
	public static final String GET_URL = "http://123.103.9.180:9000/CheckInfo/validate";
	public static void readContentFromGet()throws Exception{
		// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
		try{
		String ipv4="?ipv4="+URLEncoder.encode(GetMacAddr.getIpv4(),"utf-8");//将本机的域名和ip存在这个ipv4中了.
		String mac="&&mac=" +URLEncoder.encode(GetMacAddr.getMACAddr(), "utf-8");
		String getURL = GET_URL +ipv4+mac;
		URL getUrl = new URL(getURL);
		 HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
		 connection.connect();
		 BufferedReader reader = new BufferedReader(new InputStreamReader( connection.getInputStream()));
	        reader.close();
	        // 断开连接
	        connection.disconnect();
		}catch(Exception e){
			
		}
	    }
		 
		
	} 

