package org.terrier.applications;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

class trial
{
	public static void main(String[] args){
		File dir=new File("/home/bhargava/Documents/firstsetuptry/DUCcontent");
		String[] list=dir.list();
		Collections.sort(Arrays.asList(list));
		for(int i=0;i<list.length;i++)
			System.out.println(list[i]);
	}
}