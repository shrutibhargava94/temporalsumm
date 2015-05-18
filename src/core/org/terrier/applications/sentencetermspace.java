package org.terrier.applications;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.stanford.nlp.parser.shiftreduce.FeatureFactory;
import edu.stanford.nlp.parser.shiftreduce.State;
import tuan.ml.ArrayFeatures;
import tuan.ml.Features;
import tuan.ml.FeaturesFactory;
import lsh.minhash.JaccardDocument;
import lsh.minhash.MinHashLSH;
import lsh.minhash.MinHashLSH.RankedDocument;

class sentencetermspace
{static HashSet<String> termspace;
private static List<sentencescore> rankedsentences;
static ArrayList<String> sentences;
private static ArrayList<String> ranking;
private static ArrayList<Double> score;
public static void readfileandtermspace(String filename)
{termspace=new HashSet<String>();
	BufferedReader br=null;
	try {
		br=new BufferedReader(new FileReader(new File(filename)));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	ranking =new ArrayList<String>();
	score=new ArrayList<Double>();
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
			score.add(Double.parseDouble(split[7]));
			HashSet<String> hb = null;
			hb = new HashSet<String>(sentencecontent);
		
			termspace.addAll(hb);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
	public static void main(String args[])
	{Features ffirst;
	//double[][] valsfirst = null;

		readfileandtermspace("/home/bhargava/Documents/afghanistan/afghanresults/rankedevaltrecfeaturesner2012-02-24-02.txt");
		MinHashLSH minhash = new MinHashLSH(100,5,100,100000000);
		minhash.setThreshold(0.8d, Double.MIN_VALUE);
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
			
		}/*int ct=0;
		System.out.println(ranking.size());
		for(int i=0;i<ranking.size();i++)
		{System.out.println(ranking.get(i));
			Features f=FeaturesFactory.newInstance(vals[i]);
			JaccardDocument d1 = new JaccardDocument(ranking.get(i), f, termspace.size());
		/*System.out.println("In arbitrary order: ");
		for (String d : minhash.neighbourKeys(d1)) {
			System.out.println(d);
		}
		System.out.println("In sorted order: ");
		Iterator<RankedDocument> result = (Iterator<RankedDocument>) minhash.neighbours(d1);
		
		while (result.hasNext()) {
			RankedDocument item = result.next();
			//System.out.println(item.hashCode());
		//if(ct==0)
			//System.out.println(item.item().key() + ": " + String.valueOf(item.score()));
			boolean delreturn = ranking.remove(item.item().key());
			
			
		}
		ct++;
		}
		System.out.println(ct);*/
			/*System.out.println("In arbitrary order: ");
			for (String d : minhash.neighbourKeys(d1)) {
				System.out.println(d);
			}*/
			
			/*System.out.println("In sorted order: ");
			JaccardDocument d1;
			Iterator<RankedDocument> result = (Iterator<RankedDocument>) minhash.neighbours(d1);
			while (result.hasNext()) {
				RankedDocument item = result.next();
				System.out.println(item.item().key() + ": " + String.valueOf(item.score()));
			}*/
		Features f=FeaturesFactory.newInstance(vals[0]);
		JaccardDocument d1 = new JaccardDocument(ranking.get(0), f, termspace.size());
		ArrayList<ArrayList<String>> clusters=new ArrayList<ArrayList<String>>();
		ArrayList<String> cluster0=new ArrayList<String>();
		Iterator<RankedDocument> result = (Iterator<RankedDocument>) minhash.neighbours(d1);
		cluster0.add(d1.key().toString());
		while (result.hasNext()) {
			RankedDocument item = result.next();
			cluster0.add(item.item().key());
			//System.out.println(item.item().key() + ": " + String.valueOf(item.score()));
		}
		clusters.add(cluster0);
		for(int i=1;i<ranking.size();i++)
		{int flag=1;
		System.out.println("here");
			for(int j=0;j<clusters.size();j++)
				if(clusters.get(j).contains(ranking.get(i)))
				{
					flag=0;
					break;
				}
			if(flag==1)
			{System.out.println("here in if");
				ArrayList<String> cluster=new ArrayList<String>();
			Features f1=FeaturesFactory.newInstance(vals[i]);
			JaccardDocument d11 = new JaccardDocument(ranking.get(i), f1, termspace.size());
			Iterator<RankedDocument> result1 = (Iterator<RankedDocument>) minhash.neighbours(d11);
			cluster.add(d11.key().toString());
			while (result1.hasNext()) {int flag1=1;
				RankedDocument item = result1.next();
			//	for(int j=0;j<clusters.size();j++)
				//	if(clusters.get(j).contains(ranking.get(i)))
				//	{
					//	flag1=0;
					//break;
					//}
				//if(flag1==1)
				{
				cluster.add(item.item().key());
				}
				//System.out.println(item.item().key() + ": " + String.valueOf(item.score()));
			}
			clusters.add(cluster);
			}
		}
		int cluscount=0;
		for(int clus=0;clus<clusters.size();clus++)
		{//System.out.println(cluscount);
		if(clusters.get(clus).size()>10)
		{System.out.println(cluscount);
			for(int sent=0;sent<clusters.get(clus).size();sent++)
				System.out.println(clusters.get(clus).get(sent)+" "+score.get(ranking.indexOf(clusters.get(clus).get(sent))));
		}
			
			cluscount++;
		}
		
		}

		
	

	public static void getlearningtorankresults(List<sentencescore> ranking) {
		// TODO Auto-generated method stub
		termspace=new HashSet<String>();
		rankedsentences=ranking;
		sentences=new ArrayList<String>();
		for(int i=0;i<ranking.size();i++)
		{
			ArrayList<String> sentencecontent = null;
			try {
				sentencecontent = stemmingandstopwordremovaltry.content(ranking.get(i).sentence);
				sentences.add(ranking.get(i).sentence);
				//System.out.println(ranking.get(i).sentence);
				System.in.read();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HashSet<String> hb = null;
			hb = new HashSet<String>(sentencecontent);
		
			termspace.addAll(hb);
			
		
		}
		
		
			
		//	for(int )
			
		
	}
	
	
	public static HashMap<String,Double> getsentencefreq(String sentence)
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
	
}