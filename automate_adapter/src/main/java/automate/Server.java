package automate;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class Server extends Thread{
    final static int port = 5901;

    @Override
    public void run(){
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        ServerSocket sc = null;
        try {
            sc = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            Socket socket = null;
            try {
                socket = sc.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            threadPool.execute(new ServiceHandler(socket));
        }
    }
}
