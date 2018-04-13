package com.yonyou.kms.modules.cms.utils;   
  
  
  
import java.io.BufferedReader;   
  
import java.io.DataOutputStream;   
  
import java.io.IOException;   
  
import java.io.InputStreamReader;   
  
import java.net.HttpURLConnection;   
import java.net.URLDecoder;
  
import java.net.URL;   
  
import java.net.URLEncoder;   
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
  
/**
 * 
 * HttpClient请求工具类
 * @author Administrator
 *
 */
public class HttpIvokerUtils {   
  
  
		   

        
        
        public static JSONObject httpPost(String url,Map<String,Object> map, boolean noNeedResponse){
            //post请求返回结果
            
        	System.out.println("POST_URL:"+url);
        	DefaultHttpClient httpClient = new DefaultHttpClient();
            JSONObject jsonResult = null;
            HttpPost method = new HttpPost(url);
            try {
            	
            	//单个参数传递
//                if (null != jsonParam) {
//                    //解决中文乱码问题
//                    StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
//                    entity.setContentEncoding("utf-8");
//                    entity.setContentType("application/json;charset=utf-8"); 
//                    method.setEntity(entity);
//                }
                
                //多个参数传递
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("data",map.get("json").toString()));
                params.add(new BasicNameValuePair("key",map.get("key").toString()));
                params.add(new BasicNameValuePair("contentType","json"));
                UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
                entity.setContentEncoding("utf-8");
                //多参数默认ContentType:application/x-www-form-urlencoded; charset=UTF-8
                //entity.setContentType("application/json;charset=utf-8"); 
                method.setEntity(entity);
                
                HttpResponse result = httpClient.execute(method);
               // url = URLDecoder.decode(url, "UTF-8");
                /**请求发送成功，并得到响应**/
                if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = "";
                    try {
                        /**读取服务器返回过来的json字符串数据**/
                        str = EntityUtils.toString(result.getEntity(),"utf-8");
                        System.out.println(":"+result.getHeaders("Content-Type")[0]);
                        if (noNeedResponse) {
                            return null;
                        }
                        /**把json字符串转换成json对象**/
                        jsonResult = new JSONObject(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }
        
        /**
         * 发送get请求
         * @param url    路径
         * @return
         */
        public static JSONObject httpGet(String url){
            //get请求返回结果
            JSONObject jsonResult = null;
            try {
                DefaultHttpClient client = new DefaultHttpClient();
                //发送get请求
                HttpGet request = new HttpGet(url);
                HttpResponse response = client.execute(request);
     
                /**请求发送成功，并得到响应**/
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    /**读取服务器返回过来的json字符串数据**/
                    String strResult = EntityUtils.toString(response.getEntity());
                    /**把json字符串转换成json对象**/
                    jsonResult = new JSONObject(strResult);
                    url = URLDecoder.decode(url, "UTF-8");
                } else {
                   
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        
        
  
        public static void main(String[] args) {   
 
        	JSONObject jsonResult=new JSONObject("{content:'成功'}");
        //	System.out.println(httpPost(POST_URL,jsonResult,false));

        }   
  
}   