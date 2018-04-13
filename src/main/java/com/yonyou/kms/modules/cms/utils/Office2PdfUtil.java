package com.yonyou.kms.modules.cms.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.junit.Test;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.sun.star.io.ConnectException;


public class Office2PdfUtil  {
	//public class Office2PdfUtil {
   	 private File inputFile;// 需要转换的文件  

     private File outputFile;// 输出的文件  

     public Office2PdfUtil(File inputFile, File outputFile) {  

        this.inputFile = inputFile;  

        this.outputFile = outputFile; 

    }
     public Office2PdfUtil(){
    	 
     }
     /**
      *@return 
     * @yangshw6 
      * 检测openoffice服务是否开启 运行批处理文件 执行dos命令
      *
      */
    public void  openService(){
    	boolean flag=true;
		flag=findProcess("soffice.exe");
		if(!flag){
			 String command="cmd /c c: & cd C:\\Program Files (x86)\\OpenOffice 4\\program\\ & soffice -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";
			 try {
				 Runtime.getRuntime().exec(command);
			 } catch (IOException e) {
			 e.printStackTrace();
			 }
		}
    }
    /**
     * 检测进程是否停止工作(取不到连接),自动杀掉前一个进程，开启新进程
     * 
     */
    public void ObtainService(){
    	String command="cmd /c taskkill /f /im soffice.bin /im soffice.exe &"
			+ " c: & "
			+ "cd C:\\Program Files (x86)\\OpenOffice 4\\program\\ & soffice -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard"; 
    	try {
			 Runtime.getRuntime().exec(command);
		 } catch (IOException e) {
		 e.printStackTrace();
		 }
    }
    /**
	 * 检测程序。
	 * 
	 * @param processName
	 *            线程的名字，请使用准确的名字
	 * @return 找到返回true,没找到返回false
	 */
	public static boolean findProcess(String processName) {
		BufferedReader bufferedReader = null;
		try {
			Process proc = Runtime.getRuntime().exec(
					"cmd /c  tasklist");
			bufferedReader = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String line = null;
//			System.out.println(bufferedReader.readLine());
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains(processName)) {
					return true;
				}
			}
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception ex) {
				}
			}
		}
	}

    
    
    
    
    public void docToPdf() {  
        Date start = new Date();      
        /*
         * give up run openoffice and then stop,just always runing it
        String OpenOffice_HOME = "C:\\Program Files (x86)\\OpenOffice 4";
		   如果从文件中读取的URL地址最后一个字符不是 '\'，则添加'\'  
         if (OpenOffice_HOME.charAt(OpenOffice_HOME.length() - 1) != '\\') {  
             OpenOffice_HOME += "\\";  
         }         
        String command = OpenOffice_HOME+ "program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";  
        Process pro =null;
        */
//        openService();
        OpenOfficeConnection connection =null;
         try {
        	 // 启动OpenOffice的服务 
        	 //pro = Runtime.getRuntime().exec(command);
        	 //connect to an OpenOffice.org instance running on port 8100  
        	 //connection = new SocketOpenOfficeConnection(8100);
        	 try{
        		 connection = new SocketOpenOfficeConnection("localhost", 8100);
        		 connection.connect();  
        	 }catch(Exception ex){
        		 ex.printStackTrace();
        		 ObtainService();
        		 connection = new SocketOpenOfficeConnection("localhost", 8100);
        		 connection.connect();  
        	 }
        	 DocumentConverter converter = new OpenOfficeDocumentConverter(connection);  
        	 converter.convert(inputFile, outputFile);  
        } catch (java.net.ConnectException e) {
			e.printStackTrace();
		}catch (IOException e1) {
			
			e1.printStackTrace();
		} finally {  
            if (connection != null) {  
//            	System.out.println("关闭连接");
                connection.disconnect();  
                connection = null; 
//                if(pro!=null){
//                	System.out.println("关闭OpenOffice服务的进程");
//                	pro.destroy();
//                }           
            }  
        }  
    }  
    
    /** 
     * 由于服务是线程不安全的，所以……需要启动线程 
     */ 
    public File getInputFile() {  
        return inputFile;  
    }  
    
    public void setInputFile(File inputFile) {  
        this.inputFile = inputFile;  
    }  

    public File getOutputFile() {  
        return outputFile;  
    }  

    public void setOutputFile(File outputFile) {  
        this.outputFile = outputFile;  
    } 
    
    /**
      * 单元测试
      * @param args
      */
//    @Test
//    public void test() {
//
//        File inputFile = new File("d://temp//333.docx");
//
//        File outputFile = new File("d://temp//333.pdf");
//
//        Office2PdfUtil dp=new Office2PdfUtil(inputFile,outputFile);
//
//        dp.start();
//
//    } 

}
