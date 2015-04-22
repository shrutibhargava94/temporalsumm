package org.terrier.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class learningscoresort
{static List<sentencescore> ranking=new ArrayList<sentencescore>();
	public static void main(String args[])
	{
		File f1=new File("/home/bhargava/Documents/firstsetuptry/myscorefile.txt");
		FileReader fr = null;
		try {
			fr = new FileReader(f1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
		File f2=new File("/home/bhargava/Documents/firstsetuptry/test1.txt");
		FileReader fr1 = null;
		try {
			fr1 = new FileReader(f2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br1=new BufferedReader(fr1);
		String line;
		//rankmap=new HashMap<String, sentencerank>();
		
			try {
				while((line=br.readLine())!=null)
				{String[] score=line.split("\t");
				sentencescore ss=new sentencescore();
				ss.topic=Integer.parseInt(score[0]);
				ss.score=Double.parseDouble(score[2]);
				String line1=br1.readLine();
				String[] sent=line1.split("#");
				ss.sentence=sent[1];
				ranking.add(ss);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Collections.sort(ranking,new Comparator<sentencescore>(){
		    	public int compare(sentencescore s1,sentencescore s2)
		    	{if(s1.topic==s2.topic)
		    		return s2.score.compareTo(s1.score);
		    	else
		    		return s1.topic-s2.topic;
		    	}
		    });
			
			for(int i=0;i<ranking.size();i++)
			{
				System.out.println(i+" "+ranking.get(i).topic+" "+ranking.get(i).sentence);
			}
		}
	}
