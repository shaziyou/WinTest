package com.win.test.reg;

import java.util.Scanner;
import java.util.regex.Pattern;

public class TestMain {

	public static void main(String[] args) {
		System.out.println(Pattern.matches("\\d", "1"));
		System.out.println(Pattern.matches("/d", "1"));
		Scanner scan = new Scanner(System.in);
		System.out.print("Please input a:");
		String a = scan.nextLine();
		System.out.print("Please input b:");
		String b = scan.nextLine();
		scan.close();
		if("".equals(a))
			a = null;
		if("".equals(b))
			b = null;
		tag: if (null == a) {
			if (null == b){
				System.out.println("a = null , b = null");
				break tag;
			}
			System.out.println("a = null , b  ! = null");
		} else {
			System.out.println("a != null");
		}
		System.out.println("Finished...");
	}

}
