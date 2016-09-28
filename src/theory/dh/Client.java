package theory.dh;
/*
 * 客户端 B
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.swing.*;

public class Client extends JFrame implements ActionListener{
	private final int WIDTH = 400;
	private final int HEIGHT = 300;
	
	private JTextArea 	resultArea;
	private JTextArea 	input;
	private JButton		play;
	private JLabel pubKey;
	private JPanel buttonPane;
	
	private BigInteger p = new BigInteger("0");
	private BigInteger g = new BigInteger("0");
	private BigInteger one = new BigInteger("1");
	private BigInteger zero = new BigInteger("0");
	
	public Client(){
		//设置JFrame
		super("DH-B");
		int windowWidth = WIDTH; // 获得窗口宽
		int windowHeight = HEIGHT; // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		this.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setResizable(false);
		setVisible(true);
		setLayout(new GridLayout(3, 1));
		
		//DH的公开参数
		pubKey = new JLabel();
		
		input = new JTextArea(8, 20);
		resultArea = new JTextArea(8, 20);
		resultArea.setMargin(new Insets(5, 5, 5, 5));
		input.setMargin(new Insets(5, 5, 5, 5));
	
		play = new JButton("共享密钥");
		play.addActionListener(this);
		
		buttonPane = new JPanel();
		buttonPane.add(play);
		buttonPane.add(pubKey);
		
		add(input);
		add(buttonPane);
		add(resultArea);
		pack();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			//1.创建客户端Socket，指定服务器地址和端口
			Socket socket=new Socket("localhost", 8888);
			//2 获取输出流
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			//产生一个大素数p
			p = BigInteger.probablePrime(20, new SecureRandom());
			//求大素数p的一个原根g
			g = getPrimitive(p);
			pubKey.setText("参数 ：p="+p+" g="+g);
			
			//生成随机私钥x
			BigInteger x = BigInteger.probablePrime(18, new SecureRandom());
			input.setText(x+"");

			//计算公钥A
			BigInteger A = g.modPow(x, p);
			System.out.println("p:"+p+" g:"+g);
			String message = p+";"+g+";"+A;
			pw.write(message);
			pw.flush();//调用flush将缓冲输出
			socket.shutdownOutput();
			//3.获取输入流，并读取服务器端的响应信息
			InputStream is=socket.getInputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			String info = "";
			BigInteger B = new BigInteger("0");
			while((info = br.readLine())!=null){//接收服务器端传送的公钥B
				B = new BigInteger(info);
			}
			System.out.println("A的公钥："+A+" B的公钥："+B);
			//共享生成的密钥
			BigInteger k = B.modPow(x, p);
			resultArea.setText(k+"");
			System.out.println("共享密钥abk="+k);
		
			//4.关闭资源
			br.close();
			is.close();
			pw.close();
			os.close();
			socket.close();
		} catch (UnknownHostException ee) {
			ee.printStackTrace();
		} catch (IOException ee) {
			ee.printStackTrace();
		}
		
	}
	//判断一个数是否一个大素数的原根
	public  boolean isPrimitive(BigInteger p,BigInteger n,BigInteger g){
		boolean flag = true;//是否是原根 默认是
		BigInteger key = one;
		BigInteger num = new BigInteger(n + "");
		ArrayList<BigInteger> list = new ArrayList<BigInteger>();
		while (num.compareTo(one) == 1) {
			for (BigInteger i = new BigInteger("2"); i.compareTo(num) != 1; i=i.add(one)) {// 从2开始除到本身，用于判断素数
				if (num.mod(i).compareTo(zero) == 0) { // 找到素数因子
					key = i;
					list.add(key); // 保存这个素数因子
					break;
				}
			}
			num = num.divide(key); // 继续分解除以素数因子得到的商
		}
		for (int i = 0; i < list.size(); i++) {
			if(g.modPow(list.get(i),p).compareTo(one)==0 && list.get(i).compareTo(n)!=0){
				flag = false;//不是
				break;//中断
			};
		}
		return flag;

	}
	//产生一个随机数 找一个指定大素数的原根
	public BigInteger getPrimitive(BigInteger p){
		System.out.println("p="+p);
		BigInteger n = p.subtract(one);
		boolean f = true;//true 表示不是原根 默认不是原根
		BigInteger g = new BigInteger("0");
		while(f){
			g = BigInteger.probablePrime(8, new SecureRandom());
			boolean flag = isPrimitive(p,n,g);//返回是否是原根的结果 true表示是 false不是
			if(flag){
				System.out.println("g="+g);
				f = false;
			}
		}
		return g;
	}

	public static void main(String[] args) {
		Client client = new Client();
	}
}