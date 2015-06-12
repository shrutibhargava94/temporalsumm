package org.terrier.applications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class CopyOfmatchnuggettestcolumbia
{private static ArrayList<matchattributes> match_trec;
private static ArrayList<nuggetattributes> nugget_trec;
private static ArrayList<updateattributes> update_trec;
private static ArrayList<String> uidlist=new ArrayList<String>();
private static ArrayList<String> ridlist=new ArrayList<String>();
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
public static void readrunfile()
{
	BufferedReader runmake = null;
	try {
		runmake = new BufferedReader(new FileReader(new File("/home/bhargava/Downloads/cunlp-updates.ssv")));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String line="";
		try {
			while((line=runmake.readLine())!=null)
			{System.out.println("here1");
				String[] runattributes=line.split(" ");
			System.out.println(runattributes[0]+"\t"+runattributes[1]+"\t"+runattributes[2]+"\t"+runattributes[3]+"\t"+runattributes[4]+"\t"+runattributes[5]+"\t"+runattributes[6]);

				runattributes r=new runattributes();
				r.qid=runattributes[0];
				r.tid=runattributes[1];
				r.rid=runattributes[2];
				r.did=runattributes[3];
				r.sid=runattributes[4];
				r.timestamp=runattributes[5];
				r.confidence=runattributes[6];
				ridlist.add(r.rid);
				uidlist.add(r.did+"-"+r.sid);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
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
		readtrecupdate();
		readrunfile();
		BufferedWriter bw=null;
		try {
			bw=new  BufferedWriter(new FileWriter(new File("/home/bhargava/Documents/trecevaluationanalysiscu2apsal.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*for(int i=0;i<match_trec.size();i++)
		{	if((match_trec.get(i).qid.equals("TS14.14"))||(match_trec.get(i).qid.equals("TS14.16"))||(match_trec.get(i).qid.equals("TS14.17"))||(match_trec.get(i).qid.equals("TS14.22")))
			{for(int j=0;j<nugget_trec.size();j++)
			if(match_trec.get(i).nid.equals(nugget_trec.get(j).nid))
			{	System.out.print(nugget_trec.get(j).text+"§");break;}
		for(int j=0;j<update_trec.size();j++)
			if(match_trec.get(i).uid.equals(update_trec.get(j).uid))
			{	System.out.print(update_trec.get(j).utext);break;}
		System.out.println();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}}*/
		for(int j=0;j<update_trec.size();j++)
		{
			if((update_trec.get(j).qid.equals("TS14.14"))||(update_trec.get(j).qid.equals("TS14.16"))||(update_trec.get(j).qid.equals("TS14.17"))||(update_trec.get(j).qid.equals("TS14.22")))
		{if(uidlist.contains(update_trec.get(j).uid)&&(ridlist.get(uidlist.indexOf(update_trec.get(j).uid)).equals("2APSal")))
		{System.out.println("hereactual");	for(int i=0;i<match_trec.size();i++)
		{	if(match_trec.get(i).uid.equals(update_trec.get(j).uid))
		{	System.out.println("update "+update_trec.get(j).utext);
		try {
			bw.write("update "+update_trec.get(j).utext+" "+ridlist.get(uidlist.indexOf(update_trec.get(j).uid)));
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int k=0;k<nugget_trec.size();k++)
		{
			if(nugget_trec.get(k).nid.equals(match_trec.get(i).nid))
			{
				
		System.out.println(nugget_trec.get(k).text);
		try {
			bw.write(nugget_trec.get(k).text);
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
		}System.out.println();
		try {
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		}
		}
		}}
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
