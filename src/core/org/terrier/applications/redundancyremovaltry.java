package org.terrier.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

class redundancyremovaltry
{
	public static void main(String args[])
	{   ArrayList<String> summ=new ArrayList<String>();
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
		try {
			line=br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		summ.add(line);
		try {
			while((line=br.readLine())!=null)
			{flag=0;
			String[] line1=line.split("\t");
			ArrayList<String> linewords = stemmingandstopwordremovaltry.content(line1[2]);
			if((linewords.size()<35)&&(linewords.size()>0))
			{
			for(int i=0;i<summ.size();i++)
			{Double cos=cosinetf(linewords,summ.get(i));
				if((cos>0.03))
					flag=1;
			}
			
			if(flag==0)
			{
				summ.add(line1[2]);
				System.out.println(line);
			}
			}}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
