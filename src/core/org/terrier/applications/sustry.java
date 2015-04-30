package org.terrier.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.net.ntp.TimeStamp;

class sustry
{static String scorepath="/home/bhargava/Documents/iranbeginresultsquery/";
static String initsummary="";
static ArrayList<String> updatesummary=new ArrayList<String>();
public static double jc_check(String ta,String tb)
{ HashSet<String> ha = null;
try {
	ha = new HashSet<String>(stemmingandstopwordremovaltry.content(ta));
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
HashSet<String> hb = null;
try {
	hb = new HashSet<String>(stemmingandstopwordremovaltry.content(tb));
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

HashSet<String> hac=new HashSet<String>(ha) ;
HashSet<String> hac1=new HashSet<String>(ha) ;

hac.retainAll(hb);
hac1.addAll(hb);

double jc=(double)hac.size()/(double)hac1.size();
return jc;

}
public static double infogain_check(ArrayList<String> y,ArrayList<String> x)
{
	Map<String, Double> wordCounty = new HashMap<>();
	for (String word: y) {
	    if (wordCounty.containsKey(word)) {
	        // Map already contains the word key. Just increment it's count by 1
	        wordCounty.put(word, wordCounty.get(word) + 1);
	    } else {
	        // Map doesn't have mapping for word. Add one with count = 1
	        wordCounty.put(word, 1.0);
	    }
	}
	Map<String,Double> wordCountx = new HashMap<>();
	for (String word: x) {
	    if (wordCountx.containsKey(word)) {
	        // Map already contains the word key. Just increment it's count by 1
	        wordCountx.put(word, wordCountx.get(word) + 1);
	    } else {
	        // Map doesn't have mapping for word. Add one with count = 1
	        wordCountx.put(word, 1.0);
	    }
	}
	ArrayList<Double> px=new ArrayList<Double>();
	for (Map.Entry<String, Double> entry : wordCountx.entrySet())
	{
	     wordCountx.put(entry.getKey(),entry.getValue()/(double)x.size());
	}
	
	double entropyx=0;
	for (Map.Entry<String, Double> entry : wordCountx.entrySet())
	{
		entropyx=entropyx-entry.getValue()*Math.log10(entry.getValue())/0.30103;
	}
	ArrayList<Double> py=new ArrayList<Double>();
	for (Map.Entry<String, Double> entry : wordCounty.entrySet())
	{
	     wordCounty.put(entry.getKey(),entry.getValue()/(double)y.size());
	}
	double conditionalentropy=0;
	for (Map.Entry<String, Double> entry : wordCountx.entrySet())
	{
		for(Map.Entry<String, Double> entry1 : wordCounty.entrySet())
		{
			if(entry.getKey().equals(entry1.getKey()))
			{
				conditionalentropy=conditionalentropy +entry.getValue()*Math.log10(1/entry1.getValue())/0.30103;
			}
				
		}
	}
	//System.out.println(entropyx-conditionalentropy);
	return (entropyx-conditionalentropy);
}
	public static void main(String args[])
	{
		
		File scores=new File(scorepath);
		String[] folders=scores.list();
		Collections.sort(Arrays.asList(folders));
		
		for(int i=0;i<folders.length;i++)
		{if(folders[i].contains("mmr"))
		{
			System.out.println(folders[i]);
			if(i==0)
				processinitsummary(folders[i]);
			else
			processsummary(folders[i]);
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		
	}

	private static void processinitsummary(String string) {
		// TODO Auto-generated method stub
		File f1=new File(scorepath+string);
		FileReader fr = null;
		try {
			fr = new FileReader(f1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
		String line;
		try {
			while((line=br.readLine())!=null)
			{
				updatesummary.add(line);
				System.out.println(line);
					
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void processsummary(String string) {int flag = 0;
		// TODO Auto-generated method stub
	
		File f1=new File(scorepath+string);
		FileReader fr = null;
		try {
			fr = new FileReader(f1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
		String line;
		try {
			while((line=br.readLine())!=null)
			{//System.out.println(line);
				flag=0;
				for(int i=0;i<updatesummary.size();i++)
				{
					if(jc_check(updatesummary.get(i), line)>0.25)
					//if(infogain_check(stemmingandstopwordremovaltry.content(line), stemmingandstopwordremovaltry.content(updatesummary.get(i)))<0.4)
					flag=1;
				}
				if(flag!=1)
				{
					updatesummary.add(line);
					System.out.println(line);
				}
					
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}