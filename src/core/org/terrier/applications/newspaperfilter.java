package org.terrier.applications;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import streamcorpus.StreamItem;

class newspaperfilter
{private static String htmlpath="/home/bhargava/Documents/hostagehtmlfilter";
static int ct=0;
public static ArrayList<StreamItem> readStremItemFromFilehtml(String filepath,String outfile, ArrayList<String> filelist)
    {
	File file = new File(filepath);
	InputStream input = null;
	try {
		input = new FileInputStream(file);
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}    	
	XZCompressorInputStream stream = null;
	try {
		stream = new XZCompressorInputStream(input);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} 	
	TTransport transport;
	transport = new TIOStreamTransport(stream);
	try {
		transport.open();
	} catch (TTransportException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}  	
	TProtocol protocol = new TBinaryProtocol(transport);
	ArrayList<StreamItem> doc_list = new ArrayList<StreamItem>();
	int num_docs = 0;
	String outputfile=htmlpath+"/"+outfile+"/";
	StreamItem doc = new StreamItem();
	while(true)
	{   
		
		
		
        try { 
        	doc.read(protocol);
           
        	// if((doc.getBody().getLanguage().getName().equalsIgnoreCase("english"))&&((doc.getBody().clean_html.contains("azerbaijan")||doc.getBody().clean_html.contains("earthquake")||doc.getBody().clean_html.contains("iran"))))
           if(filelist.contains(doc.getStream_id()+".txt"))
        		   {if((doc.getBody().getLanguage().getName().equalsIgnoreCase("english")))
           {   File fileDir = new File(outputfile+doc.getStream_id()+".html");
           
   		Writer out = new BufferedWriter(new OutputStreamWriter(
   			new FileOutputStream(fileDir), "UTF-8" ));
   		System.out.println(doc.getBody().getEncoding()); 
   		
            out.write(doc.getBody().getClean_html());
				
           
            
          
            System.out.println(outputfile+doc.stream_id);
            out.close();
            
           ct++;
           filelist.remove(doc.getStream_id());
           System.out.println(ct);
       
           }
           else
        	   {System.out.println("here"+doc.getBody().getLanguage().getCode());
}
        		   }  } catch (TTransportException e) { 
            if (((TTransportException) e).getType() == TTransportException.END_OF_FILE) 
            { 
                break; 
            } 
        } catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        //doc_list.add(doc);
        num_docs += 1;
        
	}
	System.out.println(file.getName() + " contains " + num_docs + " documents.");
	transport.close();

	return doc_list;
}
	
private static String freqhashmappath="/home/bhargava/Documents/hostagefreqstat/";
private static String xzpath="/home/bhargava/Documents/hostagexz/";
	public static void main(String args[])
	{Scanner input=new Scanner(System.in);
	System.out.println("The folder where the html files will be saved needs to be created");
	System.out.println("Enter the path for the frequency statistics of the top ranked document Example /home/bhargava/Documents/nilamfreqstat/");
	freqhashmappath=input.nextLine();
	System.out.println("Enter the path for the xz files Example /home/bhargava/Documents/nilamxz/");
	xzpath=input.nextLine();
	System.out.println("Enter the path where the html files will be stored Example /home/bhargava/Documents/nilamhtmlfilter");
	htmlpath=input.nextLine();
	File outfolder=new File(freqhashmappath);
	String[] innerfolders=outfolder.list();
	Collections.sort(Arrays.asList(innerfolders));
	for(int i=0;i<innerfolders.length;i++)
	{if(!innerfolders[i].equals(".directory"))
		{Path dirfolderfreq = Paths.get(htmlpath+"/"+innerfolders[i]);//path for saving statistics to
	try {
		Files.createDirectory(dirfolderfreq);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Unable to create directory");
		e.printStackTrace();
		break;
	}
		System.out.println(innerfolders[i]);
		File innerfolder=new File(freqhashmappath+innerfolders[i]+"/");
		String[] files=innerfolder.list();
		if(files.length!=0)
		{
		Collections.sort(Arrays.asList(files));
		ArrayList<String> filelist=new ArrayList<String>();
		HashSet<String> xzfiles=new HashSet<String>();
		for(int j=0;j<files.length;j++)
		{
		String[] splitfile=files[j].split("@");
		System.out.println(splitfile[0]);
		System.out.println(splitfile[1]);
		xzfiles.add(splitfile[0]);
		filelist.add(splitfile[1]);
		
		}
		
	
	Iterator<String> iter=xzfiles.iterator();
	while(iter.hasNext())
	{readStremItemFromFilehtml(xzpath+innerfolders[i]+"/"+iter.next(),innerfolders[i],filelist);
		
	}
	}}
	}}
}