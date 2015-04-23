package org.terrier.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

class sustry
{static String scorepath="/home/bhargava/Documents/2try/summariestvs/";
static String initsummary="";
static ArrayList<String> updatesummary=new ArrayList<String>();
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
	public static void main(String args[])
	{
		
		File scores=new File(scorepath);
		String[] folders=scores.list();
		Collections.sort(Arrays.asList(folders));
		
		for(int i=0;i<folders.length;i++)
		{
			System.out.println(folders[i]);
			if(i==0)
				processinitsummary(folders[i]);
			else
			processsummary(folders[i]);
		}
		
	}

	private static void processinitsummary(String string) {
		// TODO Auto-generated method stub
		File f1=new File(scorepath+string);
		FileReader fr = null;
		try {
			fr = new FileReader(f1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
		String line;
		try {
			while((line=br.readLine())!=null)
			{
				updatesummary.add(line);
				System.out.println(line+ string);
					
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void processsummary(String string) {int flag = 0;
		// TODO Auto-generated method stub
		File f1=new File(scorepath+string);
		FileReader fr = null;
		try {
			fr = new FileReader(f1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(fr);
		String line;
		try {
			while((line=br.readLine())!=null)
			{
				for(int i=0;i<updatesummary.size();i++)
				{
					if(jc_check(updatesummary.get(i), line)>0.6)
					flag=1;
				}
				if(flag!=1)
				{
					updatesummary.add(line);
					System.out.println(line+ string);
				}
					
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}