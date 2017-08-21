package com.win.test.cl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader {

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class<?> clazz = findLoadedClass(name);
		if (null != clazz)
			return clazz;
		String filePath = name.replace(".", "/").concat(".class");
		File classFile = new File("target/classes/" + filePath);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			InputStream is = new FileInputStream(classFile);
			byte[] buffer = new byte[512];
			int count = -1;
			while ((count = is.read(buffer)) != -1) {
				bos.write(buffer, 0, count);
			}
			is.close();
			clazz = defineClass(name, bos.toByteArray(), 0, bos.size());
		} catch (Exception e) {
			clazz = super.loadClass(name, resolve);
		}

		return clazz;
	}

}
