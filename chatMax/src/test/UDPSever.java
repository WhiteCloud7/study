package test;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UDPSever {
    private int monitorPort = 8080;
    private JFrame frame;
    private JTextArea chatPanel;
    private JTextArea message;
    private JButton send;
    private DatagramSocket serverSocket;
    private InetAddress serverAdd;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UDPSever window = new UDPSever();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UDPSever() {
        initialize();
        reciveMessage();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 686, 1);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(23, 11, 622, 319);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 622, 319);
        panel_1.add(scrollPane);

        chatPanel = new JTextArea();
        chatPanel.setEditable(false);
        scrollPane.setViewportView(chatPanel);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(23, 338, 522, 24);
        frame.getContentPane().add(scrollPane_1);

        message = new JTextArea();
        scrollPane_1.setViewportView(message);

        send = new JButton("发送消息");
        send.setBounds(556, 340, 89, 23);
        frame.getContentPane().add(send);
        send.addActionListener(e-> { sendMessage(); });
    }
    private void reciveMessage(){
    	try {
    		chatPanel.setText("服务器已就绪，等待客户端连接......");
			serverSocket = new DatagramSocket();
			chatPanel.setText("客户端已连接!");
			serverAdd = InetAddress.getByName("127.0.0.1");
			new Thread(() ->{
				byte []recivedMessage = new byte[1024];
				while(true) {
					DatagramPacket recivePocket = new DatagramPacket(recivedMessage, recivedMessage.length, serverAdd, monitorPort);
					try {
						serverSocket.receive(recivePocket);
						String reciveMessage =(recivePocket.getData()).toString();
						chatPanel.append("客户端"+reciveMessage+"\n");
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}).start();;
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
    	
    }
    
    private void sendMessage(){
    	byte []sentMessage = new byte[1024];
		DatagramPacket sendPocket = new DatagramPacket(sentMessage, sentMessage.length,serverAdd,monitorPort);
		try {
			serverSocket.send(sendPocket);
			String sendMessage =(sendPocket.getData()).toString();
			chatPanel.append("客户端"+sendMessage+"\n");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    }
}