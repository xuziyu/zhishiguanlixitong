package com.yonyou.kms.modules.sys.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aliyun.opensearch.CloudsearchClient;
import com.aliyun.opensearch.CloudsearchIndex;
import com.aliyun.opensearch.CloudsearchSearch;
import com.aliyun.opensearch.object.KeyTypeEnum;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.google.common.collect.Lists;

public class FileStorageUtils {
	private static Properties FileStorageProperties = new Properties();
	public static String accessKeyId = "";
	public static String accessKeySecret = "";
	public static String bucketName = "";
	public static boolean autoCreateBucket = false;
	public static String ossEndPoint = "";
	public static String ossCliendEndPoint="";
	public static String appName = "";
	public static boolean autoCreateSearchApp = false;
	public static String host = "";
	public static String kms_tempfile_path = "";
	public static String ffmpegPath = "";
	static {
		String FileStoragePath = SystemUtil.getProjectClassesPath()
				+ "OSSKey.properties";
		// 生成文件输入流
		FileInputStream inpf = null;
		try {
			inpf = new FileInputStream(new File(FileStoragePath));
			FileStorageProperties.load(inpf);
			accessKeyId = (String) FileStorageProperties.get("key");
			ossCliendEndPoint = (String) FileStorageProperties.get("ossCliendEndPoint");
			accessKeySecret = (String) FileStorageProperties.get("secret");
			bucketName = (String) FileStorageProperties.get("bucketName");
			ossEndPoint = (String) FileStorageProperties.get("ossEndPoint");
			appName = (String) FileStorageProperties.get("appName");
			host = (String) FileStorageProperties.get("host");
			autoCreateBucket = "true".equalsIgnoreCase((String) FileStorageProperties.get("autoCreateBucket")) ? true : false;
			autoCreateSearchApp = "true".equalsIgnoreCase((String) FileStorageProperties.get("autoCreateSearchApp")) ? true : false;
			kms_tempfile_path = (String) FileStorageProperties.get("kms_tempfile_path");
			ffmpegPath = (String) FileStorageProperties.get("ffmpegPath");
		} catch (Exception e) {
//			System.out.println("读取配置文件失败");
		}
	}
	
