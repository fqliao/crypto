package symmetry;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Aes {
	
	public static String m = "hello world";
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchProviderException {
		jdkAes(m);
//		bcAes(m);

	}
	//jdk 实现AES
	public static void jdkAes(String message) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		
		//1生成key
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] bytesKey = secretKey.getEncoded();
		
		
		//2 转换key
		Key convertSecretKey = new SecretKeySpec(bytesKey, "AES");
		
		
		//3 加密
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
		byte[] cipherText = cipher.doFinal(message.getBytes());
		System.out.println("jdk aes encrypt:"+Hex.encodeHexString(cipherText));
		
		//4 解密
		cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
		byte[] plainText = cipher.doFinal(cipherText);
		System.out.println(new String(plainText));
	}
	//bc 实现AES
	public static void bcAes(String message) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException{
		
		Security.addProvider(new BouncyCastleProvider());
		//1生成key
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES","BC");
		keyGenerator.init(128);
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] bytesKey = secretKey.getEncoded();
		
		//2 转换key
		Key convertSecretKey = new SecretKeySpec(bytesKey, "AES");
		
		
		//3 加密
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
		byte[] cipherText = cipher.doFinal(message.getBytes());
		System.out.println("bc aes encrypt:"+Hex.encodeHexString(cipherText));
		
		//4 解密
		cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
		byte[] plainText = cipher.doFinal(cipherText);
		System.out.println(new String(plainText));
	}

}
