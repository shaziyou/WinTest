package com.win.test.cl;

public class TestMain {

	public static void main(String[] args) {
		StaticFieldHolder.obj = "Hi";
		try {
			Class<?> clazz = new MyClassLoader().loadClass("com.win.test.cl.TestClass");
			clazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		System.out.println(StaticFieldHolder.obj);
	}

}
