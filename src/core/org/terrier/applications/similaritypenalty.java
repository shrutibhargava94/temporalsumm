package org.terrier.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

class similaritypenalty
{
	public static void main(String args[])
	{
		ArrayList<String> summ=new ArrayList<String>();
		ArrayList<sentencescore> linescore=new ArrayList<sentencescore>();
		File f=new File("/home/bhargava/Documents/iranbeginresultsquery/rankednertrecfeaturesner2012-08-11-14.txt");
		FileReader fr = null;
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
		int flag=0;
		String line=null;
		
		//summ.add(line);
		
			try {
				while((line=br.readLine())!=null)
				{String[] linesplit=line.split("\t");
				sentencescore s=new sentencescore();
				s.score=Double.parseDouble(linesplit[3]);
				s.topic=Integer.parseInt(linesplit[1]);
				s.sentence=linesplit[2];
				
				
					linescore.add(s);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}int k = 0;
			while(k<30)
			{
				summ.add(linescore.get(0).sentence);
				ArrayList<String> linewords = null;
				try {
					linewords = stemmingandstopwordremovaltry.content(linescore.get(0).sentence);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}if((linewords.size()>3)&&(linewords.size()<30))System.out.println(linescore.get(0).sentence);
				linescore.remove(0);k++;
				
			
				for(int i=0;i<linescore.size();i++)
					linescore.get(i).score=linescore.get(i).score-200*cosinetf(linewords,linescore.get(i).sentence);
				Collections.sort(linescore,new Comparator<sentencescore>(){
			    	public int compare(sentencescore s1,sentencescore s2)
			    	{if(s1.topic==s2.topic)
			    		return s2.score.compareTo(s1.score);
			    	else
			    		return s1.topic-s2.topic;
			    	}
			    });
			}
	
}
	private static double cosinetf(ArrayList<String> linewords, String summ) {
		double similarity = 0;
		// TODO Auto-generated method stub
		try {
			
		
			ArrayList<String> summwords=stemmingandstopwordremovaltry.content(summ);
			HashMap<String, Double> sentencefreq = new HashMap<String, Double>();
			for(int i=0;i<linewords.size();i++)
			{
				if(sentencefreq.containsKey(linewords.get(i)))
				{
			sentencefreq.put(linewords.get(i), sentencefreq.get(linewords.get(i))+1.0);
				}
		else
			sentencefreq.put(linewords.get(i),1.0);
				
			}HashMap<String, Double> sentencefreq1 = new HashMap<String, Double>();
			for(int i=0;i<summwords.size();i++)
			{
				if(sentencefreq1.containsKey(summwords.get(i)))
				{
			sentencefreq1.put(summwords.get(i), sentencefreq1.get(summwords.get(i))+1.0);
				}
		else
			sentencefreq1.put(summwords.get(i),1.0);
				
			}
			for(Entry<String, Double> entry:sentencefreq.entrySet())
			{
				if(sentencefreq1.containsKey(entry.getKey()))
				{
					similarity=similarity+sentencefreq1.get(entry.getKey())*entry.getValue();
				}
			}
				similarity=similarity/(sentencefreq.size()*sentencefreq1.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(similarity);
		return similarity;
		
	}
}