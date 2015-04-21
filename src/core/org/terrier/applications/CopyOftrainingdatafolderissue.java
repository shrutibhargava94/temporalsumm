package org.terrier.applications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

class CopyOftrainingdatafolderissue
{static String datapath="/home/bhargava/Documents/firstsetuptry/DUCcontent/";
static String summarypath="/home/bhargava/Documents/firstsetuptry/models";
static ArrayList<ArrayList<HashMap<String,Integer>>> summfreq=new ArrayList<ArrayList<HashMap<String,Integer>>>();
static ArrayList<ArrayList<Integer>> modhi=new ArrayList<ArrayList<Integer>>();
static double lamda=0.3;
	public static void processsummaries()
	{int topic=0;
	int human=0;
		Path dirtopic=Paths.get(summarypath);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirtopic)) {
		    for (Path file: stream){//System.out.println(file);
		    	 ArrayList<HashMap<String,Integer>> summfreqtopic=new ArrayList<HashMap<String,Integer>>();
		    	summfreqtopic.add(processsummary(file,human,topic));
		    	human++;
		    	if(human==4)
		    		{summfreq.add(topic, summfreqtopic);human=0;
		    		topic++;}
		    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<summfreq.size();i++)
		{ArrayList<Integer> hi=new ArrayList<Integer>();
			for(int j=0;j<summfreq.get(i).size();j++)
			{int sum=0;
				for(Map.Entry<String, Integer> entry: summfreq.get(i).get(j).entrySet())
				{
					sum=sum+entry.getValue();
				}
				hi.add(sum);
			}
				
			modhi.add(hi);
		}
	}
	public static HashMap<String,Integer> processsummary(Path file, int human, int topic)
	{HashMap<String,Integer> freq=new HashMap<String,Integer>();
		File f=new File(file.toString());
		FileReader fr = null;
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
		String curr;
		
		try {
			while((curr=br.readLine())!=null )
			{ArrayList<String> temp;
				temp=stemmingandstopwordremovaltry.content(curr);
				for(int i=0;i<temp.size();i++)
				{
					if(freq.containsKey(temp.get(i)))
						freq.put(temp.get(i),freq.get(temp.get(i))+1 );
					else
						freq.put(temp.get(i),1 );
						
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return freq;
		
		
	}
	
	public static void processfiles(StanfordCoreNLP pipeline)
	{File f=new File("/home/bhargava/Documents/firstsetuptry/duc2007nonsorted.txt");
	FileWriter fw=null;
	try {
		 fw=new FileWriter(f);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	BufferedWriter bw=new BufferedWriter(fw);
	File f1=new File("/home/bhargava/Documents/firstsetuptry/duc2007_mmr.txt");
	FileWriter fw1=null;
	try {
		 fw1=new FileWriter(f1);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	BufferedWriter bw1=new BufferedWriter(fw1);
		//Path dirtopic=Paths.get(datapath);
		int topic=0;
		File dirtopic=new File(datapath);
		String[] folders=dirtopic.list();
		Collections.sort(Arrays.asList(folders));
		System.out.println("middle");
		for(int i=0;i<folders.length;i++)
		{System.out.println(folders[i]);
		    ArrayList<sentencescore> s=new ArrayList<sentencescore>();
		    	 //Path folder=Paths.get(datapath+folders[i]);
				File file=new File(datapath+folders[i]);
				String[] files=file.list();
				Collections.sort(Arrays.asList(files));
				for(int j=0;j<files.length;j++){//System.out.println("inner");
				    	String text = null;
						try {Path filep=Paths.get(datapath+folders[i]+"/"+files[j]);
							text = new String(Files.readAllBytes(filep), StandardCharsets.UTF_8);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	 Annotation document = new Annotation(text);
				    	    
				    	    // run all Annotators on this text
				    	    pipeline.annotate(document);
				    	    
				    	    // these are all the sentences in this document
				    	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
				    	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
				    	    
				    	    for(CoreMap sentence: sentences) {
				    	    	double score=processsentence(sentence,topic);
				    	    	//System.out.println(topic+" "+score+" "+sentence.toString());
				    	    	//bw.write(topic+"?"+score+"?"+sentence.toString());
				    	    	//bw.newLine();
				    	    	sentencescore s1=new sentencescore();
				    	    	s1.score=score;
				    	    	s1.sentence=sentence.toString();
				    	    	s1.topic=topic;
				    	    	s.add(s1);
				    	    }
				    }
				    /*Collections.sort(s,new Comparator<sentencescore>(){
				    	public int compare(sentencescore s1,sentencescore s2)
				    	{
				    		return s2.score.compareTo(s1.score);
				    	}
				    });*/
				    for(int l=0;l<s.size();l++)
				    {
				    	try {
							bw.write(l+"\t"+s.get(l).topic+"\t"+s.get(l).score+"\t"+s.get(l).sentence);
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
				    /*
				    //sorting
				    for(int l=0;l<s.size()-1;l++)
				    {double min=s.get(l).score;
				   int pos=l;
				  
				 
				    	for(int m=l+1;m<s.size()-1;m++)
				    		{if(s.get(m).score>min)
				    		{
				    			min=s.get(m).score;
				    			pos=m;
				    		}
				    		}
				    		
				    	sentencescore temp = s.get(l);
				    	sentencescore temp2=s.get(pos);
				    	s.remove(l);
				    	s.remove(pos);
				    	s.add(l, temp2);
				    	s.add(pos,temp);
				    	
				    }
				    //mmr
				    ArrayList<sentencescore> selected=new ArrayList<sentencescore>();
				    selected.add(s.get(0));
				    for(int k=0;k<11;k++)
				    {
				    double outermax=-9999;
				    int pos=-1;
				    for(int i=1;i<s.size();i++)
				    {
				    double max =-9999; 
				    	if(!selected.contains(s.get(i)))
				    			{ 
				    		max=-9999;
				    			
				    		for(int j=1;j<selected.size();j++)
				    		{
				    			double jc=jc_check(s.get(i).sentence,selected.get(j).sentence);
				    			if(jc>max)
				    			{
				    				max=jc;
				    				
				    			}
				    			
				    		}
				    		
				    			}
				    	double scoremmr=lamda*s.get(i).score-(1.0-lamda)*max;
				    	if(scoremmr>outermax)
				    	{
				    		outermax=scoremmr;
				    		pos=i;
				    	}
				    }
				    sentencescore sel=new sentencescore();
				    sel.score=outermax;
				    sel.sentence=s.get(pos).sentence;
				    sel.topic=s.get(pos).topic;
				    selected.add(sel);
				    
				    }//mmrends
				    //sorting mmr scores
				    int ct=0;
				    for(int l=0;l<selected.size()-1;l++)
				    {double min=selected.get(l).score;
				   int pos=l;
				   System.out.println(ct);
				   ct++;
				    	for(int m=l+1;m<selected.size()-1;m++)
				    		{if(selected.get(m).score>min)
				    		{
				    			min=selected.get(m).score;
				    			pos=m;
				    		}
				    		}
				    		
				    	sentencescore temp = selected.get(l);
				    	sentencescore temp2=selected.get(pos);
				    	selected.remove(l);
				    	selected.remove(pos);
				    	selected.add(l, temp2);
				    	selected.add(pos,temp);
				    	
				    }
				    //writing mmr scores
				    for(int l=0;l<selected.size();l++)
				    {
				    	bw1.write(l+"?"+selected.get(l).topic+"?"+selected.get(l).score+"?"+selected.get(l).sentence);
				    	bw1.newLine();
				    }*/
				   
				    topic++;
		    }
		    
		try {
			bw.close();
			 bw1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
	}
	public static double jc_check(String ta,String tb)
	{ HashSet<String> ha = null;
	try {
		ha = new HashSet<String>(stemmingandstopwordremovaltry.content(ta));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	HashSet<String> hb = null;
	try {
		hb = new HashSet<String>(stemmingandstopwordremovaltry.content(tb));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	HashSet<String> hac=new HashSet<String>(ha) ;
	HashSet<String> hac1=new HashSet<String>(ha) ;
	
	hac.retainAll(hb);
    hac1.addAll(hb);
    
    double jc=(double)hac.size()/(double)hac1.size();
    return jc;
	
	}
	private static double processsentence(CoreMap sentence, int topic) {
		double score=0;
		// TODO Auto-generated method stub
		try {
			ArrayList<String> unigrams=stemmingandstopwordremovaltry.content(sentence.toString());
			for(int i=0;i<unigrams.size();i++)
			{
				for(int j=0;j<summfreq.get(topic).size();j++)
				{if(summfreq.get(topic).get(j).containsKey(unigrams.get(i)))
					score=score+(double)summfreq.get(topic).get(j).get(unigrams.get(i))/(double)modhi.get(topic).get(j);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return score;
	}
	public static void main(String args[])
	{ Properties props = new Properties();
    props.setProperty("annotators", "tokenize, ssplit");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		processsummaries();
		processfiles(pipeline);
		
	}
}