package com.win.test.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestMain {

	public static void main(String[] args) {

		ObjectA objA = null;

		Class<ObjectA> clazz = ObjectA.class;
		Annotation[] anns = clazz.getDeclaredAnnotations();
		System.out.println("Annotation Num: " + anns.length);
		EnableCreate enableCreate = clazz.getAnnotation(EnableCreate.class);
		if (null != enableCreate) {
			System.out.println(enableCreate.name());
			try {
				objA = clazz.newInstance();
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					SetVal setVal = field.getAnnotation(SetVal.class);
					if (null == setVal)
						continue;
					String fieldName = field.getName();
					String fieldValue = setVal.value();
					System.out.println("Filed :" + fieldName + "-" + fieldValue);
					try {
						Method setMethod = clazz.getMethod(
								"set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
								field.getType());
						setMethod.invoke(objA, fieldValue);
					} catch (Exception ex) {
						field.setAccessible(true);
						field.set(objA, fieldValue);
						field.setAccessible(false);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("\n***********************************\n");
		System.out.println(objA);
	}

}
