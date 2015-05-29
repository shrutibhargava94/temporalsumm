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

class evaluation
{static ArrayList<updateattributes> update_trec;
static ArrayList<matchattributes> match_trec;
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
public static int checkintrecupdate(String sentence)
{int pos=-1;
	for(int i=0;i<update_trec.size();i++)
		if(sentence.equals(update_trec.get(i).uid))
		{
		pos=i;
		}
	return pos;
	
}
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
	public static void main(String args[])
	{   readtrecupdate();int count=0;
	readtrecmatches();
	BufferedWriter excep=null;
	try {
		 excep=new BufferedWriter(new FileWriter(new File("/home/bhargava/Documents/afghanistanfull/excepconfirm")));
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	BufferedWriter run=null;
	try {
		 run=new BufferedWriter(new FileWriter(new File("/home/bhargava/Documents/afghanistanfull/runconfirm.ssv")));
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}BufferedWriter matches=null;
	try {
		matches=new BufferedWriter(new FileWriter(new File("/home/bhargava/Documents/afghanistanfull/matchconfirm.tsv")));
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}BufferedWriter updates=null;
	try {
		 updates=new BufferedWriter(new FileWriter(new File("/home/bhargava/Documents/afghanistanfull/updateconfirm.tsv")));
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		String outfolderpath="/home/bhargava/Documents/afghanistanfull/afghansumm/";
		//read hour files generated by learning score sort multiavg
		File outfolder=new File("/home/bhargava/Documents/afghanistanfull/afghansumm/");
		String[] runfiles=outfolder.list();
		Collections.sort(Arrays.asList(runfiles));
		for(int i=0;i<runfiles.length;i++)
		{
			if(runfiles[i].contains("eval"))
			{String line=null;
				try {
					BufferedReader runmake=new BufferedReader(new FileReader(new File(outfolder+"/"+runfiles[i])));
					try {
						while((line=runmake.readLine())!=null)
						{System.out.println("here1");
							String[] runattributes=line.split("\t");
						System.out.println(runattributes[0]+"\t"+runattributes[1]+"\t"+runattributes[2]+"\t"+runattributes[3]+"\t"+runattributes[4]+"\t"+runattributes[6]+"\t"+runattributes[7]);
						run.write(runattributes[0]+" "+runattributes[1]+" "+runattributes[2]+" "+runattributes[3]+" "+runattributes[4]+" "+runattributes[6]+" "+runattributes[7]);
						run.newLine();
							runattributes r=new runattributes();
							r.qid=runattributes[0];
							r.tid=runattributes[1];
							r.rid=runattributes[2];
							r.did=runattributes[3];
							r.sid=runattributes[4];
							r.timestamp=runattributes[6];
							r.confidence=runattributes[7];
							updateattributes u=new updateattributes();
							u.qid=r.qid;
							u.uid=r.did+"-"+r.sid;
							u.did=r.did;
							u.sid=r.sid;
							u.ulength=Integer.toString(runattributes[5].length());
							u.utext=runattributes[5];
							if(checkintrecupdate(u.uid)!=-1)
							{int updatespos=checkintrecupdate(u.uid);
								System.out.println("found"+ update_trec.get(updatespos).utext+ update_trec.get(updatespos).uid);
							//	System.in.read();
								updates.write(update_trec.get(updatespos).qid+"\t"+update_trec.get(updatespos).uid+"\t"+update_trec.get(updatespos).did+"\t"+update_trec.get(updatespos).sid+"\t"+update_trec.get(updatespos).ulength+"\t"+update_trec.get(updatespos).dupid+"\t"+update_trec.get(updatespos).utext);
								updates.newLine();
								count++;
								int matchpos=findinmatches(u.uid);
								if(matchpos!=-1)
								{System.out.println(match_trec.get(matchpos));
								matches.write(match_trec.get(matchpos).qid+"\t"+match_trec.get(matchpos).uid+"\t"+match_trec.get(matchpos).nid+"\t"+match_trec.get(matchpos).ustart+"\t"+match_trec.get(matchpos).uend+"\t"+match_trec.get(matchpos).autop);
								matches.newLine();}
						
							}
							else
							{
								excep.write(u.utext);
								excep.newLine();
							}
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
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	try {System.out.println(count);
		System.in.read();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}try {
		matches.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		updates.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		run.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		excep.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}}
	public static int findinmatches(String uid) {
		for(int i=0;i<match_trec.size();i++)
		{
			if(match_trec.get(i).uid.equals(uid))
			{
				return i;
			}
		}
		return -1;
		// TODO Auto-generated method stub
		
	}
}