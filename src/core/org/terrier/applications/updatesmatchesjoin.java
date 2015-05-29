package org.terrier.applications;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.terrier.applications.matchattributes;
import org.terrier.applications.updateattributes;

class updatesmatchesjoin
{
	private static ArrayList<matchattributes> match_trec;
	private static ArrayList<updateattributes> update_trec;
	private static HashSet<String> sentences;
	public static void readtrecmatches()
	{
		BufferedReader matchtrecread=null;
		String line;
		//System.out.println("here");
		match_trec=new ArrayList<matchattributes>();
			try {
				 matchtrecread = new BufferedReader(new FileReader(new File("/home/bhargava/Downloads/matches (2).tsv")));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				while((line=matchtrecread.readLine())!=null)
				{System.out.println("here");
				String[] attributes=line.split("\t");
				matchattributes m=new matchattributes();
				m.qid=attributes[0];
				m.uid=attributes[1];
				m.nid=attributes[2];
				m.ustart=attributes[3];
				m.uend=attributes[4];
				m.autop=attributes[5];
				match_trec.add(m);
		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				matchtrecread.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static void readtrecupdate()
	{BufferedReader updatetrecread=null;
	String line;
	//System.out.println("here");
	update_trec=new ArrayList<updateattributes>();
		try {
			 updatetrecread = new BufferedReader(new FileReader(new File("/home/bhargava/Downloads/updates_sampled.extended.tsv")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while((line=updatetrecread.readLine())!=null)
			{System.out.println("here");
			String[] attributes=line.split("\t");
			updateattributes u=new updateattributes();
			u.qid=attributes[0];
			u.uid=attributes[1];
			u.did=attributes[2];
			u.sid=attributes[3];
			u.ulength=attributes[4];
			u.dupid=attributes[5];
			u.utext=attributes[6];
			update_trec.add(u);
	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			updatetrecread.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{readtrecmatches();
	readtrecupdate();
	sentences=new HashSet<String>();
		for(int i=0;i<update_trec.size();i++)
		{if(update_trec.get(i).qid.equals("TS14.16"))
		{
			for(int j=0;j<match_trec.size();j++)
			{
				if(match_trec.get(j).uid.equals(update_trec.get(i).uid))
					{
					if(sentences.add(update_trec.get(i).utext))
						System.out.println(update_trec.get(i).utext);
					}
				
			}
		}
		}
		
	}
}