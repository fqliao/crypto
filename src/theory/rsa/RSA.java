package theory.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RSA {
	
	private final static SecureRandom random = new SecureRandom();
	private final static BigInteger one = new BigInteger("1");
	
	private BigInteger modulus;
	private BigInteger phi;
	
	private BigInteger publicKey;
	private BigInteger privateKey;
	
	public RSA(int bitLength){
		//按位数找素数 p q 计算n和 ∅(n)
		BigInteger p = BigInteger.probablePrime(bitLength / 2, new Random());
		BigInteger q = BigInteger.probablePrime(bitLength / 2, new Random());
		this.modulus = p.multiply(q);
		this.phi = (p.subtract(one)).multiply(q.subtract(one));
		
		//这里是判断可逆是否存在 也可以判断公钥与∅(n)是否互素 如果互素则私钥一定存在，判断互素用扩展欧几里得算法
		this.publicKey = (BigInteger.probablePrime(bitLength / 2, new Random())).mod(phi);
		boolean flag = false;//私钥是否存在标志位 默认不存在
		while(flag == false){
			try {
				this.privateKey = publicKey.modInverse(phi);
				flag = true;
			} catch (Exception e) {// 不存在
				flag = false;
				this.publicKey = (BigInteger.probablePrime(bitLength / 2, new Random())).mod(phi);
			}
		}
	}
	//RSA加密
	public String encrypt(String message){
		BigInteger m = new BigInteger(message);
		return m.modPow(publicKey, modulus).toString();
	}
	//RSA解密
	public String decrypt(String message){
		BigInteger c = new BigInteger(message);
		return c.modPow(privateKey, modulus).toString();
	}
	
	public BigInteger getModulus(){
		return this.modulus;
	}
	
	public String getPublicKey(){
		return "n="+this.modulus.toString() +"  e="+ this.publicKey.toString();
	}
}