	//初始化操作
	/**构造函数
	 * @author hefeng
	 *  初始化创建Bucket和searchapp
	*/
	public FileStorageUtils() {
		OSSClient client = new OSSClient(ossCliendEndPoint,accessKeyId, accessKeySecret);
		if(autoCreateBucket){
			if(client.doesBucketExist(bucketName)){
//				System.out.println("bucket已存在,无需创建");
			}else{
				client.createBucket(bucketName);
//				System.out.println("bucket创建成功");
			}
		}
		Map<String, Object> opts = new HashMap<String, Object>();
		CloudsearchClient searchclient = null;
		try {
			searchclient = new CloudsearchClient(accessKeyId, accessKeySecret , host, opts, KeyTypeEnum.ALIYUN);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if(autoCreateSearchApp){
			CloudsearchIndex app = new CloudsearchIndex(appName, searchclient);
			try {
				JSONObject jsonobject=new JSONObject(app.createByTemplateName("builtin_document"));
				if(jsonobject.getString("status").equals("OK")){
//					System.out.println("SearchApp创建成功");
				}else{
//					System.out.println("SearchApp创建失败");
					if(jsonobject.getJSONArray("errors").getJSONObject(0).getInt("code")==2002){
//						System.out.println("SearchApp已存在,无需创建");
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**上传文件(1)
	 * @author 
	 * @param oss中的object名称 key
	 * @param 输入流 contentStream 
	 * @param 长度 contentLength
	 * @return PutObjectResult
	*/
	//public static PutObjectResult upload(String key,InputStream contentStream,long contentLength,String fileType){
	public static PutObjectResult upload(String key,File file,String filename,String fileType,long contentLength,InputStream contentStream){	
	OSSClient client = new OSSClient(ossCliendEndPoint,accessKeyId, accessKeySecret);

	     if(fileType.equals("newpdf")){
//	    	 System.out.println("上传转换过来的pdf文件=>"+filename+".pdf"+"key:"+key);
	    	 ObjectMetadata meta = new ObjectMetadata();
	 		 meta.setContentLength(contentLength);
	 		 meta.setContentType("application/pdf");
	    	 PutObjectResult result = client.putObject(bucketName,key,contentStream,meta);
	    	 return result;
	     }
	     if(!filevideo(fileType).equals("0")){
//	    	 System.out.println("上传视频文件=>"+filename+"key:"+key);
	 		 ObjectMetadata meta = new ObjectMetadata();
	 		 meta.setContentLength(contentLength);
	 		 //meta.setContentType("video/mp4");//video/mpeg4  video/mp4
	    	 PutObjectResult result2 = client.putObject(bucketName,key,contentStream,meta);
	 		//PutObjectResult result = client.putObject(bucketName,key,file);
	    	 return result2;
	     }
	     if(fileType.equals("newflv")){
//	    	 System.out.println("上传视视频转换的flv文件=>"+"key:"+key); 
	    	 ObjectMetadata meta = new ObjectMetadata();
	 		 meta.setContentLength(contentLength);
	 		// meta.setContentType("video/x-flv");
	    	 PutObjectResult result = client.putObject(bucketName,key,contentStream,meta);
	    	// PutObjectResult result = client.putObject(bucketName,key,file);
	    	 return result;
	     }
	     
//	    System.out.println("上传其他文件=>"+filename+"key:"+key);
		PutObjectResult result = client.putObject(bucketName,key,file);
		return result;
	}
	
	/**上传文件(2)
	 * @author hefeng
	 * @param oss中的object名称 key
	 * @param 文件路径 filepath 
	 * @return PutObjectResult
	*/
	public static PutObjectResult upload(String key,String filePath){
		OSSClient client = new OSSClient(ossCliendEndPoint,accessKeyId, accessKeySecret);
		File file = new File(filePath);
	    InputStream content = null;
		try {
			content = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    ObjectMetadata meta = new ObjectMetadata();
	    meta.setContentLength(file.length());
	    PutObjectResult result = client.putObject(bucketName, key, content, meta);
		return result;
	}
	
	/**下载文件
	 * @author hefeng
	 * @param oss中的object名称 key
	 * @return InputStream
	*/
	public static InputStream download(String key){
		OSSClient client = new OSSClient(ossCliendEndPoint,accessKeyId, accessKeySecret);
		OSSObject  obj = client.getObject(bucketName,key);
		InputStream objStream = obj.getObjectContent();
		return objStream;
	}
	
	/**搜索附件
	 * @author hefeng
	 * @param 关键字 keyword
	 * @return List<Map<String, Object>>
	*/
	public static List<Map<String, Object>> search(String keyword){
		List<Map<String, Object>> lists=Lists.newArrayList();
		if(keyword.equals("")||keyword==null){
			return lists;
		}
		Map<String, Object> opts = new HashMap<String, Object>();
		CloudsearchClient client = null;
		try {
			client = new CloudsearchClient(accessKeyId, accessKeySecret , host, opts, KeyTypeEnum.ALIYUN);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		CloudsearchSearch search = new CloudsearchSearch(client);
		search.addIndex(appName);
		String s="'"+keyword+"'";
		search.setQueryString(s);
		search.setFormat("json");
//		search.addFilter("format=\"word\"");
		search.addFilter("creation_date!=0");//过滤日志文件(待验证)
		JSONObject jsonobject=null;
		try {
			jsonobject=new JSONObject(search.search());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONArray array =jsonobject.getJSONObject("result").getJSONArray("items");
		String[] fields={"identifier","title","author","subject","keywords","creation_date","modified_date","content","format"};
		for (int i = 0; i < array.length(); i++) {
            JSONObject object = (JSONObject) array.opt(i);
            Map<String, Object> map = new HashMap<String, Object>();  
            for (String str : fields) {  
                map.put(str, object.get(str));  
            }  
            list.add(map);
        }
		return list;
	}
	
	public static String gerObjectUrl(String key){
		
		OSSClient client = new OSSClient(ossCliendEndPoint,accessKeyId, accessKeySecret);
		// Generate a presigned URL 
    	Date expires = new Date (new Date().getTime()+ 2000 * 60 ); // 2 minute to expire 
    	GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key); 
    	generatePresignedUrlRequest.setExpiration(expires); 
    	URL url = client.generatePresignedUrl(generatePresignedUrlRequest); 
//    	System.out.println(url.toString()); 
    	return url.toString();
	}
	
	 public static boolean contentType(String FilenameExtension){  
//	        if(FilenameExtension.equals("BMP")||FilenameExtension.equals("bmp")){return "image/bmp";}  
//	        if(FilenameExtension.equals("GIF")||FilenameExtension.equals("gif")){return "image/gif";}  
//	        if(FilenameExtension.equals("JPEG")||FilenameExtension.equals("jpeg")||  
//	           FilenameExtension.equals("JPG")||FilenameExtension.equals("jpg")||     
//	           FilenameExtension.equals("PNG")||FilenameExtension.equals("png")){return "image/jpeg";}  
//	        if(FilenameExtension.equals("HTML")||FilenameExtension.equals("html")){return "text/html";}  
//	        if(FilenameExtension.equals("TXT")||FilenameExtension.equals("txt")){return true;}   
	        if(FilenameExtension.equals("PPTX")||FilenameExtension.equals("pptx")||  
	            FilenameExtension.equals("PPT")||FilenameExtension.equals("ppt")){return true;}  
	        if(FilenameExtension.equals("DOCX")||FilenameExtension.equals("docx")
	        	||FilenameExtension.equals("DOC")||FilenameExtension.equals("doc")
	        	){return true;}
	        if(FilenameExtension.equals("XLS")||FilenameExtension.equals("xls") ||
	        	FilenameExtension.equals("XLSX")||FilenameExtension.equals("xlsx")){return true;}
	        return false;  
	     }  
	 public static String filevideo(String FilenameExtension){  
	        if(FilenameExtension.equals("MP4")||FilenameExtension.equals("mp4")){return "video/mp4";}  
//	        if(FilenameExtension.equals("3GP")||FilenameExtension.equals("3gp")){return "video/3gpp";}  
	        if(FilenameExtension.equals("AVI")||FilenameExtension.equals("avi")){return "video/avi";}  
//	        if(FilenameExtension.equals("wmv")||FilenameExtension.equals("WMV")){return "video/avi";}
//	           FilenameExtension.equals("JPG")||FilenameExtension.equals("jpg")||     
//	           FilenameExtension.equals("PNG")||FilenameExtension.equals("png")){return "image/jpeg";}   
//	        if(FilenameExtension.equals("PDF")||FilenameExtension.equals("pdf")){return "application/pdf";}  

	        return "0"; 
	     }  
	 
	 public static String filePDF(String FilenameExtension){  
	        if(FilenameExtension.equals("PDF")||FilenameExtension.equals("pdf")){return "application/pdf";}  
//	        if(FilenameExtension.equals("GIF")||FilenameExtension.equals("gif")){return "image/gif";}  
//	        if(FilenameExtension.equals("JPEG")||FilenameExtension.equals("jpeg")||  
//	           FilenameExtension.equals("JPG")||FilenameExtension.equals("jpg")||     
//	           FilenameExtension.equals("PNG")||FilenameExtension.equals("png")){return "image/jpeg";}   
//	        if(FilenameExtension.equals("PDF")||FilenameExtension.equals("pdf")){return "application/pdf";}  

	        return "0";  
	     }
	 
	 /**
	  * 
	  * @param bucketName
	  * @param key
	  * @param filePath
	  * @return
	  * @throws FileNotFoundException
	  */
		public static String putObject(String key,String filePath) throws FileNotFoundException{
			OSSClient client=new OSSClient(ossCliendEndPoint,accessKeyId,accessKeySecret);
			File file=new File(filePath);
			InputStream input=new FileInputStream(file);
			ObjectMetadata meta=new ObjectMetadata();
			meta.setContentLength(file.length());
			//PutObjectResult result=client.putObject(bucketName, key, input, meta);
			return ossEndPoint+key;
		}
		
		/** 
		 * 删除单个文件 
		 * @param   sPath    被删除文件的文件名 
		 * @return 单个文件删除成功返回true，否则返回false 
		 */  
		public static boolean deleteFile(String sPath) {  
		    boolean flag = false;  
		    File file = new File(sPath);  
		    // 路径为文件且不为空则进行删除  
		    if (file.isFile() && file.exists()) {  
		        file.delete();  
		        flag = true;  
		    }  
		    return flag;  
		}
}
