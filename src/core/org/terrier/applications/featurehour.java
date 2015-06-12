package org.terrier.applications;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class featurehour
{public  String hashmappath;//="/home/bhargava/Documents/hostagefreqstat/";
 String path;//="/home/bhargava/Documents/hostagesentence/";
HashMap<String,Double> hourfreq;
private String featurespath;
private String contentpath;
	public featurehour(String sentencepath, String featurespath,
		String freqhashmappath, String contentpath) {
	// TODO Auto-generated constructor stub
		path=sentencepath;
		hashmappath=freqhashmappath;
		this.featurespath=featurespath;
		this.contentpath=contentpath;
}
	public void computefeaturesforhour(String string, String innerfolders, ArrayList<String> querycontent, HashMap<String, Double> topicweight, int topic) {
		// TODO Auto-generated method stub
		if(!innerfolders.contains(".directory"))
		{
		File f=new File(featurespath+"/trecfeaturesner"+innerfolders+".txt");
		FileWriter fw=null;
		try {
			 fw=new FileWriter(f);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		BufferedWriter bw=new BufferedWriter(fw);
		
		File outfolder=new File(string+innerfolders);
		String[] files=outfolder.list();
		if(files.length!=0)
		{
		Collections.sort(Arrays.asList(files));
		hourfreq=new HashMap<String,Double>();
		for(int i=0;i<files.length;i++)
		{FileInputStream fin = null;
		try {
			fin = new FileInputStream(string+innerfolders+"/"+files[i]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(fin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HashMap<String,Double>  freq= (HashMap<String,Double>) ois.readObject();
			for(Map.Entry<String,Double> entry:freq.entrySet())
			{
				if(hourfreq.containsKey(entry.getKey()))
				{
					hourfreq.put(entry.getKey(), hourfreq.get(entry.getKey())+entry.getValue());
				}
				else
				{
					hourfreq.put(entry.getKey(),entry.getValue());
				}
					
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		for(int i=0;i<files.length;i++)
		{
			System.out.println(files[i]);
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(string+innerfolders+"/"+files[i]);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(fin);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}HashMap<String,Double>  freq = null;
			
				try {
					 freq= (HashMap<String,Double>) ois.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
			//featurefile file=new featurefile();
				CopyOffeaturefilesentid file=new CopyOffeaturefilesentid();
			System.out.println(path+innerfolders);
			file.computefeaturesforfile(path+innerfolders,files[i],querycontent,hourfreq,topicweight,freq,bw,topic,null,contentpath);
		}
try {
	bw.close();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}}}
}}