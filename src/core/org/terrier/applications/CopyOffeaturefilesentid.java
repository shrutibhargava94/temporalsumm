package org.terrier.applications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

class CopyOffeaturefilesentid
{int sentencepos;
private String htmlfilterpath="/home/bhargava/Documents/afghanistanboilerplate/htmlfilter/";
private ArrayList<String> nonboilerplatesentences;
	public void computefeaturesforfile(String string, String files, ArrayList<String> querycontent, HashMap<String, Double> hourfreq, HashMap<String, Double> topicweight, HashMap<String, Double> freq, BufferedWriter bw, int topic, HashMap<String, sentencerank> rankmap) {
		// TODO Auto-generated method stub
		File article=new File(string+"/"+files);
		preprocessnewspaperoutput(files, string);
		/*try {
			Document doc = Jsoup.parse(article, "UTF-8");
			String text = doc.body().text();
			Properties props = new Properties();
		    props.setProperty("annotators", "tokenize, ssplit, pos, lemma,ner");
		    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		    
		    // read some text in the text variable
		    // Add your text here!
		    
		    // create an empty Annotation just with the given text
		    Annotation document = new Annotation(text);
		    
		    // run all Annotators on this text
		    pipeline.annotate(document);
		    
		    // these are all the sentences in this document
		    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		  sentencepos=0;
		    for(CoreMap sentence: sentences) {
		    	featuresentence sent=new featuresentence(sentencepos,sentence,querycontent,hourfreq,topicweight,freq,bw,topic,rankmap);
		    	sent.computesentencefeatures();
		    	//sent.computesentencefeaturestraining();
		    	sentencepos++;
		    }
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		FileReader fr = null;
		try {
			fr = new FileReader(article);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader articlebr=new BufferedReader(fr);
		String line="";
		try {int sentencepos=0;
			while((line=articlebr.readLine())!=null)
			{//System.out.println(line);
				if(check(line))
			{
				CopyOffeaturesentencesentid sent=new CopyOffeaturesentencesentid(sentencepos,line,querycontent,hourfreq,topicweight,freq,bw,topic,rankmap,files);
		    	sent.computesentencefeatures();
		    	
			}
			sentencepos++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void preprocessnewspaperoutput(String files,String hourpath)
	{String[] hourname=hourpath.split("/");
	String[] filename=files.split("@");
		File article=new File(htmlfilterpath+hourname[hourname.length-1]+"/"+filename[1].replace("txt", "html"));
	Document doc = null;
	try {
		doc = Jsoup.parse(article, "UTF-8");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String text = doc.body().text();
	Properties props = new Properties();
	  props.setProperty("annotators", "tokenize, ssplit");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    // read some text in the text variable
	    // Add your text here!
	    
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	  sentencepos=0;
	  nonboilerplatesentences=new ArrayList<String>();
	  for(CoreMap sentence: sentences) {
	  nonboilerplatesentences.add(sentence.toString());
	  //System.out.println(sentence.toString());
	  }
	   
	    
		
	}double jaccard(List<String> s1, List<String> s2)
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
	private boolean check(String line) {
		List<String> lineplit = Arrays.asList(line.split(" "));
		for(int i=0;i<nonboilerplatesentences.size();i++)
		{	List<String> goodsent = Arrays.asList(nonboilerplatesentences.get(i).split(" "));
			if(jaccard(lineplit,goodsent)>0.70)
			{return true;
				
			}
		}
		
		return false;
		// TODO Auto-generated method stub
		/*if(line.contains("http"))
		return false;
		if(line.contains("iref"))
		return false;
		if(line.contains("|"))
		return false;
		if(line.contains("/"))
		return false;
		
		
		int spaces = line == null ? 0 : line.length() - line.replace(" ", "").length();
		if(spaces<2)
			return false;
		
		return true;*/
		
	}
}