package digest;

import java.io.*;
import java.math.BigInteger;

public class FileDigest {

	public static void main(String[] args) throws Exception {
		digest();

	}
	
	public static void digest() throws Exception{
		String pathname = "D:"+File.separator+"6 - 2 - Generic birthday attack (16 min).mp4";
		File file = new File(pathname);
		InputStream input = new FileInputStream(file);
//		byte[] bytes = new byte[(int)file.length()];
		byte[] bytes = new byte[]{1,1,1};
//		int len = input.read(bytes);
		
		String binaryString = binary(bytes, 2);
		System.out.println("数据的长度"+binaryString.length());
		System.out.println("数据的内容"+binaryString);
		
	}
	 public static String binary(byte[] bytes, int radix){  
	        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数  
	}  
}
