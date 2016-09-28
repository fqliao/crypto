package symmetry;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class Des {
	public static String m = "hello world";
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
		//jdkDes(m);
//		bcDes(m);
//		jdk3Des(m);
		bc3Des(m);
	}
	//jdk 实现DES
	public static void jdkDes(String message) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		
		//1生成key
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
		keyGenerator.init(56);
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] bytesKey = secretKey.getEncoded();
		
		//2 转换key
		DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
		Key convertSecretKey = secretKeyFactory.generateSecret(desKeySpec);
		
		//3 加密
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
		byte[] cipherText = cipher.doFinal(message.getBytes());
		System.out.println("jdk des encrypt:"+Hex.encodeHexString(cipherText));
		
		//4 解密
		cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
		byte[] plainText = cipher.doFinal(cipherText);
		System.out.println(new String(plainText));
	}
	
	//bc 实现DES
	public static void bcDes(String message) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException{
		
		Security.addProvider(new BouncyCastleProvider());
		//1生成key
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DES","BC");
		keyGenerator.init(56);
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] bytesKey = secretKey.getEncoded();
		
		//2 转换key
		DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
		Key convertSecretKey = secretKeyFactory.generateSecret(desKeySpec);
		
		//3 加密
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
		byte[] cipherText = cipher.doFinal(message.getBytes());
		System.out.println("bc des encrypt:"+Hex.encodeHexString(cipherText));
		
		//4 解密
		cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
		byte[] plainText = cipher.doFinal(cipherText);
		System.out.println(new String(plainText));
	}
	
	//jdk实现三重DES
	public static void jdk3Des(String message) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		
		//1生成key
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
		keyGenerator.init(168);
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] bytesKey = secretKey.getEncoded();
		
		//2 转换key
		DESedeKeySpec desKeySpec = new DESedeKeySpec(bytesKey);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
		Key convertSecretKey = secretKeyFactory.generateSecret(desKeySpec);
		
		//3 加密
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
		byte[] cipherText = cipher.doFinal(message.getBytes());
		System.out.println("jdk 3des encrypt:"+Hex.encodeHexString(cipherText));
		
		//4 解密
		cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
		byte[] plainText = cipher.doFinal(cipherText);
		System.out.println(new String(plainText));
	}
	
	//bc 实现3DES
	public static void bc3Des(String message) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException{
		
		Security.addProvider(new BouncyCastleProvider());
		//1生成key
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede","BC");
//		keyGenerator.init(168);
		keyGenerator.init(new SecureRandom()); //第二种方式
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] bytesKey = secretKey.getEncoded();
		
		//2 转换key
		DESedeKeySpec desedeKeySpec = new DESedeKeySpec(bytesKey);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
		Key convertSecretKey = secretKeyFactory.generateSecret(desedeKeySpec);
		
		//3 加密
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
		byte[] cipherText = cipher.doFinal(message.getBytes());
		System.out.println("bc 3des encrypt:"+Hex.encodeHexString(cipherText));
		
		//4 解密
		cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
		byte[] plainText = cipher.doFinal(cipherText);
		System.out.println(new String(plainText));
	}
		
}
