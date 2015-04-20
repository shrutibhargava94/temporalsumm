package org.terrier.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import org.terrier.indexing.tokenisation.EnglishTokeniser;
import org.terrier.indexing.tokenisation.Tokeniser;
import org.terrier.terms.BaseTermPipelineAccessor;
import org.terrier.terms.PorterStemmer;
import org.terrier.terms.Stopwords;
import org.terrier.terms.TermPipeline;



class stemmingandstopwordremovaltry
{
public static ArrayList<String> content(String text) throws IOException
  {System.setProperty("terrier.home","C:\\Users\\bhargava\\Downloads\\terrier-4.0\\terrier-4.0-win");
  TermPipeline _next = new BaseTermPipelineAccessor();
  Stopwords stop=new Stopwords(_next);
  
   
  PorterStemmer ps=new PorterStemmer();


EnglishTokeniser et=new EnglishTokeniser();

Reader reader=new StringReader(text);
String[] tokens=et.getTokens(reader);
ArrayList<String> newtoken=new ArrayList<String>();
int k=0;
for(int i=0;i<tokens.length;i++)
	{//System.out.println(tokens[i]);
	if(stop.isStopword(tokens[i]))
		continue;
	else
		{newtoken.add(ps.stem(tokens[i]));
		k++;
		}
	
	
	}
return newtoken;

  }
}