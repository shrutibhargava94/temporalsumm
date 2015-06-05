package org.terrier.applications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



class manualevaluation
{static ArrayList<nuggetattributes> nuggets; 
	public static void readnuggets()
	{
	BufferedReader br=null;
	try {
		String path="/home/bhargava/Downloads/nuggets.tsv";
		br=new BufferedReader(new FileReader(new File(path)));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	nuggets=new ArrayList<nuggetattributes>();

	String line="";

		try {
			br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			try {
				while((line=br.readLine())!=null)
				{String[] nuggetsplit=line.split("\t");
				nuggetattributes n=new nuggetattributes();
				n.qid=nuggetsplit[0];
				n.nid=nuggetsplit[1];
				n.timestamp=nuggetsplit[2];
				n.importance=nuggetsplit[3];
				n.length=nuggetsplit[4];
				n.text=nuggetsplit[5];
				nuggets.add(n);
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
			}
			}
	
	
	public static void main(String args[])
	{readnuggets();
	File f=new File("/home/bhargava/Documents/afghanistanfull/june4/matchesappendafghan");
	FileWriter fw=null;
	try {
		 fw=new FileWriter(f,true);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	BufferedWriter bw=new BufferedWriter(fw);
	
		Scanner sc=new Scanner(System.in);
		String path = "/home/bhargava/Documents/afghanistanfull/june4/excepafghan";
		BufferedReader br=null;
		try {
			br=new BufferedReader(new FileReader(new File(path)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String line="";
	String choice="y";
			try {
				while((line=br.readLine())!=null)
				{String[] updateattri=line.split("\t");
				updateattributes u=new updateattributes();
				u.qid=updateattri[0];
				u.uid=updateattri[1];
				u.did=updateattri[2];
				u.sid=updateattri[3];
				u.ulength=updateattri[4];
				u.dupid=updateattri[5];
				u.utext=updateattri[6];
				for(int i=0;i<u.utext.length();i++)
				{if(u.utext.charAt(i)==' ')
				System.out.print(" "+i+" ");
				else
				System.out.print(u.utext.charAt(i));
				}
				System.out.println();
				
				//for(int i=0;i<u.utext.length();i++)
					//System.out.print(i+" ");
				//System.out.println();
				System.out.println("matches any nugget?");
				choice=sc.next();
				while(choice.equals("y"))
				{
			System.out.println("nuggetline number");
			Integer nuggetnum=sc.nextInt();
			System.out.println(nuggets.get(nuggetnum-2).text);
			System.out.println("start");
			int nuggetstart = sc.nextInt();
			System.out.println("end");
			int nuggetend = sc.nextInt();
			System.out.println(nuggets.get(nuggetnum-2).qid+"\t"+u.uid+"\t"+nuggets.get(nuggetnum-2).nid+"\t"+nuggetstart+"\t"+nuggetend+"\t"+"0");
			bw.write(nuggets.get(nuggetnum-2).qid+"\t"+u.uid+"\t"+nuggets.get(nuggetnum-2).nid+"\t"+nuggetstart+"\t"+nuggetend+"\t"+"0");
			bw.flush();
			bw.newLine();
			bw.flush();
			System.out.println("matches any  other nugget?(y)");
			choice=sc.next();
				}
				
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
		}	
	}
}