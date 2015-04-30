package org.terrier.applications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class learningscoresortmultiavg
{static List<sentencescore> ranking=new ArrayList<sentencescore>();
private static double lamda=0.25;
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
public static void mmr(List<sentencescore> s, BufferedWriter bw2)
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
    try {
		bw2.write(sel.sentence);
		bw2.newLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
}
	public static void process(String folders)
	{File f=new File("/home/bhargava/Documents/iranbeginresultsquery/rankedner"+folders);
	FileWriter fw=null;
	try {
		 fw=new FileWriter(f);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	BufferedWriter bw=new BufferedWriter(fw);
	File f3=new File("/home/bhargava/Documents/iranbeginresultsquery/rankedmmrner"+folders);
	FileWriter fw2=null;
	try {
		 fw2=new FileWriter(f3);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	BufferedWriter bw2=new BufferedWriter(fw2);
		File f1=new File("/home/bhargava/Documents/scorefiles/iranbeginqueryavg/"+folders);
		FileReader fr = null;
		try {
			fr = new FileReader(f1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
		File f2=new File("/home/bhargava/Documents/iranbeginfeaturesqueryonly/"+folders);
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
		int num=0;
			try {
				while((line=br.readLine())!=null)
				{String[] score=line.split("\t");
				sentencescore ss=new sentencescore();
				ss.topic=Integer.parseInt(score[0]);
				ss.score=Double.parseDouble(score[2]);
				String line1=br1.readLine();
				//System.out.println(num);
				String[] sent=line1.split("#");
				ss.sentence=sent[1];
				ranking.add(ss);
				num++;
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
			mmr(ranking,bw2);
			try {
				bw2.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				br1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	public static void main(String args[])
	{  	average();
		String scorepath="/home/bhargava/Documents/scorefiles/iranbeginqueryavg";
		File scores=new File(scorepath);
		String[] folders=scores.list();
		Collections.sort(Arrays.asList(folders));
		
		for(int i=0;i<folders.length;i++)
		{
		
			System.out.println(folders[i]);
		
			process(folders[i]);
		
		}
	}
	private static void average() {
		// TODO Auto-generated method stub
		String scorepath="/home/bhargava/Documents/iranbeginfeaturesqueryonly";
		File scores=new File(scorepath);
		String[] folders=scores.list();
		Collections.sort(Arrays.asList(folders));
		
		for(int i=0;i<folders.length;i++)
		{//if(folders[i].endsWith(".txt"))
		{HashMap<Integer,scorefile> sflist=new HashMap<Integer,scorefile>();
			System.out.println(folders[i]);
		for(int j=0;j<10;j++)
		{int filenum=j+1;
			File f1=new File("/home/bhargava/Documents/scorefiles/iranbeginscorequeryonly/"+folders[i]+filenum);
		FileReader fr = null;
		try {
			fr = new FileReader(f1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
			
			String line=null;
			
			try {
				while((line=br.readLine())!=null)
				{String[] linessplit=line.split("\t");
				scorefile sf=new scorefile();
				sf.topic=Integer.parseInt(linessplit[0]);
				sf.pos=Integer.parseInt(linessplit[1]);
				
					if(sflist.containsKey(sf.pos))
					{
						sf.score=sflist.get(sf.pos).score+Double.parseDouble(linessplit[2]);
						
					}
					else
					{
						sf.score=Double.parseDouble(linessplit[2]);
					}
						
					sflist.put(sf.pos, sf);
					
						
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}	File f=new File("/home/bhargava/Documents/scorefiles/iranbeginqueryavg/"+folders[i]);
		FileWriter fw=null;
		try {
			 fw=new FileWriter(f);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		BufferedWriter bw=new BufferedWriter(fw);
		
		for(Entry<Integer, scorefile> entry: sflist.entrySet())
		{double score=entry.getValue().score/10;
			try {
				bw.write(entry.getValue().topic+"\t"+entry.getValue().pos+"\t"+score);
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
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			//process(folders[i]);
		}
	}
}
}
