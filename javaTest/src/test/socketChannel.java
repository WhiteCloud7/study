package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class socketChannel {
    public socketChannel() {
        SocketChannel socketChannel = null;
        Scanner scanner = null;
        try {
            // 获取通道
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
            // 切换成非阻塞模式
            socketChannel.configureBlocking(false);
            // 分配缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // 发送数据
            scanner = new Scanner(System.in);
            while (true) {
                String msg = scanner.nextLine();
                byteBuffer.put(msg.getBytes());
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socketChannel != null) {
                    socketChannel.close();
                }
                if (scanner != null) {
                    scanner.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new socketChannel();
    }
}    