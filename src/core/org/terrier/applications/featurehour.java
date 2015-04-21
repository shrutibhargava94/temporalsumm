package org.terrier.applications;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class featurehour
{public static String hashmappath="/home/bhargava/Documents/firstsetuptry/terrierfreqstat/";
static String path="/home/bhargava/Documents/firstsetuptry/newspapercontent1/";
HashMap<String,Double> hourfreq;
	public void computefeaturesforhour(String string, String innerfolders, ArrayList<String> querycontent, HashMap<String, Double> topicweight) {
		// TODO Auto-generated method stub
		
		
		File outfolder=new File(string+innerfolders);
		String[] files=outfolder.list();
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
			
			
			featurefile file=new featurefile();
			System.out.println(path+innerfolders);
			file.computefeaturesforfile(path+innerfolders,files[i],querycontent,hourfreq,topicweight,freq);
		}
}
}