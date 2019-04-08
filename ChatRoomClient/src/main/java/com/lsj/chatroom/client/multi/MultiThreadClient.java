package com.lsj.chatroom.client.multi;

import java.io.IOException;
import java.net.Socket;

public class MultiThreadClient{
    public static void main(String[] args) {
        try{
            //通过命令行获取端口号
            int port = 6666;
            if(args.length>0){
                try{
                    port = Integer.parseInt(args[0]);
                }catch(NumberFormatException e){
                    System.out.println("端口参数不正确，采用默认参数："+port);
                }
            }
            String host = "127.0.0.1";
            if(args.length>1){
                host = args[1];
                //host校验
            }
            final Socket socket = new Socket(host,port);
            new WriteDataToServerThread(socket).start();
            new ReadDataFromServerThread(socket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}