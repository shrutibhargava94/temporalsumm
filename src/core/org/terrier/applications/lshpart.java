package org.terrier.applications;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

class lshpart
{static HashSet<String> termspace;
public static HashMap<String,runattributes> rankedsent;
public static void readfileandtermspace(String filename)
{termspace=new HashSet<String>();
rankedsent=new HashMap<String, runattributes>();
	BufferedReader br=null;
	try {
		br=new BufferedReader(new FileReader(new File(filename)));
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
			
			
			ArrayList<String> sentencecontent = stemmingandstopwordremovaltry.content(split[5]);
			//if(sentencecontent.size()<1)
			//continue;
			runattributes r=new runattributes();
			r.qid=split[0];
			r.tid=split[1];
			r.rid=split[2];
			r.did=split[3];
			r.sid=split[4];
			r.sent=split[5];
			r.timestamp=split[6];
			r.confidence=split[7];
			rankedsent.put(split[5], r);
			
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
		minhash.setThreshold(0.8d, Double.MIN_VALUE);int i=0;
		double[][] vals = new double[rankedsent.size()][termspace.size()];
		for(Map.Entry<String, runattributes> entry:rankedsent.entrySet())
		{
			HashMap<String, Double> sentfreq = getsentencefreq(entry.getKey());
			
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
			JaccardDocument d1 = new JaccardDocument(entry.getKey(), f, termspace.size());
			minhash.put(d1);
			i++;
			
		}/*int ct=0;
		System.out.println(ranking.size());
		for(int i=0;i<ranking.size();i++)
		{System.out.println(ranking.get(i)+" "+score.get(i));
			Features f=FeaturesFactory.newInstance(vals[i]);
			JaccardDocument d1 = new JaccardDocument(ranking.get(i), f, termspace.size());
		
		System.out.println("In sorted order: ");
		Iterator<RankedDocument> result = (Iterator<RankedDocument>) minhash.neighbours(d1);
		
		while (result.hasNext()) {
			RankedDocument item = result.next();
			//System.out.println(item.hashCode());
		//if(ct==
			System.out.println(ranking.indexOf(item.item().key()));
	score.remove(ranking.indexOf(item.item().key()));
			ranking.remove(item.item().key());
			
			
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
			}*/i=0;
		ArrayList<ArrayList<runattributes>> clusters=new ArrayList<ArrayList<runattributes>>();
		for(Map.Entry<String, runattributes> entry:rankedsent.entrySet())
		{int flag=1;
		if(i==0)
			flag=0;
		System.out.println("here");
			for(int j=0;j<clusters.size();j++)
				if(clusters.get(j).contains(entry.getValue()))
				{
					flag=0;
					break;
				}
			if(flag==1)
			{System.out.println("here in if");
				ArrayList<runattributes> cluster=new ArrayList<runattributes>();
			Features f1=FeaturesFactory.newInstance(vals[i]);
			JaccardDocument d11 = new JaccardDocument(entry.getKey(), f1, termspace.size());
			Iterator<RankedDocument> result1 = (Iterator<RankedDocument>) minhash.neighbours(d11);
			
			while (result1.hasNext()) {int flag1=1;
				RankedDocument item = result1.next();
			
				cluster.add(rankedsent.get(item.item().key()));
				
				//System.out.println(item.item().key() + ": " + String.valueOf(item.score()));
			}Collections.sort(cluster,new Comparator<runattributes>(){
		    	public int compare(runattributes s1,runattributes s2)
		    	{Double score1=Double.parseDouble(s1.confidence);
		    	Double score2=Double.parseDouble(s2.confidence);
		    	return score2.compareTo(score1);
		    	}
		    });
			clusters.add(cluster);
			}
		i++;}
		ArrayList<runattributes> setoftoprankedsentences=new ArrayList<runattributes>();
		int cluscount=0;
		for(int clus=0;clus<clusters.size();clus++)
		{//System.out.println(cluscount);
	//	if(clusters.get(clus).size()>10)
		{System.out.println(cluscount);
			for(int sent=0;sent<clusters.get(clus).size();sent++)
				{setoftoprankedsentences.add(clusters.get(clus).get(sent));System.out.println(clusters.get(clus).get(sent).sent+" "+clusters.get(clus).get(sent).confidence);break;}
		//System.out.println(clusters.get(clus).get(0).sent);
		}
			
			cluscount++;
		}
		Collections.sort(setoftoprankedsentences,new Comparator<runattributes>(){
	    	public int compare(runattributes s1,runattributes s2)
	    	{Double score1=Double.parseDouble(s1.confidence);
	    	Double score2=Double.parseDouble(s2.confidence);
	    	return score2.compareTo(score1);
	    	}
	    });
		for(int j=0;j<setoftoprankedsentences.size();j++)
		{
			System.out.println(setoftoprankedsentences.get(j).sent+" "+setoftoprankedsentences.get(j).confidence);
		}
			
		}

		
	

	public static void getlearningtorankresults(List<sentencescore> ranking) {
		// TODO Auto-generated method stub
		/*termspace=new HashSet<String>();
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
		
		*/
			
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