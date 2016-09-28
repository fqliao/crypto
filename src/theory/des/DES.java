package theory.des;
public class DES {

	//初始置换表:64——>64
    private static final byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };
    //尾置换表:64——>64
    private static final byte[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };
    //扩充置换表：32——>48 Feistel网络使用
    private static final byte[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };
    //S盒：48bits被分为8个6bits，每个6bits转换为4bits,S盒的作用是获得高度非线性!
    private static final byte[][] S = {{
            14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13
    }, {
            15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9
    }, {
            10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12
    }, {
            7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14
    }, {
            2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3
    }, {
            12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13
    }, {
            4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12
    }, {
            13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
    }};
    //P置换表：32——>32  S盒变换后的置换 为了混淆S盒的输出结果
    private static final byte[] P = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };
    //缩减变换PC1表：64——>56  
    private static final byte[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };
    //缩减变换PC2表：56——>48
    private static final byte[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };
    //密钥循环左移变换，第1，2，9，16，循环左移一位，其他左移两位 用于生产16轮的子密钥
    private static final byte[] rotations = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };
    
    private static long IP(long src) {
        return permute(IP, 64, src);
    } 
    private static long FP(long src) {
        return permute(FP, 64, src);
    }
    private static long E(int src) {
        return permute(E, 32, src & 0xFFFFFFFFL);
    }
    private static int P(int src) {
        return (int) permute(P, 32, src & 0xFFFFFFFFL);
    } 
    private static long PC1(long src) {
        return permute(PC1, 64, src);
    }
    private static long PC2(long src) {
        return permute(PC2, 56, src);
    }
    //初始向量IV
    private static long IV;
    public static long getIv() {
        return IV;
    }
    public static void setIv(long iv) {
        IV = iv;
    }
    
    //根据置换表进行替换，输入表，表的长度，和long型值
    private static long permute(byte[] table, int srcWidth, long src) {
        long dst = 0;
        for (int i = 0; i < table.length; i++) {
            int srcPos = srcWidth - table[i];
            dst = (dst << 1) | (src >> srcPos & 0x01);
        }
        return dst;
    }

    //S盒置换
    private static byte S(int boxNumber, byte src) {
        src = (byte) (src & 0x20 | ((src & 0x01) << 4) | ((src & 0x1E) >> 1));
        return S[boxNumber - 1][src];
    }

    //将字节数组中的每8字节数组的值转换为64bit（long的类型为8字节 64bit）长的值，如果不够8字节，后面补0
    private static long getLongFromBytes(byte[] ba, int offset) {
        long l = 0;
        for (int i = 0; i < 8; i++) {
            byte value;
            if ((offset + i) < ba.length) { 
                value = ba[offset + i];
            } else {
                value = 0;
            }
            l = l << 8 | (value & 0xFFL);
        }
        return l;
    }

    //将64bit的long型值转为8字节数组
    private static void getBytesFromLong(byte[] ba, int offset, long l) {
        for (int i = 7; i > -1; i--) {
            if ((offset + i) < ba.length) {
                ba[offset + i] = (byte) (l & 0xFF);
                l = l >> 8;
            } else {
                break;
            }
        }
    }

    //Feistel函数 DES的核心！
    private static int feistel(int r, /* 48 bits */ long subkey) {
        // 1.扩充变换
        long e = E(r);
        // 2.密钥混合
        long x = e ^ subkey;
        // 3.S盒置换
        int dst = 0;
        for (int i = 0; i < 8; i++) {
            dst >>>= 4;
            int s = S(8 - i, (byte) (x & 0x3F));
            dst |= s << 28;
            x >>= 6;
        }
        // 4.置换P
        return P(dst);
    }

    //由64bit的密钥种子产生16个48bit的子密钥
    private static long[] createSubkeys(/* 64 bits */ long key) {
        long subkeys[] = new long[16];
        // PC1置换 64——>56
        key = PC1(key);
        //56位长的密钥分为两个28位长的子密钥
        int c = (int) (key >> 28);
        int d = (int) (key & 0x0FFFFFFF);
        //最移位+连接
        for (int i = 0; i < 16; i++) {
            // rotate the 28-bit values
            if (rotations[i] == 1) {
            	//循环左移一位
                c = ((c << 1) & 0x0FFFFFFF) | (c >> 27); 
                d = ((d << 1) & 0x0FFFFFFF) | (d >> 27);
            } else {
            	//循环左移2位
                c = ((c << 2) & 0x0FFFFFFF) | (c >> 26);
                d = ((d << 2) & 0x0FFFFFFF) | (d >> 26);
            }
            // 连接连个28位
            long cd = (c & 0xFFFFFFFFL) << 28 | (d & 0xFFFFFFFFL);
            // PC2置换：56——>48
            subkeys[i] = PC2(cd);
        }
        return subkeys; 
    }

    //加密64bit明文为64bit密文
    public static long encryptBlock(long m, /* 64 bits */ long key) {
        // 产生16个子密钥
        long subkeys[] = createSubkeys(key);
        // 初始置换IP
        long ip = IP(m);
        //将64bit的数据分为左右两个32bit的数据
        int l = (int) (ip >> 32);
        int r = (int) (ip & 0xFFFFFFFFL);
        // 16轮函数
        for (int i = 0; i < 16; i++) {
        	//记录之前的左边
            int previous_l = l;
            //左边=之前的右边
            l = r;
            //计算右边
            r = previous_l ^ feistel(r, subkeys[i]);
        }
        //将左右32bit交换 对特殊轮的处理
        long rl = (r & 0xFFFFFFFFL) << 32 | (l & 0xFFFFFFFFL);
        // 尾置换，获得密文
        long fp = FP(rl);
        return fp;
    }
    //字符转换为ASCll码值（十进制）
    private static int charToNibble(char c) {
        if (c >= '0' && c <= '9') {
            return (c - '0');
        } else if (c >= 'a' && c <= 'f') {
            return (10 + c - 'a');
        } else if (c >= 'A' && c <= 'F') {
            return (10 + c - 'A');
        } else {
            return 0;
        }
    }
    //将两位16进制数转换为10进制
    private static byte[] parseBytes(String s) {
        s = s.replace(" ", "");
        byte[] ba = new byte[s.length() / 2];
        if (s.length() % 2 > 0) {
            s = s + '0';
        }
        for (int i = 0; i < s.length(); i += 2) {
        	int charToNibble = charToNibble(s.charAt(i));//将单个位上的数字或字母转为十进制
        	int m = charToNibble << 4; //第一位16进制数乘以16的1次方
        	int n = charToNibble(s.charAt(i + 1)); //第二位16进制数乘以16的0次方
        	int j = m | n;  //这个的按位或是加法功能
            ba[i / 2] = (byte) (j);
        }
        return ba;
    }
    
    //字节数组转16进制
    private static String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
        	//X:表示以16进制形式输出  02:表示不足两位，前面补0输出；出过两位，不影响
            sb.append(String.format("%02X", bytes[i]));
        }
        return sb.toString();
    }
    
    //将字符串转为16进制
    public static String convertStringToHex(String str){
		char[] chars = str.toCharArray();
		StringBuffer hex = new StringBuffer();
		for(int i = 0; i < chars.length; i++){
			hex.append(Integer.toHexString((int)chars[i]));
		}
		return hex.toString();
	 }
    
   //将16进制转为字符串 lfq
   public static String convertHextoString(String hex){
	   byte[] parseBytes = parseBytes(hex);
	   StringBuffer str = new StringBuffer();
	   for (int i = 0; i < parseBytes.length; i++) {
		str = str.append((char)parseBytes[i]);
	}
	   return str.toString();
   } 
    //CBC模式加密
    public static String encryptCBC(String message, String key) {
    	//lfq
    	int j = message.length()%8;
    	if(j!=0){
    		int k = 8-j;//不满8-j个空格，补齐
    		for (int i = 0; i < k; i++) {
				message+=" ";
			}
    	}
    	String m = convertStringToHex(message);
    	byte[] mbytes = parseBytes(m);//将m变为了十进制表示
    	String keyHex = convertStringToHex(key);//口令
    	byte[] keys = parseBytes(keyHex);//将key变为了十进制表示
    	
        byte[] cbytes = new byte[mbytes.length];
        long k = getLongFromBytes(keys, 0);
        long previousCipherBlock = IV;
        for (int i = 0; i < mbytes.length; i += 8) {
        	//获得分组8字节
            long messageBlock = getLongFromBytes(mbytes, i);
            //CBC模式加密时第一个明文块异或IV后再加密，后面是明文块异或前一个密文块再加密
            long cipherBlock = encryptBlock(messageBlock ^ previousCipherBlock, k);
            //将加密后的密文块组装的密文消息中
            getBytesFromLong(cbytes, i, cipherBlock);
            //更新previousCipherBlock
            previousCipherBlock = cipherBlock;
        }
        return hex(cbytes);
    }
    
    //解密64bit密文为64bit明文
    public static long decryptBlock(long c, /* 64 bits */ long key) {
        //产生16轮子密钥
        long[] subkeys = createSubkeys(key);
        long ip = IP(c);
        //将64bit分为左右32bit
        int l = (int) (ip >> 32);
        int r = (int) (ip & 0xFFFFFFFFL);
        //16轮feistel网络迭代，注意子密钥顺序与加密相反
        for (int i = 15; i > -1; i--) {
            int previous_l = l;
            l = r;
            r = previous_l ^ feistel(r, subkeys[i]);
        }
        long rl = (r & 0xFFFFFFFFL) << 32 | (l & 0xFFFFFFFFL);
        long fp = FP(rl);
        return fp;
    }
    
    //CBC mode 解密
    public static String decryptCBC(String cipherText, String key) {
        byte[] cbytes = parseBytes(cipherText);//将m变为了十进制表示
    	byte[] message = new byte[cbytes.length];//准备解密的明文空间
    	String keyHex = convertStringToHex(key);//口令
        byte[] keys = parseBytes(keyHex);//将key变为了十进制表示
    	
        long k = getLongFromBytes(keys, 0);
        long previousCipherBlock = IV;
        for (int i = 0; i < cbytes.length; i += 8) {
            //分块解密，每一块大小为8字节
            long cipherBlock = getLongFromBytes(cbytes, i);
            long messageBlock = decryptBlock(cipherBlock, k);
            messageBlock = messageBlock ^ previousCipherBlock;
            //将解密出的明文块组装为一个明文消息
            getBytesFromLong(message, i, messageBlock);
            //更新previousCipherBlock
            previousCipherBlock = cipherBlock;
        }
        return convertHextoString(hex(message));
    }
    
    //解决两个问题：1 不是8字节的倍数的数据可以完整加解密 2优化函数封装
    public static void main(String[] args) {
    	String m = "hello world";//明文     
    	String key = "123456";//口令
    	setIv(1482);
    	//加密
    	String c = encryptCBC(m, key);//加密结果16进制表示
    	System.out.println("Encryption result:  " +c);
    	//解密
    	String decResult = decryptCBC(c,key);
    	System.out.println("Decryption result:  " + decResult);
    }
}
