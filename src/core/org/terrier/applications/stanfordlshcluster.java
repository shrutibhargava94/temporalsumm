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
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import lsh.minhash.JaccardDocument;
import lsh.minhash.MinHashLSH;
import lsh.minhash.MinHashLSH.RankedDocument;
import tuan.ml.Features;
import tuan.ml.FeaturesFactory;

class stanfordlshcluster
{Integer[] mark;
	public static void main(String args[])
	{
		/*String path="/home/bhargava/Documents/afghanresults";
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
		{stanfordlshcluster st=new stanfordlshcluster();
		st.processlsh(path+"/",files[file]);
		}
			
		}*/
		long starttime=System.currentTimeMillis();
		stanfordlshcluster st=new stanfordlshcluster();
		st.processlsh("/home/bhargava/Documents/afghanresults"+"/","rankedevaltrecfeaturesner2012-02-26-14.txt");
		long endtime=System.currentTimeMillis();
		System.out.println(endtime-starttime);
	}
	private HashSet<String> termspace;
	private ArrayList<runattributes> rankedsent;
	private ArrayList<ArrayList<runattributes>> clusters;

	private void processlsh(String string, String string2) {BufferedWriter bw = null;
	try {
		 bw=new BufferedWriter(new FileWriter(new File("/home/bhargava/Documents/afghansumm/"+string2)));
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}long starttime=System.currentTimeMillis();
		readfileandtermspace(string+string2);
		long endtime=System.currentTimeMillis();
		System.out.println(endtime-starttime);
		MinHashLSH minhash = new MinHashLSH(100,5,100,100000000);
		minhash.setThreshold(0.8d, Double.MIN_VALUE);
		//term space
	ArrayList<ArrayList<Double>> vals = new ArrayList<ArrayList<Double>>();
		for(int i=0;i<rankedsent.size();i++)
		{double[] val=new double[termspace.size()];
			HashMap<String, Double> sentfreq = getsentencefreq(rankedsent.get(i).sent);
			
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
			vals.add((ArrayList)Arrays.asList(val));
			Features f=FeaturesFactory.newInstance(val);
			JaccardDocument d1 = new JaccardDocument(rankedsent.get(i).sent, f, termspace.size());
			minhash.put(d1);
			
		}
		
		 mark=new Integer[rankedsent.size()];
		 Arrays.fill(mark, 0);
		 int i=0;
		 clusters=new ArrayList<ArrayList<runattributes>>();
	while(!allmarked())
	{if(mark[i]==0)
	{mark[i]=1;
		Features f=FeaturesFactory.newInstance((double[])vals.get(i).toArray());
		JaccardDocument d1 = new JaccardDocument(rankedsent.get(i).sent, f, termspace.size());
		
		Iterator<RankedDocument> result = (Iterator<RankedDocument>) minhash.neighbours(d1);
		
		ArrayList<runattributes> cluster = new ArrayList<runattributes>();
		System.out.println("cluster");
		cluster.add(rankedsent.get(i));
		System.out.println(rankedsent.get(i).sent);
		while (result.hasNext()) {
		RankedDocument item = result.next();
	for(int j=0;j<rankedsent.size();j++)
	{
		if(rankedsent.get(j).sent.equals(item.item().key()))
		{	mark[j]=1;
		cluster.add(rankedsent.get(j));
		System.out.println(rankedsent.get(j).sent);
		}
	}
		//System.out.println(item.item().key() + ": " + String.valueOf(item.score()));
		
	}if(cluster.size()>rankedsent.size()/10)
		{System.out.println(cluster.get(0).sent);try {
			bw.write(cluster.get(0).sent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}try {
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		clusters.add(cluster);
	}
	else
	{
	mark[i]=1;
	Features f=FeaturesFactory.newInstance(vals[i]);
	JaccardDocument d1 = new JaccardDocument(rankedsent.get(i).sent, f, termspace.size());
	
	Iterator<RankedDocument> result = (Iterator<RankedDocument>) minhash.neighbours(d1);
	
	
	while (result.hasNext()) {
	RankedDocument item = result.next();
for(int j=0;j<rankedsent.size();j++)
{
	if(rankedsent.get(j).sent.equals(item.item().key()))
	{	mark[j]=1;
	
	}
}
	//System.out.println(item.item().key() + ": " + String.valueOf(item.score()));
	
}
	}
	i++;
	}
	try {
		bw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	public boolean allmarked()
	{for(int i=0;i<mark.length;i++)
	{
		if(mark[i]==0)
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
	public  void readfileandtermspace(String filename)
	{termspace=new HashSet<String>();
		BufferedReader br=null;
		try {
			br=new BufferedReader(new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rankedsent=new ArrayList<runattributes>();
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
				rankedsent.add(r);
				HashSet<String> hb = null;
				hb = new HashSet<String>(sentencecontent);
			
				termspace.addAll(hb);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}