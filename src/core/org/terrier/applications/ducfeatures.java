package org.terrier.applications;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

class ducfeatures
{static boolean train=false;
//static String path="/home/bhargava/Documents/firstsetuptry/newspapercontent1/";
static String freqhashmappath="/home/bhargava/Documents/firstsetuptry/terrierfreqstatduc/";
static HashMap<String,Double> topicweight;
	public static void main(String args[])
	{File f=new File("/home/bhargava/Documents/firstsetuptry/ducfeatures.txt");
	FileWriter fw=null;
	try {
		 fw=new FileWriter(f);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	BufferedWriter bw=new BufferedWriter(fw);
		
		File outfolder=new File(freqhashmappath);
		String[] innerfolders=outfolder.list();
		Collections.sort(Arrays.asList(innerfolders));
		for(int i=0;i<innerfolders.length;i++)
		{
			System.out.println(innerfolders[i]);
			ducfeaturetopic hour=new ducfeaturetopic();
			
		
			hour.computefeaturesforhour(freqhashmappath,innerfolders[i],bw,i);
		}
		
	}
	
	
	
	
}

