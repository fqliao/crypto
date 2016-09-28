package symmetry;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

public class AesCBC {
	static String  hexSecretKey1 = "140b41b22a29beb4061bda66b6747e14";
	static String  hexCipherText1 = "4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81";
	static String  hexCipherText2 = "5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253";
	static String  hexCipherText3 = "20814804c1767293b99f1d9cab3bc3e7 ac1e37bfb15599e5f40eef805488281d";
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchProviderException, InvalidAlgorithmParameterException, DecoderException {
//		jdkAesDecrypt(hexSecretKey1,hexCipherText1);
//		System.out.println("============================================");
//		jdkAesDecrypt(hexSecretKey1,hexCipherText2);
		
//		String plainText = "我们都是好孩子！";
//		String cipherText = jdkAesEncrypt(hexSecretKey1, plainText);
//		jdkAesDecrypt(hexSecretKey1, cipherText);

	}
	//AES以CBC模式解密
	public static String jdkAesDecrypt(String secretKeyHex,String cipherText) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, DecoderException{
        //1 转换key
		byte[] encodedKey = Hex.decodeHex(secretKeyHex.toCharArray());
        SecretKeySpec convertSecretKey = new SecretKeySpec(encodedKey, "AES");
        
        //2 分离iv和cipher
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
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
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
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
