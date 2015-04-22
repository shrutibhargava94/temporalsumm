package org.terrier.applications;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

class trial
{
	public static void main(String[] args){
		Path xml=Paths.get("/home/bhargava/Downloads/duc2007_topics.sgml");
		String text = null;
		try {
			text = new String(Files.readAllBytes(xml), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			Document doc = Jsoup.parse(text, "", Parser.xmlParser());
			Elements e=doc.select("num");
			for(Element e1:e)
			{
				System.out.println(e1.parent().select("title"));
			}
	}
}