package org.terrier.applications;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

class ducpreprocess
{public static void main(String args[])
	{
	
	
	Path dir = Paths.get("/home/bhargava/Downloads/DUC2007_Summarization_Documents/duc2007_testdocs/main/");
	try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
	    for (Path folder: stream) {Path dirfolder = Paths.get("/home/bhargava/Documents/firstsetuptry/duccontent21april/"+folder.getFileName().toString());
		try {
			Files.createDirectory(dirfolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to create directory");
			e.printStackTrace();
		}
	    	try (DirectoryStream<Path> stream1 = Files.newDirectoryStream(folder)) {
	    	    for (Path file: stream1) {
	    	    	File f=new File(dirfolder.toString()+"/"+file.getFileName().toString());
	    	    	FileWriter fw=null;
	    	    	try {
	    	    		 fw=new FileWriter(f);
	    	    	} catch (IOException e1) {
	    	    		// TODO Auto-generated catch block
	    	    		e1.printStackTrace();
	    	    	}
	    	    	
	    	    	BufferedWriter bw=new BufferedWriter(fw);
	    	    	//File article=new File(file.toString());
	    	    	System.out.println(file.toString());
	    	    	String text = null;
					try {
						text = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	    	Document doc = Jsoup.parse(text, "", Parser.xmlParser());
	    	    	for (Element e : doc.select("Headline")) {
	    	            System.out.println(e.text());
	    	            bw.write("<title>"+e.text()+"</title>");
	    	            bw.newLine();
	    	        }
	    	    	for (Element e : doc.select("text")) {
	    	            System.out.println(e.text());
	    	            bw.write("<body>"+e.text()+"</body>");
	    	            bw.newLine();
	    	        }
	    	    	bw.close();
	    	    }
	
	    }
	}
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
}
}