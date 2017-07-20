package com.win.test.selector;

import java.io.IOException;

public class RunServer {

	public static void main(String[] args) {
		Server server = null;
		try {
			server = new Server(8088);
			server.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
