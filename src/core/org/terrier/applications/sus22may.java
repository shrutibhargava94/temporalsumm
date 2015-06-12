package org.terrier.applications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


class sus22may
{static ArrayList<runattributes> updates=new ArrayList<runattributes>();
static String path;//="/home/bhargava/afghansumm26may";
static BufferedWriter bw=null;
static double K;
	//checking if summary contains the query
private static String[] querytermsarray;
	
	//read summary for hour
	public static ArrayList<runattributes> readsumm(String filename)
	{ArrayList<runattributes> summary=new ArrayList<runattributes>();
		BufferedReader br=null;
		 try {
			br=new BufferedReader(new FileReader(new File(path+"/"+filename)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 String line="";
			
				try {
					while((line=br.readLine())!=null)
					{
						String[] split=line.split("\t");
						//System.out.println(split[5]);
						
					
								
									runattributes r=new runattributes();
									r.qid=split[1];
									r.tid=split[2];
									r.rid=split[3];
									r.did=split[4];
									r.sid=split[5];
									r.sent=split[0];
									r.timestamp=split[6];
								r.confidence=split[7];
								
									summary.add(r);
						}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return summary;
				
	}

public static void main(String args[])
{K=0.5;//Double.parseDouble(args[0]);
//if(args[2]!=null)
//path=args[2];
Scanner input=new Scanner(System.in);
System.out.println("Enter the path where the clusters are saved Example /home/bhargava/Documents/nilamclust");
path=input.nextLine();//"/home/bhargava/Documents/hostageclust";
System.out.println("Enter the path where the upadtes will be saved Example /home/bhargava/Documents/afghanistanfull/afghansumm/nilamupdateseval.tsv");
String pathforupdates = input.nextLine();
System.out.println("Enter the query");
String query=input.nextLine();
queryprocess(query);
	try {
	//bw  = new BufferedWriter(new FileWriter(new File(args[1])));
		bw  = new BufferedWriter(new FileWriter(new File(pathforupdates)));
} catch (IOException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
	//read summary and check with query till a first summary is found..which is then first update
	updates=new ArrayList<runattributes>();
	
	File rankedsent=new File(path);
	String[] files=rankedsent.list();
	Collections.sort(Arrays.asList(files));
	int updatestart=0;
	for(int file=0;file<files.length;file++)
	{System.out.println(files[file]);
		Path path1=Paths.get(rankedsent+"/"+files[file]);
	long filesize=0;
	try {
		filesize=Files.size(path1);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
if((files[file].contains("eval"))&&(filesize>0))
{ArrayList<runattributes> summ = readsumm(files[file]);
if(checkforquery(summ))
{
	updatestart=file;
	for(int i=0;i<summ.size();i++)
	{System.out.println(summ.get(i).sent+"\t"+summ.get(i).qid+"\t"+summ.get(i).tid+"\t"+summ.get(i).rid+"\t"+summ.get(i).did+"\t"+summ.get(i).sid+"\t"+summ.get(i).timestamp+"\t"+summ.get(i).confidence);
	try {
		bw.write(summ.get(i).qid+"\t"+summ.get(i).tid+"\t"+summ.get(i).rid+"\t"+summ.get(i).did+"\t"+summ.get(i).sid+"\t"+summ.get(i).sent+"\t"+summ.get(i).timestamp+"\t"+summ.get(i).confidence);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		bw.newLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	updates.add(summ.get(i));
	}
	break;
}
	
	}
	}
	
	for(int file=updatestart;file<files.length;file++)
	{System.out.println(files[file]);
		Path path1=Paths.get(rankedsent+"/"+files[file]);
	long filesize=0;
	try {
		filesize=Files.size(path1);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
if((files[file].contains("eval"))&&(filesize>0))
{
	processsumm(files[file]);
	
}
	}
try {
	bw.close();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}}
	
private static void queryprocess(String query) {
	// TODO Auto-generated method stub
	querytermsarray=query.split(" ");
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

private static void processsumm(String string) {
	// TODO Auto-generated method stub
	BufferedReader br=null;
	 try {
		br=new BufferedReader(new FileReader(new File(path+"/"+string)));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	 String line="";
		
			
				try {
					while((line=br.readLine())!=null)
					{
						String[] split=line.split("\t");
						//System.out.println(split[5]);
						
					
								
									runattributes r=new runattributes();
									r.qid=split[1];
									r.tid=split[2];
									r.rid=split[3];
									r.did=split[4];
									r.sid=split[5];
									
									r.timestamp=split[6];
									r.confidence=split[7];
									r.sent=split[0];
									int flag=0;
								for(int i=0;i<updates.size();i++)
									{
									if(infogain_check(stemmingandstopwordremovaltry.content(r.sent), stemmingandstopwordremovaltry.content(updates.get(i).sent))<K)
										flag=1;
									
									}
								if(flag!=1)
								{
									updates.add(r);
									System.out.println(r.sent);
									bw.write(r.qid+"\t"+r.tid+"\t"+r.rid+"\t"+r.did+"\t"+r.sid+"\t"+r.sent+"\t"+r.timestamp+"\t"+r.confidence);
									bw.newLine();
								}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
}

private static boolean checkforquery(ArrayList<runattributes> readsumm) {
	for(int i=0;i<readsumm.size();i++)
	{
		for(int j=0;j<querytermsarray.length;j++)
			if(readsumm.get(i).sent.contains(querytermsarray[j].toLowerCase()))
				return true;
		//if(readsumm.get(i).sent.contains("in")||readsumm.get(i).sent.contains("amenas")||readsumm.get(i).sent.contains("hostage")||readsumm.get(i).sent.contains("crisis"))
			//return true;
	}
	return false;
	
}
}