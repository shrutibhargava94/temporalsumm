package org.terrier.applications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class coulumbiarunupdate
{
	private static ArrayList<updateattributes> update_trec;
	private static ArrayList<runattributes> columbia;

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
	public static void readcolumbia()
	{BufferedReader updatetrecread=null;
	String line;
	//System.out.println("here");
	columbia=new ArrayList<runattributes>();
		try {
			 updatetrecread = new BufferedReader(new FileReader(new File("/home/bhargava/Downloads/cunlp-updates.ssv")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			try {
				while((line=updatetrecread.readLine())!=null)
				{System.out.println("here");
				String[] attributes=line.split(" ");
				runattributes r=new runattributes();
				r.qid=attributes[0];
				r.tid=attributes[1];
				r.rid=attributes[2];
				r.did=attributes[3];
				r.sid=attributes[4];
				r.timestamp=attributes[5];
				r.confidence=attributes[6];
				columbia.add(r);
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
	{readcolumbia();
	readtrecupdate();
	BufferedWriter bw=null;
	try {
		bw=new BufferedWriter(new FileWriter(new File("/home/bhargava/Documents/columbiaupdatestext.txt")));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		for(int i=0;i<columbia.size();i++)
		{String coluid=columbia.get(i).did+"-"+columbia.get(i).rid;
		System.out.println(columbia.get(i).sid);
			for(int j=0;j<update_trec.size();j++)
			{System.out.println("update");
				if(update_trec.get(j).qid.equals("TS14."+columbia.get(i).qid))
			{
				if(coluid.equals(update_trec.get(j).uid))
					{System.out.println(update_trec.get(j).utext);try {
						bw.write(update_trec.get(j).utext);
						bw.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}break;}
			}
			}
		}
	}
}