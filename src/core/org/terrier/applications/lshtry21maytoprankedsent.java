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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import lsh.minhash.JaccardDocument;
import lsh.minhash.MinHashLSH;
import lsh.minhash.MinHashLSH.RankedDocument;
import tuan.ml.Features;
import tuan.ml.FeaturesFactory;

class lshtry21maytoprankedsent
{HashMap<String,Integer> termspacereduce;
	private HashSet<String> termspace;
private HashMap<String,runattributes> rankedsent;
private ArrayList<HashMap<String,runattributes>> clusters;
private ArrayList<JaccardDocument> jaccarddoclist;
private ArrayList<String> termspacelist;
private ArrayList<runattributes> summarylist;
static int K=100;
private static String summarypath;
	public  void readfileandtermspace(String filename)
	{termspace=new HashSet<String>();
	termspacereduce =new HashMap<String, Integer>();
	termspacelist=new ArrayList<String>();
	int sentct=0;
		BufferedReader br=null;
		try {
			br=new BufferedReader(new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rankedsent=new HashMap<String, runattributes>();
		String line="";
		try {
			while((line=br.readLine())!=null)
			{
				String[] split=line.split("\t");
				//System.out.println(split[5]);
				
				
				ArrayList<String> sentencecontent = stemmingandstopwordremovaltry.content(split[5]);
	
			
				runattributes r=new runattributes();
				r.qid=split[0];
				r.tid=split[1];
				r.rid=split[2];
				r.did=split[3];
				r.sid=split[4];
				r.sent=split[5];
				r.timestamp=split[6];
				r.confidence=split[7];
				
				rankedsent.put(split[5],r);
				HashSet<String> hb = null;
				hb = new HashSet<String>(sentencecontent);
			for(int i=0;i<sentencecontent.size();i++)
			{
				if(termspacereduce.containsKey(sentencecontent.get(i)))
					termspacereduce.put(sentencecontent.get(i),termspacereduce.get(sentencecontent.get(i))+1);
				else
					termspacereduce.put(sentencecontent.get(i),1);
					
			}
				termspace.addAll(hb);
			sentct++;
			if(rankedsent.size()==K-1)
				break;
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	for(Map.Entry<String,Integer> entry:termspacereduce.entrySet())
		{if(entry.getValue()==1)
			termspace.remove(entry.getKey());
		else
		termspacelist.add(entry.getKey());
		}
	System.out.println("no of sentences"+rankedsent.size()+" "+sentct);
	
	try {
		br.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		}
	
	
	private void processlsh(String string, String string2) {BufferedWriter bw = null;
	try {
		 bw=new BufferedWriter(new FileWriter(new File(summarypath+string2)));
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}long starttime=System.currentTimeMillis();
		readfileandtermspace(string+string2);
		long endtime=System.currentTimeMillis();
		System.out.println(endtime-starttime);
		MinHashLSH minhash = new MinHashLSH(100,5,100,100000000);
		minhash.setThreshold(0.8d, Double.MIN_VALUE);
		clusters=new ArrayList<HashMap<String,runattributes>>();
	jaccarddoclist=new ArrayList<JaccardDocument>();
		//term space
	for(Map.Entry<String, runattributes> entry:rankedsent.entrySet())
	{
		double[] val=new double[termspace.size()];
		HashMap<String, Double> sentfreq = getsentencefreq(entry.getKey());
		
		int count=0;
		/*Iterator<String> iterator=termspace.iterator();
		while(iterator.hasNext())
		{
			String term=iterator.next();
			if(sentfreq.containsKey(term))
				val[count]=sentfreq.get(term);
			else
				val[count]=0.0;
			count++;
				
		}*/
		for(Map.Entry<String, Double> entry1:sentfreq.entrySet())
		{if(termspacelist.contains(entry1.getKey()))
			val[termspacelist.indexOf(entry1.getKey())]=entry1.getValue();
		}
		//System.out.println(vals.length);
		System.out.println(val.length);
		Features f=FeaturesFactory.newInstance(val);
		JaccardDocument d1 = new JaccardDocument(entry.getKey(), f, termspace.size());
		jaccarddoclist.add(d1);
		minhash.put(d1);
		
	}/*int ct=0;
	for(Map.Entry<String, runattributes> entry:rankedsent.entrySet())
	{if(ct==0)
		{HashMap<String,runattributes> cluster=new HashMap<String, runattributes>();
		double[] val=new double[termspace.size()];
	HashMap<String, Double> sentfreq = getsentencefreq(entry.getKey());
	
	int count=0;
	Iterator<String> iterator=termspace.iterator();
	while(iterator.hasNext())
	{
		String term=iterator.next();
		if(sentfreq.containsKey(term))
			val[count]=sentfreq.get(term);
		else
			val[count]=0.0;
		count++;
			
	}
	//System.out.println(vals.length);
	
	Features f=FeaturesFactory.newInstance(val);
	JaccardDocument d1 = new JaccardDocument(entry.getKey(), f, termspace.size());
Iterator<RankedDocument> result = (Iterator<RankedDocument>) minhash.neighbours(d1);
	cluster.put(entry.getKey(),entry.getValue());
	System.out.println(entry.getKey());
	while (result.hasNext()) {
	RankedDocument item = result.next();
	cluster.put(item.item().key(),rankedsent.get(item.item().key()));
	System.out.println(item.item().key());
	}clusters.add(cluster);
	}else
	{int alreadycovered=0;
		for(int k=0;k<clusters.size();k++)
		{
			if(clusters.get(k).containsKey(entry.getKey()))
			{
				alreadycovered=1;break;
			}
				
		}
		if(alreadycovered==0)
		{
			HashMap<String,runattributes> cluster=new HashMap<String, runattributes>();
			double[] val=new double[termspace.size()];
		HashMap<String, Double> sentfreq = getsentencefreq(entry.getKey());
		
		int count=0;
		Iterator<String> iterator=termspace.iterator();
		while(iterator.hasNext())
		{
			String term=iterator.next();
			if(sentfreq.containsKey(term))
				val[count]=sentfreq.get(term);
			else
				val[count]=0.0;
			count++;
				
		}
		//System.out.println(vals.length);
		
		Features f=FeaturesFactory.newInstance(val);
		JaccardDocument d1 = new JaccardDocument(entry.getKey(), f, termspace.size());
	Iterator<RankedDocument> result = (Iterator<RankedDocument>) minhash.neighbours(d1);
		cluster.put(entry.getKey(),entry.getValue());
		System.out.println(entry.getKey());
		while (result.hasNext()) {
		RankedDocument item = result.next();
		cluster.put(item.item().key(),rankedsent.get(item.item().key()));
		System.out.println(item.item().key());
		}clusters.add(cluster);
		}
	}
	
	ct++;
	}*/int clusterct=0;
	System.out.println(clusterct);
	
	clusterct++;
	Iterator<RankedDocument> result = (Iterator<RankedDocument>) minhash.neighbours(jaccarddoclist.get(0));
	HashMap<String, runattributes> cluster = new HashMap<String, runattributes>();
	cluster.put(jaccarddoclist.get(0).key(),rankedsent.get(jaccarddoclist.get(0).key()));
System.out.println(jaccarddoclist.get(0).key());


	while (result.hasNext()) {
	RankedDocument item = result.next();
	cluster.put(item.item().key(),rankedsent.get(item.item().key()));
	System.out.println(item.item().key());
	
	}clusters.add(cluster);
	for(int i=1;i<jaccarddoclist.size();i++)
	{int alreadycovered=0;
	for(int k=0;k<clusters.size();k++)
	{
		if(clusters.get(k).containsKey(jaccarddoclist.get(i).key()))
		{
			alreadycovered=1;break;
		}
			
	}
	if(alreadycovered==0)
	{System.out.println(clusterct);
	
	
		HashMap<String,runattributes> cluster1=new HashMap<String, runattributes>();
	Iterator<RankedDocument> result1 = (Iterator<RankedDocument>) minhash.neighbours(jaccarddoclist.get(i));
	cluster1.put(jaccarddoclist.get(i).key(),rankedsent.get(jaccarddoclist.get(i).key()));
	System.out.println(jaccarddoclist.get(i).key());
	
	while (result1.hasNext()) {
	RankedDocument item = result1.next();
	int alreadycovered1=0;
	for(int k=0;k<clusters.size();k++)
	{
		if(clusters.get(k).containsKey(item.item().key()))
		{
			alreadycovered1=1;break;
		}
			
	}if(alreadycovered1==0)
	{
	cluster1.put(item.item().key(),rankedsent.get(item.item().key()));
System.out.println(item.item().key());
	}
	}clusterct++;clusters.add(cluster1);
	try {
		System.in.read();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	}
	}Collections.sort(clusters,new Comparator<HashMap<String,runattributes>>(){
    	public int compare(HashMap<String,runattributes>s1,HashMap<String,runattributes> s2)
    	{return s2.size()-s1.size();
    	}
    });int totalupdates=0;
    
	for(int i=0;i<clusters.size();i++)
	{//int updatefromcluster=clusters.get(i).size()/K*20;
		System.out.println(clusters.get(i).size());
		//System.out.println(clusters.get(i).entrySet().iterator().next());
		SortedSet<Map.Entry<String, runattributes>> sortedset = new TreeSet<Map.Entry<String, runattributes>>(
	            new Comparator<Map.Entry<String, runattributes>>() {
	                @Override
	                public int compare(Map.Entry<String, runattributes> e1,
	                        Map.Entry<String, runattributes> e2) {
	                    return e2.getValue().confidence.compareTo(e1.getValue().confidence);
	                }
	            });

	  sortedset.addAll(clusters.get(i).entrySet());
	 /* try {
			bw.write(sortedset.first().getValue().sent+"\t"+sortedset.first().getValue().qid+"\t"+sortedset.first().getValue().tid+"\t"+sortedset.first().getValue().rid+"\t"+sortedset.first().getValue().did+"\t"+sortedset.first().getValue().sid+"\t"+sortedset.first().getValue().timestamp+"\t"+sortedset.first().getValue().confidence);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  try {
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	 //System.out.println(sortedset.first());
	 int up=0;
	 Iterator<Entry<String, runattributes>> iter=sortedset.iterator();
	 summarylist=new ArrayList<runattributes>();
	 while(iter.hasNext())
	 {runattributes nextsent = iter.next().getValue();
	 if(jaccardcheck(nextsent.sent))
	 { summarylist.add(nextsent);
		 System.out.println(nextsent.sent+ " "+nextsent.confidence);
		 try {
				bw.write(nextsent.sent+"\t"+nextsent.qid+"\t"+nextsent.tid+"\t"+nextsent.rid+"\t"+nextsent.did+"\t"+nextsent.sid+"\t"+nextsent.timestamp+"\t"+nextsent.confidence);
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
		 up++; totalupdates++;
	//	 if(up==updatefromcluster)
		//	 break;
	 }
	 }
	
//if(totalupdates==20)
	//break;
	 
}
	
	
	try {
		bw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	private boolean jaccardcheck(String sent) {
		ArrayList<String> s1 = null;
		// TODO Auto-generated method stub
		try {
			s1 = stemmingandstopwordremovaltry.content(sent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}ArrayList<String> s2 = null;
		for(int i=0;i<summarylist.size();i++)
		{try {
			 s2= stemmingandstopwordremovaltry.content(summarylist.get(i).sent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			if(jaccard(s1,s2)>0.2)
				return false;
		}
		return true;
	}


	public  HashMap<String,Double> getsentencefreq(String sentence)
	{ArrayList<String> sentencecontent = null;
	try {
		sentencecontent = stemmingandstopwordremovaltry.content(sentence);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		HashMap<String, Double> sentencefreq = new HashMap<String, Double>();
		for(int i=0;i<sentencecontent.size();i++)
		{
			if(sentencefreq.containsKey(sentencecontent.get(i)))
			{
		sentencefreq.put(sentencecontent.get(i), sentencefreq.get(sentencecontent.get(i))+1.0);
			}
	else
		sentencefreq.put(sentencecontent.get(i),1.0);
			
		}
		return sentencefreq;
		
	}
	double jaccard(List<String> s1, List<String> s2)
	{
		HashSet<String> ha=new HashSet<String>(s1);
		HashSet<String> hb=new HashSet<String>(s2);
		
		HashSet<String> hac=new HashSet<String>(ha) ;
		HashSet<String> hac1=new HashSet<String>(ha) ;
		
		hac.retainAll(hb);
	    hac1.addAll(hb);
	    
	    double jc=(double)hac.size()/(double)hac1.size();
		return jc;
	    
	    
	   
	}
	public static void main(String args[])
	{Scanner input=new Scanner(System.in);
	System.out.println("Enter the path for the ranking results example /home/bhargava/Documents/nilamresultsranking");
		String path=input.nextLine();//"/home/bhargava/Documents/hostageresultsranking";
		System.out.println("Enter the path where the summaries will be stored example /home/bhargava/Documents/nilamclust/");
	  summarypath=input.nextLine();
		File rankedsent=new File(path);
		String[] files=rankedsent.list();
		Collections.sort(Arrays.asList(files));
		
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
		{lshtry21maytoprankedsent st=new lshtry21maytoprankedsent();
		st.processlsh(path+"/",files[file]);
  try {
	System.in.read();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		}
			
		}
		/*long starttime=System.currentTimeMillis();
		lshtry21maytoprankedsent st=new lshtry21maytoprankedsent();
		st.processlsh("/home/bhargava/Documents/afghanresults"+"/","rankedevaltrecfeaturesner2012-02-27-23.txt");
		long endtime=System.currentTimeMillis();
		System.out.println(endtime-starttime);*/
	}
}