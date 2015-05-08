package org.terrier.applications;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import weka.core.Utils;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

class CopyOffeaturesentencesentid
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
CoreMap sentencecoremap;
private double location;
private double time;
private double date;
private double duration;
private double money;
private String docid;
	public CopyOffeaturesentencesentid(int sentencepos, String line, ArrayList<String> querycontent, HashMap<String, Double> hourfreq,HashMap<String,Double>topicweight, HashMap<String, Double> freq, BufferedWriter bw, int topic, HashMap<String, sentencerank> rankmap, String files) {
		// TODO Auto-generated constructor stub
		absoluteposition=(double)sentencepos;
		sentence=line.toString();
		this.querycontent=querycontent;
		this.hourfreq=hourfreq;
		this.topicweight=topicweight;
		this.freq=freq;
		this.bw=bw;
		this.topic=topic;
		this.rankmap=rankmap;
		this.docid=files.substring(0,files.indexOf("."));
		//this.sentencecoremap=line;
		location=0.0;
		time=0.0;
		money=0.0;
		date=0.0;
		duration=0.0;
	
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
		ner();
		
		 System.out.println(sentence+" "+absoluteposition+" "+numberofcontentwords+" "+unigramoverlap+" "+sumbasic+" "+sumfocus+" "+mutualinfo);
		 try {String sentenceescaped=Utils.quote(sentence);
			bw.write("0"+" qid:"+topic+" 1:"+absoluteposition+" 2:"+numberofcontentwords+" 3:"+unigramoverlap+" 4:"+sumbasic+" 5:"+sumfocus+" 6:"+mutualinfo+" 7:"+location+" 8:"+duration+" 9:"+time+" 10:"+money+" 11:"+date+" #"+docid+"\t"+sentence);
			//bw.write(topic+","+absoluteposition+","+numberofcontentwords+","+unigramoverlap+","+sumbasic+","+sumfocus+","+mutualinfo+","+location+","+duration+","+time+","+money+","+date);
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
		ner();
		 System.out.println(sentence+" "+absoluteposition+" "+numberofcontentwords+" "+unigramoverlap+" "+sumbasic+" "+sumfocus+" "+mutualinfo);
		// System.out.println(rankmap);
		 try {if(rankmap.containsKey(sentence))//why wont a sentence be in this rankmap?
			 bw.write(rankmap.get(sentence).rank+" qid:"+rankmap.get(sentence).topic+" 1:"+absoluteposition+" 2:"+numberofcontentwords+" 3:"+unigramoverlap+" 4:"+sumbasic+" 5:"+sumfocus+" 6:"+mutualinfo+" 7:"+location+" 8:"+duration+" 9:"+time+" 10:"+money+" 11:"+date+" #"+sentence);
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
	public void ner()
	{
		Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma,ner");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    // read some text in the text variable
	    // Add your text here!
	    
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(sentence);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
		for (CoreLabel token: document.get(TokensAnnotation.class)) {
	       
	       
	        // this is the NER label of the token
	        String ne = token.get(NamedEntityTagAnnotation.class); 
	        if(ne=="LOCATION")
	        	location=1.0;
			if(ne=="DATE")
	        	date=1.0;
	        if(ne=="TIME")
	        	time=1.0;
	        if(ne=="DURATION")
	        	duration=1.0;	
	        if(ne=="MONEY")
	        	money=1.0;
	       
	       
	      }
	}
	
}