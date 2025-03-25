package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class serviceChannel {
    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            // 打开 ServerSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            // 绑定端口
            serverSocketChannel.bind(new InetSocketAddress(8080));
            // 设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            // 打开选择器
            selector = Selector.open();
            // 将 ServerSocketChannel 注册到选择器，并监听接收事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server is listening on port 8080...");

            while (true) {
                // 等待事件发生
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                // 获取就绪的事件集合
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey selectionKey = keyIterator.next();

                    if (selectionKey.isAcceptable()) {
                        // 处理接收事件
                        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = ssc.accept();
                        socketChannel.configureBlocking(false);
                        // 将新的 SocketChannel 注册到选择器，并监听读事件
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("New client connected: " + socketChannel.getRemoteAddress());
                    } else if (selectionKey.isReadable()) {
                        // 处理读事件
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int bytesRead;
                        try {
                            bytesRead = socketChannel.read(buffer);
                            if (bytesRead > 0) {
                                buffer.flip();
                                byte[] data = new byte[buffer.remaining()];
                                buffer.get(data);
                                System.out.println("Received from client: " + new String(data));
                            } else if (bytesRead == -1) {
                                // 客户端关闭连接
                                System.out.println("Client disconnected: " + socketChannel.getRemoteAddress());
                                socketChannel.close();
                            }
                        } catch (IOException e) {
                            // 处理读异常
                            System.out.println("Error reading from client: " + e.getMessage());
                            socketChannel.close();
                        } finally {
                            buffer.clear();
                        }
                    }
                    // 移除已处理的事件
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (selector != null) {
                    selector.close();
                }
                if (serverSocketChannel != null) {
                    serverSocketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}    