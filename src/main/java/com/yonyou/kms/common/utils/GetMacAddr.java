package com.yonyou.kms.common.utils;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
public class GetMacAddr {
         
    public GetMacAddr() {    
    	
    }
    //获取本机的域名和ip地址.
    public static String getIpv4() throws UnknownHostException{
    	//System.out.println(InetAddress.getLocalHost().toString());
		return InetAddress.getLocalHost().toString();
    }
	public  static String getMACAddr()throws SocketException, UnknownHostException {
		NetworkInterface netInterface =NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
	byte[] macAddr = netInterface.getHardwareAddress();
			StringBuilder sb=new StringBuilder();
		for (byte b : macAddr) 
		{
			sb.append(toHexString(b)+"-");
		}
		return sb.substring(0,sb.length()-1);
		
	}
	
	private static String toHexString(int integer) {
		String str = Integer.toHexString((int) (integer & 0xff));
	if (str.length() == 1) {
		str = "0" + str;
		}
	return str;
	}
	
	public static void main(String args[]) throws SocketException, UnknownHostException{
		new GetMacAddr().getMACAddr();
		new GetMacAddr().getIpv4();
	}
	
}
