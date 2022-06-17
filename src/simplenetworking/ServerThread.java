package simplenetworking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerThread extends Thread{
    PrintWriter writer;
    static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
    Socket sock;

    ServerThread(Socket sock){
        this.sock = sock;
        try {
            writer = new PrintWriter(sock.getOutputStream());
            list.add(writer);
        } catch (Exception e){

        }
    }

    public void run(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            while (true) {

                // 클라이언트에서 입력이 있을 때 까지 기다린다
                String str = reader.readLine();
                if (str == null) break;

                // 입력이있을 경우 현재 리스트에 있는 writer 객체에 모두 같은 내용을 써줌
                sendAll(str);
            }

        }catch (Exception e){

        }
    }
    private void sendAll(String str) {
        for (PrintWriter writer : list) {
            writer.println(str);
            writer.flush();
        }
    }
}
