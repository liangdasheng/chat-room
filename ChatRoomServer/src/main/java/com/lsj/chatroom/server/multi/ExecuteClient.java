package com.lsj.chatroom.server.multi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ExecuteClient implements Runnable {
    //每次都是NEW一个新对象，数据不能共享，所以加static
    //ConcurrentHashMap解决并发问题，当前是多线程的聊天室，为了保证安全，并发数据不被修改，使用安全的Map
    // TreeMap和HashMap不安全
    private static final Map<String, Socket> ON_LINE_USER_MAP = new ConcurrentHashMap<>();
    private final Socket client;

    public ExecuteClient(Socket client) {
        this.client = client;
    }

    public void run() {
        //获取客户端输入
        try {
            InputStream clientInput = this.client.getInputStream();
            Scanner scanner = new Scanner(clientInput);
            while (true) {
                String line = scanner.nextLine();
                //业务:定义一套规范
                //注册  userName:<name>
                if (line.startsWith("userName")) {
                    String userName = line.split("\\:")[1];
                    this.register(userName, client);
                    continue;
                }
                //私聊  private:<name>:<mes>
                if (line.startsWith("private")) {
                    //取冒号前面的第一个元素就是userName
                    String[] segments = line.split("\\:");
                    String userName = segments[1];
                    String message = segments[2];
                    this.privateChat(userName, message);
                    continue;
                }
                //群聊  group:<mes>
                if (line.startsWith("group")) {
                    String message = line.split("\\:")[1];
                    this.groupChat(message);
                    continue;
                }
                //退出  bye
                if (line.equals("bye")) {
                    this.quit();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void quit() {
        String currentUserName = this.getCurrentUserName();
        System.out.println("用户" + currentUserName + "下线");
        Socket socket = ON_LINE_USER_MAP.get(currentUserName);
        this.sendMessage(socket, "bye");
        ON_LINE_USER_MAP.remove(currentUserName);
        printOnLineUser();
    }

    private void groupChat(String message) {
        for (Socket socket : ON_LINE_USER_MAP.values()) {
            if (socket.equals(this.client)) {
                continue;
            }
            this.sendMessage(socket, this.getCurrentUserName() + " 说：" + message);
        }
    }

    private void privateChat(String userName, String message) {
        String currentUserName = this.getCurrentUserName();
        Socket target = ON_LINE_USER_MAP.get(userName);
        if (target != null) {
            this.sendMessage(target, currentUserName + " 对你说：" + message);
        }
    }

    private void sendMessage(Socket socket, String s) {
        try {
            OutputStream clientOutput = socket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            writer.write( s+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(String userName, Socket client) {
        System.out.println(userName + "加入到聊天室" + client.getRemoteSocketAddress());
        ON_LINE_USER_MAP.put(userName, client);
        printOnLineUser();
        sendMessage(this.client, userName + "注册成功！");
    }

    private static void printOnLineUser() {
        System.out.println("当前在线人数：" + ON_LINE_USER_MAP.size() + " 用户名如下列表：");
        for (Map.Entry<String, Socket> entry : ON_LINE_USER_MAP.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    private String getCurrentUserName() {
        String currentUserName = "";
        for (Map.Entry<String, Socket> entry : ON_LINE_USER_MAP.entrySet()) {
            if (this.client.equals(entry.getValue())) {
                currentUserName = entry.getKey();
                break;
            }
        }
        return currentUserName;
    }
}