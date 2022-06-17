package simplenetworking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleChatServer {
	public void go() {
		try {
			ServerSocket server = new ServerSocket(4242);
			while (true) {
				Socket socket = server.accept();
				System.out.println(socket);
				Thread thread = new ServerThread(socket);
				thread.start();
			}
		} catch (Exception exception) {
		}
	}
}