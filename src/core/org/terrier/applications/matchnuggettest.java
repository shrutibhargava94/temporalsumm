package org.terrier.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class matchnuggettest
{private static ArrayList<matchattributes> match_trec;
private static ArrayList<nuggetattributes> nugget_trec;
public static void readtrecmatches()
{
	BufferedReader matchtrecread=null;
	String line;
	//System.out.println("here");
	match_trec=new ArrayList<matchattributes>();
		try {
			 matchtrecread = new BufferedReader(new FileReader(new File("/home/bhargava/Downloads/matches (3).tsv")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while((line=matchtrecread.readLine())!=null)
			{//System.out.println("here");
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
public static void trecnuggets()
{
	BufferedReader matchtrecread=null;
	String line;
	//System.out.println("here");
	nugget_trec=new ArrayList<nuggetattributes>();
		try {
			 matchtrecread = new BufferedReader(new FileReader(new File("/home/bhargava/Downloads/nuggets (4).tsv")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while((line=matchtrecread.readLine())!=null)
			{//System.out.println("here");
			String[] attributes=line.split("\t");
			nuggetattributes n=new nuggetattributes();
		n.qid=attributes[0];
		n.nid=attributes[1];
		n.timestamp=attributes[2];
		n.importance=attributes[3];
		n.length=attributes[4];
		n.text=attributes[5];
		nugget_trec.add(n);
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
	public static void main(String args[])
	{int count=0;
		trecnuggets();
		readtrecmatches();
		for(int i=0;i<nugget_trec.size();i++)
		{int f=0;
			for(int j=0;j<match_trec.size();j++)
			{//System.out.println(nugget_trec.get(i).nid+ " "+match_trec.get(j).nid);
				if(nugget_trec.get(i).nid.equals(match_trec.get(j).nid))
					{//System.out.println(nugget_trec.get(i).nid+ " "+match_trec.get(j).nid);
					f=1;break;}
			}
			
			if(f==0)
				{System.out.println(nugget_trec.get(i).text);count++;}
		}
		
		System.out.println(count+" "+nugget_trec.size());
	}
}