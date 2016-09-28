package theory.sha;

public final class SHA1
{
  public final String run(String message){
    int H0 = 0x67452301;
    int H1 = 0xEFCDAB89;
    int H2 = 0x98BADCFE;
    int H3 = 0x10325476;
    int H4 = 0xC3D2E1F0;
    
  	byte[] msg = message.getBytes();
  	int original_byte_len = msg.length;
  	long original_bit_len = original_byte_len * 8;
  	//填充1bit 1 但是实际是以一个字节 8位填充的 即1000000 = 0x80
  	msg = add(msg, (byte)0x80);

  	//填充0的字节个数，以字节数来算 512bit = 64byte 448bit = 56byte 增加1是填了1个字节 8bit
    int times = (((56 - (original_byte_len + 1) % 64) + 64) % 64);
    for (int i =0; i < times ; i++) {
  		msg = add(msg, (byte)0x00);
    }

    //将原始数据的长度补到已经进行了补位操作的消息后面 以64bit表示
    msg = add(msg, original_bit_len);

    //将消息按64字节分块 以64字节（512bit）的分组为处理单元 然后进行整体运算
    for (int i = 0; i < msg.length; i = i + 64 ) {
    	int[] w = new int[80];
    	/*
	    	w[0]        =  msg[0] + msg[1] + msg[2] + msg[3]
			w[1]        =  msg[4] + msg[5] + msg[6] + msg[7]
			...
			w[15]       =  msg[60] + msg[61] + msg[62] + msg[63]
		  */
    	//将64字节的块存入16个字（32bit）
    	for (int j=0; j < 16; j++) {
    		w[j] = (msg[i + (j * 4) + 0] << 24) + 
    			   (msg[i + (j * 4) + 1] << 16) + 
    			   (msg[i + (j * 4) + 2] << 8) + 
    			   (msg[i + (j * 4) + 3] & 0xff);
    	}

    	/*
			w[16]       = (w[13] xor w[8] xor w[2] xor w[0]) leftrotate 1
			w[17]       = (w[14] xor w[9] xor w[3] xor w[1]) leftrotate 1
			...
			w[79]       = (w[76] xor w[71] xor w[65] xor w[63]) leftrotate 1
		  */
    	//计算16-79的字
  		for (int t = 16; t < 80; t++){
  			int x = w[t - 3] ^ w[t - 8] ^ w[t - 14] ^ w[t - 16];
  			w[t] = leftrotate(x, 1);
  		}


  		int A = H0; int B = H1; int C = H2; int D = H3; int E = H4;

  		//主循环 计算摘要
  		for(int t = 0; t < 80; t++){
  			int K = 0, F = 0;
  			
  			if(0 <= t && t <= 19){
  				F = (B & C) | ((~ B) & D);
  				K = 0x5A827999;
  			}
	        else if (20 <= t && t <= 39){
	            F = B ^ C ^ D;
	            K = 0x6ED9EBA1;
	        }
	        else if (40 <= t && t <= 59){
	            F = (B & C) | (B & D) | (C & D) ;
	            K = 0x8F1BBCDC;
	        }
	        else if (60 <= t && t <= 79){
	            F = B ^ C ^ D;
	            K = 0xCA62C1D6;
	        }

	        int Temp = leftrotate(A, 5) + F + E + w[t] + K;
	        E = D;
	        D = C;
	        C = leftrotate(B, 30);
	        B = A;
	        A = Temp;
	  		}
			H0 = H0 + A;
			H1 = H1 + B;
			H2 = H2 + C;
			H3 = H3 + D;
			H4 = H4 + E;
    }
    
    //160 bit number:
    String res = "" + toHexString(H0) + toHexString(H1) + toHexString(H2) +
                      toHexString(H3) + toHexString(H4);
    return res;
  }

  /**
  *     byte[] one = [xxxxxxxx, xxxxxxxx, ...]
  *     int    two =  yyyyyyyy * 4  (16 bit)
  *     byte[] res = [xxxxxxxx, xxxxxxxx, ..., yyyyyyyy, ..., yyyyyyyy]
  */
  public final byte[] add(byte[] one, int two){
  	byte[] temp = new byte[4];
  	temp[0] = (byte) (two >> 24);
  	temp[1] = (byte) (two >> 16);
  	temp[2] = (byte) (two >> 8);
  	temp[3] = (byte) (two);
  	return add(one,temp);
  }


  /**
  *     byte[] one = [xxxxxxxx, xxxxxxxx, ...]
  *     byte   two =  yyyyyyyy   (8 bit)
  *     byte[] res = [xxxxxxxx, xxxxxxxx, ..., yyyyyyyy]
  */
  public final byte[] add(byte[] one, byte two){
  	byte[] temp = new byte[1];
  	temp[0] = two;
  	return add(one,temp);
  }

  /**
  *     byte[] one = [xxxxxxxx, xxxxxxxx, ...]
  *     long   two =  yyyyyyyy * 8  (64 bit)
  *     byte[] res = [xxxxxxxx, xxxxxxxx, ..., yyyyyyyy*8]
  */
  public final byte[] add(byte[] one, long two){
  	byte[] temp = new byte[8];
  	temp[0] = (byte) (two >> 56);
  	temp[1] = (byte) (two >> 48);
  	temp[2] = (byte) (two >> 40);
  	temp[3] = (byte) (two >> 32);
  	temp[4] = (byte) (two >> 24);
  	temp[5] = (byte) (two >> 16);
  	temp[6] = (byte) (two >> 8);
  	temp[7] = (byte) (two);
  	return add(one,temp);
  }

  //组合两个字节数组为一个字节数组
  public final byte[] add(byte[] one, byte[] two){
    byte[] combined = new byte[one.length + two.length];
    System.arraycopy(one,0,combined,0,one.length);
    System.arraycopy(two,0,combined,one.length,two.length);
    return combined;
  }

  //x向左循环移位count位  注意移位操作值针对int long
  private final int leftrotate(int x, int count){
	  return ((x << count) | (x >>> (32 - count)));
  }

  private String toHexString(long two) {
    byte[] temp = new byte[4];
    temp[0] = (byte) (two >>> 24);
    temp[1] = (byte) (two >>> 16);
    temp[2] = (byte) (two >>> 8);
    temp[3] = (byte) (two);
    return toHexString(temp);
  }

  private String toHexString(int two) {
    byte[] temp = new byte[4];
    temp[0] = (byte) (two >>> 24);
    temp[1] = (byte) (two >>> 16);
    temp[2] = (byte) (two >>> 8);
    temp[3] = (byte) (two);
    return toHexString(temp);
  }

  /***
  *
  */
  private static String toHexString(byte[] b)
  {
    final String hexChar = "0123456789ABCDEF";

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < b.length; i++)
    {
      sb.append(hexChar.charAt((b[i] >> 4) & 0x0f));
      sb.append(hexChar.charAt(b[i] & 0x0f));
    }
    return sb.toString();
  }


  public static void main(String[] args)
  {
    SHA1 sha = new SHA1();
	String res = sha.run("廖飞强");
	System.out.println("摘要内容:" +res);
	System.out.println("摘要长度:" +res.length()*4+"位");
  }
}
