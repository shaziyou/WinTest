package com.win.test.syspro;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemPropertiesLister {

	public static void main(String[] args) {
		for (Object obj : System.getProperties().keySet()) {
			System.out.println(obj + " : " + System.getProperty(String.valueOf(obj)));
		}
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
