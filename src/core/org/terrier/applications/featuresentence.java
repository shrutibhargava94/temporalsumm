package org.terrier.applications;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

class featuresentence
{double absoluteposition;
double numberofcontentwords;
String sentence;
ArrayList<String> sentencecontent;
 ArrayList<String> querycontent;
 double unigramoverlap;
private HashMap<String, Double> hourfreq;
private double sumbasic;
private double N;
private ArrayList<String> topicontent;
private double sumfocus;
private HashMap<String, Double> topicweight;
private HashMap<String, Double> freq;
private double mutualinfo;
private HashMap<String, Double> sentencefreq;
private BufferedWriter bw;
private int topic;
private HashMap<String, sentencerank> rankmap;
	public featuresentence(int sentencepos, String string, ArrayList<String> querycontent, HashMap<String, Double> hourfreq,HashMap<String,Double>topicweight, HashMap<String, Double> freq, BufferedWriter bw, int topic, HashMap<String, sentencerank> rankmap) {
		// TODO Auto-generated constructor stub
		absoluteposition=(double)sentencepos;
		sentence=string;
		this.querycontent=querycontent;
		this.hourfreq=hourfreq;
		this.topicweight=topicweight;
		this.freq=freq;
		this.bw=bw;
		this.topic=topic;
		this.rankmap=rankmap;
	}
public void preprocesssentence()
{
	try {
		sentencecontent=stemmingandstopwordremovaltry.content(sentence);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	public void computesentencefeatures()
	{
		preprocesssentence();
		numberofcontentwords=sentencecontent.size();
		unigramoverlap();
		sumbasic();
		sumfocus();
		mutualinfo();
		 System.out.println(sentence+" "+absoluteposition+" "+numberofcontentwords+" "+unigramoverlap+" "+sumbasic+" "+sumfocus+" "+mutualinfo);
		 try {
			bw.write("0"+" qid:"+topic+" 1:"+absoluteposition+" 2:"+numberofcontentwords+" 3:"+unigramoverlap+" 4:"+sumbasic+" 5:"+sumfocus+" 6:"+mutualinfo+" #"+sentence);
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
	}
	public void computesentencefeaturestraining()
	{
		preprocesssentence();
		numberofcontentwords=sentencecontent.size();
		unigramoverlap();
		sumbasic();
		sumfocus();
		mutualinfo();
		 System.out.println(sentence+" "+absoluteposition+" "+numberofcontentwords+" "+unigramoverlap+" "+sumbasic+" "+sumfocus+" "+mutualinfo);
		// System.out.println(rankmap);
		 try {if(rankmap.containsKey(sentence))//why wont a sentence be in this rankmap?
			bw.write(rankmap.get(sentence).rank+" qid:"+rankmap.get(sentence).topic+" 1:"+absoluteposition+" 2:"+numberofcontentwords+" 3:"+unigramoverlap+" 4:"+sumbasic+" 5:"+sumfocus+" 6:"+mutualinfo+" #"+sentence);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {if(rankmap.containsKey(sentence))//why wont a sentence be in this rankmap?
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void unigramoverlap()
	{
		HashSet<String> ha=new HashSet<String>(querycontent);
		HashSet<String> hb=new HashSet<String>(sentencecontent);
		
		HashSet<String> hac=new HashSet<String>(ha) ;
		HashSet<String> hac1=new HashSet<String>(ha) ;
		
		hac.retainAll(hb);
	    hac1.addAll(hb);
	    
	    double jc=(double)hac.size()/(double)hac1.size();
	    unigramoverlap=jc;
	    
	   
	}
	void sumbasic()
	{
		 sumbasic=0.0;N=0.0;
		 for(Entry<String, Double> entry:hourfreq.entrySet())
		 {
			 N=N+entry.getValue();
		 }
		for(int i=0;i<sentencecontent.size();i++)
		{if(hourfreq.containsKey(sentencecontent.get(i)))
		{
			sumbasic=sumbasic+hourfreq.get(sentencecontent.get(i))/N;
		}
		}
	}
	void sumfocus()
	{
		 sumfocus=0.0;
		for(int i=0;i<sentencecontent.size();i++)
		{if(hourfreq.containsKey(sentencecontent.get(i)))
		{if(topicweight.containsKey(sentencecontent.get(i)))
		{sumfocus=sumfocus+0.1*hourfreq.get(sentencecontent.get(i))/N+0.9*topicweight.get(sentencecontent.get(i));//0.1,0.9 emprically determined in sumfocus paper
			
		}else
		{
			sumfocus=sumfocus+0.1*hourfreq.get(sentencecontent.get(i))/N;
		}
		}
		}
	}
	void mutualinfo()
	{double denom=0.0;mutualinfo=0.0;
	double pw = 0,pwi,psnum=0.0;
	double ps=0.0;
		for(Entry<String,Double> entry:freq.entrySet())
		{
			denom=denom+entry.getValue();
		}sentencefreq=new HashMap<String, Double>();
		for(int i=0;i<sentencecontent.size();i++)
		{
			if(sentencefreq.containsKey(sentencecontent.get(i)))
			{
		sentencefreq.put(sentencecontent.get(i), sentencefreq.get(sentencecontent.get(i))+1.0);
			}
	else
		sentencefreq.put(sentencecontent.get(i),1.0);
			ps++;
		}
		 ps= ps/denom;
		for(Entry<String,Double> entry:sentencefreq.entrySet())
		{if(freq.containsKey(entry.getKey()))
		{
			 pw= entry.getValue()/denom;
			 pwi=freq.get(entry.getKey())/denom;
			 double pij = Math.log10(pw/(pwi*ps));
			 
			if(pij>0)
				 mutualinfo=mutualinfo+pij;
		}
		}
	}
	
}