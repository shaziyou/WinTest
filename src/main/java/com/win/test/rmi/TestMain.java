package com.win.test.rmi;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public class TestMain {

	public static void main(String[] args) throws RemoteException, ServerNotActiveException {
		Client c = new Client();
		System.out.println(c.getRef().remoteToString());
	}

	/**
	 * 绑定客户端 IP
	 *   针对多IP时，服务端调用客户端时IP绑定异常
	 */
	public static void bindIp() {
		System.setProperty("java.rmi.server.hostname", "192.168.137.1");
	}

}
