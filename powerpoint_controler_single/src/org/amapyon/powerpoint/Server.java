package org.amapyon.powerpoint;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;

public class Server {
	HttpServer server;

	public Server(int port) {
		try {
			server = HttpServer.create(new InetSocketAddress(port), -1);
			server.createContext("/", new Handler());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		System.out.println(server.getAddress() + " Start");
		server.start();
	}

}
