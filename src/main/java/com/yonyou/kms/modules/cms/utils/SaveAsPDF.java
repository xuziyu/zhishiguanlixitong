package com.yonyou.kms.modules.cms.utils;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 
 * 利用word2007系列的一个转pdf插件在线将文件转成pdf,需要jacob.jar支持，
 * 将jacob.dll放入服务器jdk的bin和jre的bin目录中
 * 本例是jacob-1.14.3版本
 * @author yangshw6
 *	服务端需要word2007系列,支持word,ppt,excel转换
 */
public class SaveAsPDF{
	static final int wdFormatPDF = 17;// PDF 格式 word转换码
	static final int ppSaveAsPDF = 32; //ppt转换码
	static final int excelpdfFile = 0; //excel转换码
	private File inputFile;// 需要转换的文件  

    private File outputFile;// 输出的文件
    
	public SaveAsPDF(File inputStream,File outputStream) {
		this.inputFile=inputStream;
		this.outputFile=outputStream;
	}
	
	public String saveAsPDF(){
		String flag="true";
//		System.out.println("启动Word...");
		ComThread.InitSTA();
		String sfilePath=inputFile.getAbsolutePath();
		String toFilePath=outputFile.getAbsolutePath();
		//根据文件后缀判断文件是哪一种类型文件:word,ppt,excel
		String fileName=inputFile.getName();
		String fileType=fileName.substring(fileName.lastIndexOf(".")+1);
		long start = System.currentTimeMillis();
		ActiveXComponent app = null;
		Dispatch doc = null;
		Dispatch docs=null;
		int Type=0;
		try {
			
			if(fileType.equals("doc") || fileType.equals("docx")){
				app = new ActiveXComponent("Word.Application");
				app.setProperty("Visible", new Variant(false));
				docs = app.getProperty("Documents").toDispatch();
				Type=1;
			}else if(fileType.equals("ppt")|| fileType.equals("pptx")){
				app = new ActiveXComponent("PowerPoint.Application");  
//				app.setProperty("Visible", new Variant(false));
	            docs = app.getProperty("Presentations").toDispatch();
	            Type=2;
			}else if(fileType.equals("xls") || fileType.equals("xlsx")){
				app = new ActiveXComponent("Excel.Application");
				app.setProperty("Visible", new Variant(false));
				docs = app.getProperty("Workbooks").toDispatch();
				Type=3;
			}
//			doc = Dispatch.call(docs, "Open", sfilePath,true,true,false).toDispatch();
//			System.out.println("打开文档..." + sfilePath);
//			System.out.println("转换文档到PDF..." + toFilePath);
			switch(Type){
			case 1:
				doc = Dispatch.call(docs, "Open", sfilePath).toDispatch();
				Dispatch.call(doc, "SaveAs", toFilePath,wdFormatPDF);break;
			case 2:
				doc = Dispatch.call(docs, "Open", sfilePath,true,true,false).toDispatch();
				Dispatch.call(doc, "SaveAs", toFilePath,ppSaveAsPDF);break;
			case 3:
				doc = Dispatch.call(docs, "Open", sfilePath,false,true).toDispatch();
				Dispatch.call(doc, "ExportAsFixedFormat",excelpdfFile, toFilePath);break;
			}
			long end = System.currentTimeMillis();
//			System.out.println("转换完成..用时：" + (end - start) + "ms.");

		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("========Error:文档转换失败：" + e.getMessage());
			flag="false";
		} finally {
			try{
				if(doc !=null){
					Dispatch.call(doc, "Close");
				}else{
					Dispatch.call(docs, "Close");
				}
			}catch(Exception e){
				e.printStackTrace();
				
			}
			
			System.out.println("关闭文档:"+flag);
			if (app != null)
				app.invoke("Quit");
		}
		// 如果没有这句话,winword.exe进程将不会关闭
		ComThread.Release();
		ComThread.quitMainSTA();
		return flag;
	}
}
