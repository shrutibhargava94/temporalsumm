package org.terrier.applications;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

class features
{static boolean train=false;
//static String path="/home/bhargava/Documents/firstsetuptry/newspapercontent1/";
static String freqhashmappath="/home/bhargava/Documents/firstsetuptry/terrierfreqstat/";
static HashMap<String,Double> topicweight;
private static int topic=0;
	public static void main(String args[])
	{
		
		File outfolder=new File(freqhashmappath);
		String[] innerfolders=outfolder.list();
		Collections.sort(Arrays.asList(innerfolders));
		for(int i=0;i<innerfolders.length;i++)
		{
			System.out.println(innerfolders[i]);
			featurehour hour=new featurehour();
			
			querypreprocess();
			topicpreprocess();
			hour.computefeaturesforhour(freqhashmappath,innerfolders[i],querycontent,topicweight,topic);
		}
		
	}
	
	public static ArrayList<String> querycontent;
	private static ArrayList<String> topiccontent;
	static void querypreprocess()
	{
		String query="iran earthquake";
		try {
			querycontent=stemmingandstopwordremovaltry.content(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void topicpreprocess()
	{
		String topic="2012 East Azerbaijan earthquakes";
		try {
			topiccontent=stemmingandstopwordremovaltry.content(topic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		topicweight=new HashMap<String, Double>();
		for(int i=0;i<topiccontent.size();i++)
		{
			if(topicweight.containsKey(topiccontent.get(i)))
					{
				topicweight.put(topiccontent.get(i), topicweight.get(topiccontent.get(i))+1.0);
					}
			else
				topicweight.put(topiccontent.get(i),1.0);
		}
	}
	
	
}

