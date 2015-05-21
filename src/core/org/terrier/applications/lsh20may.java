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
import java.util.HashSet;
import java.util.Iterator;

import lsh.minhash.JaccardDocument;
import lsh.minhash.MinHashLSH;
import lsh.minhash.MinHashLSH.RankedDocument;
import tuan.ml.Features;
import tuan.ml.FeaturesFactory;

class lsh20may
{private  HashSet<String> selectedsentences;
	private  HashSet<String> termspace;
	private  ArrayList<String> ranking;
	private  ArrayList<runattributes> score;
	public  void readfileandtermspace(String filename)
	{termspace=new HashSet<String>();
		BufferedReader br=null;
		try {
			br=new BufferedReader(new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ranking =new ArrayList<String>();
		score=new ArrayList<runattributes>();
		String line="";
		try {
			while((line=br.readLine())!=null)
			{
				String[] split=line.split("\t");
				//System.out.println(split[5]);
				
				
				ArrayList<String> sentencecontent = stemmingandstopwordremovaltry.content(split[5]);
				if((sentencecontent.size()<2)||(line.contains("|")))
				continue;
				ranking.add(split[5]);
				runattributes r=new runattributes();
				r.qid=split[0];
				r.tid=split[1];
				r.rid=split[2];
				r.did=split[3];
				r.sid=split[4];
				r.sent=split[5];
				r.timestamp=split[6];
				r.confidence=split[7];
				score.add(r);
				HashSet<String> hb = null;
				hb = new HashSet<String>(sentencecontent);
			
				termspace.addAll(hb);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public  void processlsh(String path,String name)
	{
		//read sentences and other attributes
		
		readfileandtermspace(path+name);
		//LSH initialize
		MinHashLSH minhash = new MinHashLSH(100,5,100,100000000);
		minhash.setThreshold(0.8d, Double.MIN_VALUE);
		//term space
		double[][] vals = new double[ranking.size()][termspace.size()];
		for(int i=0;i<ranking.size();i++)
		{
			HashMap<String, Double> sentfreq = getsentencefreq(ranking.get(i));
			
			int count=0;
			Iterator<String> iterator=termspace.iterator();
			while(iterator.hasNext())
			{
				String term=iterator.next();
				if(sentfreq.containsKey(term))
					vals[i][count]=sentfreq.get(term);
				else
					vals[i][count]=0.0;
				count++;
					
			}
			//System.out.println(vals.length);
			Features f=FeaturesFactory.newInstance(vals[i]);
			JaccardDocument d1 = new JaccardDocument(ranking.get(i), f, termspace.size());
			minhash.put(d1);
			
		}selectedsentences=new HashSet<String>();
		for(int i=0;i<ranking.size();i++)
		{
		Features f=FeaturesFactory.newInstance(vals[i]);
		JaccardDocument d1 = new JaccardDocument(ranking.get(i), f, termspace.size());
		
		Iterator<RankedDocument> result = (Iterator<RankedDocument>) minhash.neighbours(d1);

int sizeofneighbours=0;
		while (result.hasNext()) {sizeofneighbours++;
			RankedDocument item = result.next();
			ranking.remove(item.item().key());//addedthis 5.30 20 may
			//System.out.println(item.item().key() + ": " + String.valueOf(item.score()));
			
		}
		//System.out.println(sizeofneighbours);
		if(sizeofneighbours>ranking.size()/10)
		{//System.out.println(ranking.get(i));
			if((i>=0)&&(i<ranking.size()))
		selectedsentences.add(ranking.get(i));
		
		}
		}BufferedWriter bw = null;
		try {
			 bw=new BufferedWriter(new FileWriter(new File("/home/bhargava/Documents/afghansumm/"+name)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
Iterator<String> iterator=selectedsentences.iterator();
while(iterator.hasNext())
{String next=iterator.next();
//	System.out.println(next);
	try {
		bw.write(next);
		bw.newLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
try {
	bw.close();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	
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
	



	public static void main(String args[])
	{String scorepath="/home/bhargava/Documents/afghanresults";
	File scores=new File(scorepath);
	String[] folders=scores.list();
	Collections.sort(Arrays.asList(folders));
	
	for(int file=0;file<folders.length;file++)
	{System.out.println(folders[file]);
		Path path=Paths.get(scorepath+"/"+folders[file]);
		long filesize=0;
		try {
			filesize=Files.size(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	if((folders[file].contains("eval"))&&(filesize>0))
	{lsh20may l=new lsh20may();
	l.processlsh(scorepath+"/",folders[file]);
	}
		
	}
	}
}