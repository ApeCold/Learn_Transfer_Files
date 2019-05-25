package cn.bsd.learn.transfer.files.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class BioServer {
    public static Charset charset = Charset.forName("UTF-8");
    public static void main(String[] args){
        int port = 9200;
        try (ServerSocket ss = new ServerSocket(port)){
            while (true){
                try {
                    //接收链接
                    Socket s = ss.accept();
                    new Thread(new SocketProcess(s)).start();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream(),charset));
//                    String mess = null;
//                    //接收数据
//                    while ((mess = reader.readLine())!=null){
//                        System.out.println(mess);
//                    }
//                    s.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    static class SocketProcess implements Runnable{
        Socket s;
        public SocketProcess(Socket s){
            super();
            this.s=s;
        }
        @Override
        public void run() {
            try (BufferedReader reader =  new BufferedReader(
                    new InputStreamReader(s.getInputStream(),charset));){
                //接收数据
                String mess = null;
                while ((mess=reader.readLine())!=null){
                    System.out.println(mess);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
