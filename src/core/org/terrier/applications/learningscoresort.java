package org.terrier.applications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

class learningscoresort
{static List<sentencescore> ranking=new ArrayList<sentencescore>();
private static double lamda=0;
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
public static void mmr(List<sentencescore> s)
{
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
    System.out.println(sel.sentence);
    }
}
	public static void main(String args[])
	{File f=new File("/home/bhargava/Documents/2try/ranked1201normtvskcv10.txt");
	FileWriter fw=null;
	try {
		 fw=new FileWriter(f);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	BufferedWriter bw=new BufferedWriter(fw);
		File f1=new File("/home/bhargava/Documents/scorefiles/kcvtvsmodel10.txt");
		FileReader fr = null;
		try {
			fr = new FileReader(f1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
		File f2=new File("/home/bhargava/Documents/2try/trecfeatures2012-08-12-01.txt");
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
			mmr(ranking);
			for(int i=0;i<ranking.size();i++)
			{
				//System.out.println(i+" "+ranking.get(i).topic+" "+ranking.get(i).sentence);
				try {
					bw.write(i+" "+ranking.get(i).topic+" "+ranking.get(i).sentence);
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
		}
	}
