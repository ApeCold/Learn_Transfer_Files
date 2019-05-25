package cn.bsd.learn.transfer.files.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

public class BioClient implements Runnable{
    private String host;
    private int port;
    public static Charset charset = Charset.forName("UTF-8");

    public BioClient(String host,int port){
        super();
        this.host=host;
        this.port= port;
    }


    @Override
    public void run() {
        try {
            Socket s = new Socket(host,port);
            OutputStream out = s.getOutputStream();
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入");
            String mess = scanner.nextLine();
            out.write(mess.getBytes(charset));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        BioClient client= new BioClient("localhost",9200);
        client.run();
    }
}
