package com.win.test.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject {

	private static final long serialVersionUID = -2049840462525943019L;

	public Client() throws RemoteException {
		super(3929);
	}

}
