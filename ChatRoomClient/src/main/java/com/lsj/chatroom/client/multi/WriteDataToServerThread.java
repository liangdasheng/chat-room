package com.lsj.chatroom.client.multi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteDataToServerThread extends Thread {
    private final Socket client;
    public WriteDataToServerThread(Socket client){
        this.client = client;
    }
    public void run(){
        try {
            OutputStream clientOutput = client.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.println("请输入信息：");
                String message = scanner.nextLine();
                writer.write(message+"\n");
                writer.flush();
                if(message.equals("bye")){
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}