package com.yonyou.kms.modules.cms.utils;
/**
 * 
 * 多线程上传文件类:使用多线程将文件转换后上传到远程服务器
 * 
 * @yangshw6
 * 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.model.PutObjectResult;
import com.yonyou.kms.modules.sys.utils.FileStorageUtils;

public class UploadThreadUtils implements  Runnable{
	private MultipartFile file;
	private String articleId;
	private String tempArticleId;
	private String ossFileKey;
	public UploadThreadUtils(MultipartFile file, String articleId,
			String tempArticleId,String ossFileKey) {
		this.file = file;
		this.articleId = articleId;
		this.tempArticleId = tempArticleId;
		this.ossFileKey=ossFileKey;
	}
	
	public boolean upload(){
		String fileName = file.getOriginalFilename();
		long fileSize=file.getSize();
		String fileType= file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		File outputFilePdf = null;
		InputStream inputStreanmPdf=null;
		//临时存在本地的文件
		File inputFile=null;
		//本地临时文件的路径
		String inputFilePath=null;
		
		//临时文件缓存储在服务器下的D盘temp文件夹下，上传OSS后删除
		inputFile=new File(FileStorageUtils.kms_tempfile_path+fileName);
		inputFilePath= FileStorageUtils.kms_tempfile_path+fileName;
		//将文件拷贝到inputFile临时文件夹下
		try{
			FileCopyUtils.copy(file.getBytes(), inputFile);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		if(FileStorageUtils.contentType(fileType)){
				//FiletoPDF start
			    outputFilePdf = new File(FileStorageUtils.kms_tempfile_path+fileName+".pdf");
			    SaveAsPDF as= new SaveAsPDF(inputFile,outputFilePdf);
			    
			    as.saveAsPDF();
//			    responseStr=op.saveAsPDF();
//			    if(responseStr.equals("false")){
//			    	return responseStr;
//			    }
//			    boolean flag=true;
//			    do{
//			    	switch(as.getState()){
//			    	case RUNNABLE:break;
//			    	case TERMINATED:
//			    		flag =  false;
//			    		break;
//			    	}
//			    }while(flag);
			    inputStreanmPdf = null;
			    
			    try{
			    	inputStreanmPdf=new FileInputStream(outputFilePdf);
			    }catch(Exception e){return false;}
			    	
			}
		//FiletoPDF end	

		// 上传Object
		if(!FileStorageUtils.filevideo(fileType).equals("0")){
			
			//上传视频,mp4,avi,wmv
			String outputFilePath = inputFilePath+".flv";
			ConvertVideo convertVideo =new ConvertVideo(inputFilePath,outputFilePath);
			if (convertVideo.process()) {
		            //源文件上传
				System.out.println("3");
				try{
					PutObjectResult result=FileStorageUtils.upload(ossFileKey,inputFile,fileName,fileType, fileSize,file.getInputStream());
				}catch(Exception e){
					return false;}
		            
					
		            //flv文件上传
			        String oss_file_flv_key = ossFileKey+".flv";
			        File flvFile = new File(outputFilePath);
			        String flvFileName = "";
			        String flvFileType = "newflv";
			        Long flvSize = flvFile.length();
			        
			        InputStream flvinputStream = null;
			        System.out.println("4");
			        try{
			        	flvinputStream = new FileInputStream(outputFilePath);
			        }catch(Exception e){return false;}
			        	
			        
			        System.out.println("5");
			        PutObjectResult resultFlv=FileStorageUtils.upload(oss_file_flv_key,flvFile,outputFilePath,flvFileType,flvSize,flvinputStream);
		            
		        }
			
		}else{
			//上传其他
			try{
				System.out.println("6");
				InputStream fileuplod=file.getInputStream();
				
				PutObjectResult result=FileStorageUtils.upload(ossFileKey, inputFile,fileName,fileType,fileSize,fileuplod);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			
		}
		FileDelete fileDelete = new FileDelete();
		//上传newPDF
		if(FileStorageUtils.contentType(fileType)){
			fileType="newpdf";
			String ossFilePdfKey=ossFileKey+".pdf";
			
			System.out.println("7");
		    PutObjectResult resultPdf =FileStorageUtils.upload(ossFilePdfKey, outputFilePdf,fileName,fileType,outputFilePdf.length(),inputStreanmPdf);
		
		}
			
		

	
	
	//删除临时文件
	fileDelete.deleteFile(FileStorageUtils.kms_tempfile_path+fileName);
	if(fileType.equals("newpdf")){	
		fileDelete.deleteFile(FileStorageUtils.kms_tempfile_path+fileName+".pdf");
		System.out.println("删除pdf成功！");
	}
	
	//删除flv视频
	if(!FileStorageUtils.filevideo(fileType).equals("0")){
		fileDelete.deleteFile(FileStorageUtils.kms_tempfile_path+fileName+".flv");
		System.out.println("删除flv成功！");
	}	
		return true;
	}
	
	
	public void run(){
		upload();
	}
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getTempArticleId() {
		return tempArticleId;
	}
	public void setTempArticleId(String tempArticleId) {
		this.tempArticleId = tempArticleId;
	}
	
	
	
}
