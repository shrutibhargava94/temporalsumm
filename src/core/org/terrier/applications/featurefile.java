package org.terrier.applications;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

class featurefile
{int sentencepos;
	public void computefeaturesforfile(String string, String files, ArrayList<String> querycontent, HashMap<String, Double> hourfreq, HashMap<String, Double> topicweight, HashMap<String, Double> freq, BufferedWriter bw, int topic, HashMap<String, sentencerank> rankmap) {
		// TODO Auto-generated method stub
		File article=new File(string+"/"+files);
		try {
			Document doc = Jsoup.parse(article, "UTF-8");
			String text = doc.body().text();
			Properties props = new Properties();
		    props.setProperty("annotators", "tokenize, ssplit");
		    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		    
		    // read some text in the text variable
		    // Add your text here!
		    
		    // create an empty Annotation just with the given text
		    Annotation document = new Annotation(text);
		    
		    // run all Annotators on this text
		    pipeline.annotate(document);
		    
		    // these are all the sentences in this document
		    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		  sentencepos=0;
		    for(CoreMap sentence: sentences) {
		    	featuresentence sent=new featuresentence(sentencepos,sentence.toString(),querycontent,hourfreq,topicweight,freq,bw,topic,rankmap);
		    	sent.computesentencefeatures();
		    	//sent.computesentencefeaturestraining();
		    	sentencepos++;
		    }
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}