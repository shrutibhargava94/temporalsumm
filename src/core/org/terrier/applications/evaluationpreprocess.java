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

class evaluationpreprocess
{
	public static void main(String args[])
	{ArrayList<runattributes> actualupdates=new ArrayList<runattributes>();
		String path = "/home/bhargava/Documents/afghanistanfull/afghansumm/afghanboilupdateseval.tsv";
		BufferedReader runmake=null;
		try {
			 runmake=new BufferedReader(new FileReader(new File(path)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String line="";
		try {
			while((line=runmake.readLine())!=null)
			{String[] runattributes=line.split("\t");
				runattributes r=new runattributes();
				r.qid=runattributes[0];
				r.tid=runattributes[1];
				r.rid=runattributes[2];
				r.did=runattributes[3];
				r.sid=runattributes[4];
				r.timestamp=runattributes[6];
				r.confidence=runattributes[7];
				r.sent=runattributes[5];
				System.out.println(r.sent);
				actualupdates.add(r);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			runmake.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(actualupdates,new Comparator<runattributes>(){
	    	public int compare(runattributes s1,runattributes s2)
	    	{Double score1=Double.parseDouble(s1.confidence);
	    	Double score2=Double.parseDouble(s2.confidence);
	    		
	    		if(score1.equals(score2))
	    		return s1.timestamp.compareTo(s2.timestamp);
	    	else
	    		return score2.compareTo(score1);
	    	}
	    });
		
		BufferedWriter updates=null;
		try {
			 updates=new BufferedWriter(new FileWriter(new File("/home/bhargava/Documents/afghanistanfull/afghansumm/top60updatesafghan.tsv")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}int ct=0;
		for(int i=0;i<actualupdates.size();i++)
		{System.out.println(actualupdates.get(i).sent);
			try {
				updates.write(actualupdates.get(i).qid+"\t"+actualupdates.get(i).tid+"\t"+actualupdates.get(i).rid+"\t"+actualupdates.get(i).did+"\t"+actualupdates.get(i).sid+"\t"+actualupdates.get(i).sent+"\t"+actualupdates.get(i).timestamp+"\t"+actualupdates.get(i).confidence);
				updates.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ct++;
			if(ct==60)
				break;
		}
		try {
			updates.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}