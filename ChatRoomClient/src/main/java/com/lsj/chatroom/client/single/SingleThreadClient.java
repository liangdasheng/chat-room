package com.lsj.chatroom.client.single;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class SingleThreadClient {
    public static void main(String[] args) {
        try {
            int port = 6666;
            if(args.length > 0){
                try{
                    port = Integer.parseInt(args[0]);
                }catch (NumberFormatException e){
                    System.out.println("端口号不正确，采用默认端口号：" + port);
                }
            }
            String host = "127.0.0.1";
            if(args.length > 1){
                host = args[1];
            }
            Socket clientSocket = new Socket(host,port);
            //收发数据
            OutputStream clientOutput = clientSocket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            writer.write("你好，我是客户端...\n");
            writer.flush();
            InputStream clientInput = clientSocket.getInputStream();
            Scanner scanner = new Scanner(clientInput);
            String serverData = scanner.nextLine();
            System.out.println("来自服务器的消息：" + serverData);
            //服务器一般不需要关闭，客户端可以关
            clientSocket.close();
            clientInput.close();
            clientOutput.close();
            System.out.println("客户端关闭...\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
