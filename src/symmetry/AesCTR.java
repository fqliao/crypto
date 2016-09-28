package symmetry;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;


public class AesCTR {
	static String  hexSecretKey1 = "36f18357be4dbd77f050515c73fcf9f2";
	static String  hexCipherText1 = "69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329";
	static String  hexCipherText2 = "770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451";
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchProviderException, InvalidAlgorithmParameterException, DecoderException {
		jdkAesDecrypt(hexSecretKey1,hexCipherText1);
		System.out.println("============================================");
		jdkAesDecrypt(hexSecretKey1,hexCipherText2);
		
//		String plainText = "我们都是好孩子！";
//		String cipherText = jdkAesEncrypt(hexSecretKey1, plainText);
//		jdkAesDecrypt(hexSecretKey1, cipherText);
	}
		//AES以CTR模式解密
		public static String jdkAesDecrypt(String secretKeyHex,String cipherText) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, DecoderException{
	        //1 转换key
			byte[] encodedKey = Hex.decodeHex(secretKeyHex.toCharArray());
	        SecretKeySpec convertSecretKey = new SecretKeySpec(encodedKey, "AES");
	        
	        //2 分离iv和cipher
	        Cipher aesCipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
	        byte[] encodedIvCipherBytes = Hex.decodeHex(cipherText.toCharArray());
		    byte[] encodedIvBytes = Arrays.copyOfRange(encodedIvCipherBytes, 0, aesCipher.getBlockSize());
		    System.out.println("IV:"+Hex.encodeHexString(encodedIvBytes));
		    byte[] encodedCipherBytes = Arrays.copyOfRange(encodedIvCipherBytes, aesCipher.getBlockSize(), encodedIvCipherBytes.length);
		    
		    aesCipher.init(Cipher.DECRYPT_MODE, convertSecretKey,new IvParameterSpec(encodedIvBytes));
	       	byte[] result = aesCipher.doFinal(encodedCipherBytes);
	       	String plainText = StringUtils.newStringUtf8(result);
	       	System.out.println("明文："+plainText);
	       	return plainText;
		}
		
		//AES以CBC模式加密
		public static String jdkAesEncrypt(String secretKeyHex,String plainText) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, DecoderException{
	        //1 转换key
			byte[] encodedKey = Hex.decodeHex(secretKeyHex.toCharArray());
	        SecretKeySpec convertSecretKey = new SecretKeySpec(encodedKey, "AES");
	        
	        //2 选择iv
	        Cipher aesCipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
	        byte[] ivBytes = new byte[aesCipher.getBlockSize()];
	        new SecureRandom().nextBytes(ivBytes);
	        System.out.println("IV:"+Hex.encodeHexString(ivBytes));
		    
	        //3 加密
		    aesCipher.init(Cipher.ENCRYPT_MODE, convertSecretKey,new IvParameterSpec(ivBytes));
	       	byte[] cipherBytes = aesCipher.doFinal(StringUtils.getBytesUtf8(plainText));
	       	System.out.println("密文："+Hex.encodeHexString(cipherBytes));
	       	
	       	//4 组合IV和密文
	       	byte[] ivCipherBytes = new byte[ivBytes.length+cipherBytes.length];
	       	System.arraycopy(ivBytes, 0, ivCipherBytes, 0, ivBytes.length);
	       	System.arraycopy(cipherBytes, 0, ivCipherBytes, ivBytes.length, cipherBytes.length);
	       	System.out.println("IV+密文："+Hex.encodeHexString(ivCipherBytes));
	       	return Hex.encodeHexString(ivCipherBytes);
		}

}
