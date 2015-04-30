package org.terrier.applications;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

class ducfeaturetopic
{public static String hashmappath="/home/bhargava/Documents/firstsetuptry/terrierfreqstat/";
static String path="/home/bhargava/Documents/firstsetuptry/duccontent21april/";
HashMap<String,Double> hourfreq;
	public void computefeaturesforhour(String string, String innerfolders, BufferedWriter bw, int topic, HashMap<String, sentencerank> rankmap) {
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
		gettopicquery(innerfolders);
		querypreprocess();
		topicpreprocess();
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
			file.computefeaturesforfile(path+innerfolders,files[i],querycontent,hourfreq,topicweight,freq,bw,topic,rankmap);
		}
}
	public static ArrayList<String> querycontent;
	private static ArrayList<String> topiccontent;
	private static HashMap<String, Double> topicweight;
	private static String query;
	private static String topic;
	static void querypreprocess()
	{
		
		try {
			querycontent=stemmingandstopwordremovaltry.content(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void topicpreprocess()
	{
		
		try {
			topiccontent=stemmingandstopwordremovaltry.content(query);
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
	static void gettopicquery(String innerfolders)
	{Path xml=Paths.get("/home/bhargava/Downloads/duc2007_topics.sgml");
	String text = null;
	try {
		text = new String(Files.readAllBytes(xml), StandardCharsets.UTF_8);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		Document doc = Jsoup.parse(text, "", Parser.xmlParser());
		Elements e=doc.select("num");
		
		for(Element e1:e)
		{//System.out.println(e1.text()+" "+innerfolders);
			if(e1.text().equals(innerfolders))
		{	topic=e1.parent().select("title").text();
		query=e1.parent().select("narr").text();
		}
		}
	}
}