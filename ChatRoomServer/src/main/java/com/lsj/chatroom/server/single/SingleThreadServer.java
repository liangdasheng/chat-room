package com.lsj.chatroom.server.single;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SingleThreadServer {
    public static void main(String[] args) {
        try {
            //准备ServerSocket
            int port = 6666;
            if(args.length > 0){
                try{
                    port = Integer.parseInt(args[0]);
                }catch (NumberFormatException e){
                    System.out.println("端口号不正确，采用默认端口号：" + port);
                }
            }
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器启动：" + serverSocket.getLocalSocketAddress());
            System.out.println("等待客户端连接...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("客户端的信息：" + clientSocket.getRemoteSocketAddress());
            //收发数据
            InputStream clientInput = clientSocket.getInputStream();
            Scanner scanner = new Scanner(clientInput);
            String clientData = scanner.nextLine();
            System.out.println("来自客户端的消息：" + clientData);
            OutputStream clientOutput = clientSocket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            writer.write("你好，欢迎连接服务器...\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
