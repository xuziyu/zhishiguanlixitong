package com.yonyou.kms.common.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import sun.misc.BASE64Decoder;

public class BaseImg64 {

	public static String generateImage(String imgStr, String path, String suffix) {
		if (imgStr == null || "null".equals(imgStr)) // 图像数据为空
		{
			System.out.println("没有图片");
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		String imgFilePath = "";
		String imgpath = "";
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			imgpath = "share" + new Date().getTime() + "." + suffix;
			imgFilePath = path + "/" + imgpath;// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
		} catch (Exception e) {
			imgpath = "";
			System.out.println("抛异常了吗");
			e.printStackTrace();
		}
		return imgpath;
	}
}
