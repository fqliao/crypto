package theory.sha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class SHA256 extends JFrame implements ActionListener
{
	private JLabel jmessage;
	private JLabel jdigest;
	private JButton  jb;
	private JTextField message;
	private JTextField digest;
	private JPanel jp,jp2;
	public SHA256(String title){
		super(title);
		jmessage = new JLabel("消息 ");
		jdigest = new JLabel("摘要 ");
		jb = new JButton("生成摘要");
	    jb.addActionListener(this);
		message = new JTextField(10);
		digest = new JTextField(10);
		jp = new JPanel();
		jp2 = new JPanel();
		jp.setLayout(new GridLayout(4, 8));
		jp.add(jmessage);
		jp.add(message);
		jp.add(jdigest);
		jp.add(digest);
		jp2.add(jb);
		this.add(jp, BorderLayout.CENTER);
		this.add(jp2, BorderLayout.SOUTH);
		this.setSize(700, 200);
		this.setVisible(true);
		int windowWidth = this.getWidth(); // 获得窗口宽
		int windowHeight = this.getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		this.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()== jb) {
			String content = message.getText().trim();//获得输入框的消息内容
			String res = this.run(content);//计算摘要值
			int len = res.length()*4;
			res = res + "    ("+len+"位)";
			digest.setText(res);//设置摘要值
			System.out.println("message:"+content);
			System.out.println("摘要内容:" +res);
		}
		
	}
    public final String run(String message){
	    int H0 = 0x6a09e667;
	    int H1 = 0xbb67ae85;
	    int H2 = 0x3c6ef372;
	    int H3 = 0xa54ff53a;
	    int H4 = 0x510e527f;
	    int H5 = 0x9b05688c;
	    int H6 = 0x1f83d9ab;
	    int H7 = 0x5be0cd19;

    int[] K =
	  {0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5, 
	  0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 
	  0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, 
	  0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, 
	  0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85, 
	  0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070, 
	  0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 
	  0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2};
    
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
      int[] w = new int[64];
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
               (msg[i + (j * 4) + 2] <<  8) + 
               (msg[i + (j * 4) + 3] & 0xff);
      }

      /*
      w[16]       = (w[13] xor w[8] xor w[2] xor w[0]) leftrotate 1
      w[17]       = (w[14] xor w[9] xor w[3] xor w[1]) leftrotate 1
      ...
      w[79]       = (w[76] xor w[71] xor w[65] xor w[63]) leftrotate 1
      */
    //计算16-63的字
      for (int t = 16; t < 64; t++){
        int s0 = rightrotate(w[t-15], 7) ^ rightrotate(w[t-15], 18) ^ (w[t-15] >>> 3);
        int s1 = rightrotate(w[t-2], 17) ^ rightrotate(w[t-2], 19) ^ (w[t-2] >>> 10);
        w[t] = w[t-16] + s0 + w[t-7] + s1;
      }
      int A = H0;
      int B = H1;
      int C = H2;
      int D = H3;
      int E = H4;
      int F = H5;
      int G = H6;
      int H = H7;

      //主循环 计算摘要
      for(int t = 0; t < 64; t++){
        int s1 = rightrotate(E, 6) ^ rightrotate(E, 11) ^ rightrotate(E, 25);
        int ch = (E & F) ^ ((~E) & G);
        int temp1 = H + s1 + ch + K[t] + w[t];

        int s0 = rightrotate(A, 2) ^ rightrotate(A, 13) ^ rightrotate(A, 22);
        int maj = (A & B) ^ (A & C) ^ (B & C);
        int temp2 = s0 + maj;
        H = G;
        G = F;
        F = E;
        E = D + temp1;
        D = C;
        C = B;
        B = A;
        A = temp1 + temp2;
      }
      H0 = H0 + A;
      H1 = H1 + B;
      H2 = H2 + C;
      H3 = H3 + D;
      H4 = H4 + E;
      H5 = H5 + F;
      H6 = H6 + G;
      H7 = H7 + H;
    }
    
    //256 bit number:
    String res = "" + toHexString(H0) + toHexString(H1) + toHexString(H2) +
                      toHexString(H3) + toHexString(H4) + toHexString(H5) +
                      toHexString(H6) + toHexString(H7);
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

  /**
  *     byte[] one = [xxxxxxxx, xxxxxxxx, ...]
  *     byte[]  two = [yyyyyyyy, yyyyyyyy, ...]
  *     byte[] res = [xxxxxxxx, xxxxxxxx, ..., yyyyyyyy, yyyyyyyy, ...]
  */
  public final byte[] add(byte[] one, byte[] two){
    byte[] combined = new byte[one.length + two.length];

    System.arraycopy(one,0,combined,0,one.length);
    System.arraycopy(two,0,combined,one.length,two.length);

    return combined;
  }

  private final int rightrotate(int x, int count){
    return (((x >>> count)) | (x << (32 - count)));
  }

  private String toHexString(int[] two) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < two.length; i++) {
      sb.append(toHexString(two[i]));
    }
    return sb.toString();
  }
  
  private String toHexString(int two) {
    byte[] temp = new byte[4];
    temp[0] = (byte) (two >>> 24);
    temp[1] = (byte) (two >>> 16);
    temp[2] = (byte) (two >>> 8);
    temp[3] = (byte) (two);
    return toHexString(temp);
  }

  
  private static String toHexString(byte[] b)
  {
    final String hexChar = "0123456789ABCDEF";
    //final String hexChar = "0123456789abcdef";

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
	SHA256 sha = new SHA256("SHA256");
  }
  
}
