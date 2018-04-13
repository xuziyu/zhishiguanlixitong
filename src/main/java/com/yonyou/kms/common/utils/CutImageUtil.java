package com.yonyou.kms.common.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class CutImageUtil {

	public static void cutImg(String srcPath,String targetPath,int x,int y,int width,int height,String ImageType){
		//如果没有文件夹 那么就创建一个文件夹 
		if(!new File(targetPath).isDirectory())
			 new File(targetPath).mkdirs();
		FileInputStream fis=null;
		ImageInputStream iis=null;
		
		try{
			fis=new FileInputStream(srcPath);
			Iterator<ImageReader> it=ImageIO.getImageReadersByFormatName(ImageType);
			ImageReader reader=it.next();
			iis=ImageIO.createImageInputStream(fis);
			reader.setInput(iis,true);
			ImageReadParam param=reader.getDefaultReadParam();
			Rectangle rect=new Rectangle(x,y,width,height);
			param.setSourceRegion(rect);
			BufferedImage bi=reader.read(0,param);
			ImageIO.write(bi, ImageType, new File(targetPath));
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(iis!=null){
				try {
					iis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
}
