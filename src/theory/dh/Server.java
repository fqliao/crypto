package theory.dh;
/*
 * 服务器 A
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.SecureRandom;

import javax.swing.*;

public class Server extends JFrame implements ActionListener{
	private final int WIDTH = 400;
	private final int HEIGHT = 300;
	
	private JTextArea 	resultArea;
	private JTextArea 	input;
	private JButton		play;
	private JLabel pubKey;
	
	public Server(){
		//设置JFrame
		super("DH-A");
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
		pubKey = new JLabel("");
		input = new JTextArea(8, 20);
		resultArea = new JTextArea(8, 20);
		resultArea.setMargin(new Insets(5, 5, 5, 5));
		input.setMargin(new Insets(5, 5, 5, 5));
	
		play = new JButton("共享密钥");
		play.addActionListener(this);
		
		JPanel buttonPane = new JPanel();
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
			//1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
			ServerSocket serverSocket=new ServerSocket(8888);
			System.out.println("***服务器即将启动，等待客户端的连接***");
			Socket socket = serverSocket.accept();

			//2.获取输入流，读取客户端信息
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String message = "";
			BigInteger p = new BigInteger("0");
			BigInteger g = new BigInteger("0");
			BigInteger A = new BigInteger("0");
			while((message=br.readLine())!=null){//接收客户端传送的公开参数p,g,公钥A 
				String[] m = message.split(";");
				p = new BigInteger(m[0]);
				g = new BigInteger(m[1]);
				A = new BigInteger(m[2]);
				System.out.println("p:"+p+" g:"+g);
			}
			socket.shutdownInput();
			pubKey.setText("参数 ：p="+p+" g="+g);
			
			//生成随机私钥x
			BigInteger x = BigInteger.probablePrime(18, new SecureRandom());
			input.setText(x+"");
			//计算公钥B
			BigInteger B = g.modPow(x, p);
			System.out.println("A的公钥："+A+" B的公钥："+B);
			//共享生成的密钥k
			BigInteger k = A.modPow(x, p);
			resultArea.setText(k+"");
			System.out.println("共享密钥bak="+k);
			//3.获取输出流，向服务器端发送信息
			OutputStream os=socket.getOutputStream();//字节输出流
			PrintWriter pw=new PrintWriter(os);//将输出流包装为打印流
			pw.write(B+"");
			pw.flush();
			socket.shutdownOutput();//关闭输出流
			
			//关闭资源
			br.close();
			isr.close();
			is.close();
			pw.close();
			os.close();
			socket.close();
			serverSocket.close();	
		} catch (IOException ee) {
			ee.printStackTrace();
		}	
	}
	public static void main(String[] args) {
		Server server = new Server();
	}
}