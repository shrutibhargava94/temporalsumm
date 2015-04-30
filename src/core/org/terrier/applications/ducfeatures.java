package org.terrier.applications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
static HashMap<String,sentencerank> rankmap=new HashMap<String, sentencerank>();
static HashMap<String,Double> topicweight;
	public static void main(String args[])
	{File f=new File("/home/bhargava/Documents/firstsetuptry/ducfeaturesqueryonly.txt");
	FileWriter fw=null;
	try {
		 fw=new FileWriter(f);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	BufferedWriter bw=new BufferedWriter(fw);
		File f1=new File("/home/bhargava/Documents/firstsetuptry/duc2007nonsorted.txt");
		FileReader fr = null;
		try {
			fr = new FileReader(f1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
		String line;
		//rankmap=new HashMap<String, sentencerank>();
		try {
			while((line=br.readLine())!=null)
			{
				String[] train=line.split("\t");
				sentencerank sr=new sentencerank();
				sr.rank=Integer.parseInt(train[0]);
				sr.topic=Integer.parseInt(train[1]);
				sr.sentence=train[3];
				rankmap.put(sr.sentence,sr);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File outfolder=new File(freqhashmappath);
		String[] innerfolders=outfolder.list();
		Collections.sort(Arrays.asList(innerfolders));
		for(int i=0;i<innerfolders.length;i++)
		{
			System.out.println(innerfolders[i]);
			ducfeaturetopic hour=new ducfeaturetopic();
			
		
			hour.computefeaturesforhour(freqhashmappath,innerfolders[i],bw,i,rankmap);
		}
	try {
		bw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	}
	
	
	
	
}

