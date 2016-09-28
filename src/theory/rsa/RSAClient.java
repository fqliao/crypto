package theory.rsa;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;



public class RSAClient extends JFrame {
	
	private final int WIDTH = 400;
	private final int HEIGHT = 300;
	
	private JTextArea 	resultArea;
	private JTextArea 	input;
	private JButton		decrypt;
	private JButton		encrypt;
	
	private RSA rsa;
	
	public RSAClient(){
		//设置JFrame
		super("RSA");
		int windowWidth = WIDTH; // 获得窗口宽
		int windowHeight = HEIGHT; // 获得窗口高
		//设置输入对话框，接收输入比特位数的参数
		boolean flag = false;
		String bitLength = "";
		while(flag == false){
			bitLength = JOptionPane.showInputDialog(this, "请输入比特位数");
			if(bitLength == null || "".equals(bitLength)){
				JOptionPane.showMessageDialog(this, "请输入一个合法的整数值!");
			}else{
				flag = true;
				rsa = new RSA(Integer.parseInt(bitLength));
			}
		}
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
		
		//获得公钥
		JLabel pubKey = new JLabel("公钥 "+rsa.getPublicKey().toString());
		
		input = new JTextArea(8, 20);
		resultArea = new JTextArea(8, 20);
		resultArea.setMargin(new Insets(5, 5, 5, 5));
		input.setMargin(new Insets(5, 5, 5, 5));
	
		
		encrypt = new JButton("加密");
		decrypt = new JButton("解密");
		decrypt.addActionListener(new ButtonListener());
		encrypt.addActionListener(new ButtonListener());
		
		JPanel buttonPane = new JPanel();
		buttonPane.add(encrypt);
		buttonPane.add(decrypt);
		buttonPane.add(pubKey);
		
		add(input);
		add(buttonPane);
		add(resultArea);
		pack();
	}
	
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			String result;
			String m = input.getText().trim();
			if(m == null || "".equals(m)){
					m = "0";
			}
			if(e.getSource() == encrypt){
				result = rsa.encrypt(m);
			}else{
				result = rsa.decrypt(m);
			}
			resultArea.setText(result);
		}
	}
	
	public static void main(String[] args) {
		RSAClient rsa = new RSAClient();
	}
	
}
