package org.terrier.applications;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

class features
{static boolean train=false;
//static String path="/home/bhargava/Documents/firstsetuptry/newspapercontent1/";
static String freqhashmappath;//="/home/bhargava/Documents/hostagefreqstat/";
static HashMap<String,Double> topicweight;
private static int topic;//=17;
private static String queryortitle;
	public static void main(String args[])
	{Scanner input=new Scanner(System.in);
		System.out.println("Need to make the folder for saving the features");
		System.out.println("enter the path where the frequency statistics taken from terrier are saved Example /home/bhargava/Documents/nilamfreqstat/");
		freqhashmappath=input.nextLine();
		System.out.println("enter the query");
		queryortitle=input.nextLine();
		System.out.println("Enter the topic number");
		topic=Integer.parseInt(input.nextLine());
		System.out.println("Enter the path where the documents are saved Example /home/bhargava/Documents/nilamsentence/");
		String sentencepath=input.nextLine();
		System.out.println("Enter the path where the features will be saved /home/bhargava/Documents/nilamfeatures");
		String featurespath=input.nextLine();
		System.out.println("Enter the path where the extracted articles are saved /home/bhargava/Documents/nilamcontentfilter/");
		String contentpath=input.nextLine();
		
		File outfolder=new File(freqhashmappath);
		String[] innerfolders=outfolder.list();
		Collections.sort(Arrays.asList(innerfolders));
		for(int i=0;i<innerfolders.length;i++)
		{
			System.out.println(innerfolders[i]);
			featurehour hour=new featurehour(sentencepath,featurespath,freqhashmappath,contentpath);
			
			querypreprocess();
			topicpreprocess();
			hour.computefeaturesforhour(freqhashmappath,innerfolders[i],querycontent,topicweight,topic);
		}
		
	}
	
	public static ArrayList<String> querycontent;
	private static ArrayList<String> topiccontent;
	static void querypreprocess()
	{
		String query=queryortitle;
		try {
			querycontent=stemmingandstopwordremovaltry.content(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void topicpreprocess()
	{
		String topic=queryortitle;
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

