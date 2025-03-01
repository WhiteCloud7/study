package chatMax;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

// 定义一个客户端类，用于与服务器进行通信
public class Client {
    // 用于输入服务器地址的文本框
    private JTextPane AddServer;
    // 用于输入服务器端口号的文本框
    private JTextPane port;
    // 客户端的主窗口
    private JFrame frame;
    // 发送消息的按钮
    private JButton send;
    // 用于输入要发送消息的文本区域
    private JTextArea message;
    // 显示聊天记录的文本区域
    private JTextArea chatPanel;
    // 客户端的套接字，用于与服务器建立连接
    private Socket socket;
    // 服务器的端口号，默认值为 8080
    private int serverPort = 8080;
    
    private JLabel wordCountLabel;
    private JScrollPane scrollPane_1;

    // 程序的入口方法，使用 EventQueue.invokeLater 确保 GUI 在事件调度线程中创建和显示
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // 创建 Client 类的实例
                Client window = new Client();
                // 显示客户端主窗口
                window.frame.setVisible(true);
            } catch (Exception e) {
                // 若出现异常，打印异常堆栈信息
                e.printStackTrace();
            }
        });
    }

    // 构造函数，调用 initialize 方法进行初始化
    public Client() {
        initialize();
    }

    // 初始化客户端界面，此部分省略 UI 相关注释
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle("客户端");

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 686, 1);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("服务器地址");
        lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel.setBounds(23, 10, 80, 26);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("端口号");
        lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel_1.setBounds(303, 11, 80, 26);
        frame.getContentPane().add(lblNewLabel_1);

        AddServer = new JTextPane();
        AddServer.setBounds(137, 11, 131, 21);
        frame.getContentPane().add(AddServer);

        port = new JTextPane();
        port.setBounds(369, 15, 125, 21);
        frame.getContentPane().add(port);

        JButton link = new JButton("连接");
        link.addActionListener(e -> connect());
        link.setFont(new Font("宋体", Font.PLAIN, 16));
        link.setBounds(545, 13, 93, 23);
        frame.getContentPane().add(link);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(23, 46, 622, 284);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 622, 284);
        panel_1.add(scrollPane);

        chatPanel = new JTextArea();
        chatPanel.setEditable(false);
        scrollPane.setViewportView(chatPanel);
        chatPanel.setText("输入端口号及地址以连接服务器！");

        wordCountLabel = new JLabel("字数: 0");
        wordCountLabel.setBounds(556, 365, 100, 20);
        frame.getContentPane().add(wordCountLabel);
        
        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(23, 338, 522, 24);
        frame.getContentPane().add(scrollPane_1);

        message = new JTextArea();
        scrollPane_1.setViewportView(message);
        message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateWordCountAndSize();
            }
        });
        
        send = new JButton("发送消息");
        send.setBounds(556, 340, 89, 23);
        frame.getContentPane().add(send);
        send.addActionListener(e -> sendMessage());
    }

    // 发送消息到服务器的方法
    private void sendMessage() {
        // 检查套接字是否不为空且已连接到服务器
        if (socket != null && socket.isConnected()) {
            try {
                // 创建一个 BufferedWriter 用于向服务器发送消息
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                // 获取用户输入的原始消息
                String originSentMessage = message.getText();
                // 将原始消息中的换行符替换为特定字符串 &@#，方便在网络传输中处理
                String sentMessage = originSentMessage.replaceAll("\n", "&@#");
                // 将原始消息中的换行符替换为特定格式，用于在聊天面板中显示
                String sentMessageShowPanel = originSentMessage.replaceAll("\n", "\n               ");
                if(sentMessage.length() <= 1000) {
                	// 在聊天面板中添加客户端发送的消息
                	chatPanel.append("客户端：" + sentMessageShowPanel + "\n");
                	// 向服务器发送处理后的消息
                	bw.write(sentMessage);
                	// 写入换行符，表示消息结束
                	bw.newLine();
                	// 刷新缓冲区，确保消息发送出去
                	bw.flush();
                }else {
                	JOptionPane.showMessageDialog(null, "输入字数不能超过1000，请减少字数或分次发送！","提示",JOptionPane.ERROR_MESSAGE);
                }
                // 清空输入框
                message.setText("");
            } catch (IOException e) {
                // 若发送消息时出现异常，打印异常信息并在聊天面板中提示
                e.printStackTrace();
                chatPanel.append("\n发送消息时发生错误：" + e.getMessage());
            }
        } else {
            // 若套接字未连接到服务器，在聊天面板中提示用户先连接
            chatPanel.append("\n未连接到服务器，请先连接！");
        }
    }

    // 连接到服务器的方法
    private void connect() {
        try {
            // 将用户输入的端口号转换为整数
            int clientPort = Integer.parseInt(port.getText());
            // 获取用户输入的服务器地址
            String serverAdd = AddServer.getText();
            // 在聊天面板中显示准备连接服务器的信息
            chatPanel.setText("准备连接服务器......");
            // 检查服务器地址是否为空
            if (serverAdd != "") {
                // 创建一个 Socket 对象，尝试连接到指定的服务器地址和端口
                socket = new Socket(serverAdd, clientPort);
                // 在聊天面板中显示连接成功的信息
                chatPanel.append("\n连接服务器成功！\n");
                // 启动一个新线程来接收服务器发送的消息
                new Thread(() -> {
                    try {
                        // 创建一个 BufferedReader 用于读取服务器发送的消息
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String serverMessage;
                        // 进入一个无限循环，持续接收服务器消息
                        while (true) {
                            // 读取服务器发送的一行消息
                            if ((serverMessage = br.readLine()) != null) {
                                // 将消息中的特定字符串 &@# 替换为换行符
                                serverMessage = serverMessage.replaceAll("&@#", "\n");
                                // 将消息中的换行符替换为特定格式，用于在聊天面板中显示
                                serverMessage = serverMessage.replaceAll("\n", "\n               ");
                                // 在聊天面板中添加服务器发送的消息
                                chatPanel.append("服务器：" + serverMessage + "\n");
                                Thread.sleep(100); // 由于只是两个人的聊天器，故不需要那么频繁接收
                            }
                        }
                    } catch (IOException e) {
                        // 若接收服务器消息时出现异常，且套接字未关闭，在聊天面板中提示
                        if (socket != null && !socket.isClosed()) {
                            chatPanel.append("\n接收服务器消息时发生错误：" + e.getMessage());
                        }
                    } catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
                }).start();
            } else {
                // 若服务器地址为空，在聊天面板中提示连接失败
                chatPanel.append("\n连接失败，请检查端口或地址是否正确！");
            }
        } catch (NumberFormatException ex) {
            // 若端口号输入无效，在聊天面板中提示用户输入有效的端口号
            chatPanel.append("\n端口号输入无效，请输入有效的端口号！");
        } catch (IOException ex) {
            // 若连接服务器时出现异常，在聊天面板中提示连接错误信息
            chatPanel.append("\n连接服务器时发生错误：" + ex.getMessage());
        }
    }
    
    private void updateWordCountAndSize() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    // 获取输入的文本
                    String text = message.getText();
                    // 更新字数显示
                    wordCountLabel.setText("字数: " + text.length());

                    // 强制重新绘制以确保实时刷新
                    wordCountLabel.repaint();

                    // 计算输入的行数
                    int lineCount = message.getLineCount();
                    int initialHeight = 20;
                    int maxHeight = initialHeight * 4;

                    int newHeight = initialHeight * lineCount;
                    if (newHeight > maxHeight) {
                        newHeight = maxHeight;
                    }
                    java.awt.Rectangle bounds = scrollPane_1.getBounds();
                    if (bounds.height != newHeight) {
                        bounds.height = newHeight;
                        scrollPane_1.setBounds(bounds);
                        frame.revalidate();
                        frame.repaint();
                    }
                    Thread.sleep(100); // 设置线程休眠100毫秒，防止CPU过高
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}