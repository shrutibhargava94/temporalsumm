/*
 * Terrier - Terabyte Retriever 
 * Webpage: http://terrier.org 
 * Contact: terrier{a.}dcs.gla.ac.uk
 * University of Glasgow - School of Computing Science
 * http://www.ac.gla.uk
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is TRECIndexing.java.
 *
 * The Original Code is Copyright (C) 2004-2014 the University of Glasgow.
 * All Rights Reserved.
 *
 * Contributor(s):
 *   Gianni Amati <gba{a.}fub.it> (original author)
 *   Vassilis Plachouras <vassilis{a.}dcs.gla.ac.uk>
 *   Ben He <ben{a.}dcs.gla.ac.uk>
 *   Craig Macdonald <craigm{a.}dcs.gla.ac.uk> 
 */
package org.terrier.applications;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.terrier.indexing.Collection;
import org.terrier.indexing.CollectionFactory;
import org.terrier.matching.ResultSet;
import org.terrier.structures.BitIndexPointer;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.indexing.Indexer;
import org.terrier.structures.indexing.singlepass.BasicSinglePassIndexer;
import org.terrier.structures.indexing.singlepass.BlockSinglePassIndexer;
import org.terrier.structures.postings.IterablePosting;
import org.terrier.utility.ApplicationSetup;
/**
 * This class creates the indices for a test collection.
 * <p>
 * <b>Properties:</b>
 * <ul>
 * <li><tt>trec.indexer.class</tt> - name of the class to use as the indexer. This only applies to the Index method.</li>
 * <li><tt>trec.collection.class</tt> - name of the class to use as the Collection.</li>
 * </ul>
 * @author Gianni Amati, Vassilis Plachouras, Ben He, Craig Macdonald
 */
public class CopyOfTRECIndexingboilerplate {
	/** The logger used */
	private static Logger logger = Logger.getLogger(CopyOfTRECIndexingboilerplate.class);
	/** The collection to index. */
	Collection collectionTREC;
	
	String path; String prefix;
	
	/** The indexer object.*/
	Indexer indexer;
	/**
	 * A constructor that initialised the data structures
	 * to use for indexing. 
	 * @param _path Absolute path to where the index should be created
	 * @param _prefix Prefix of the index files, usually "data"
	 */
	public CopyOfTRECIndexingboilerplate(String _path, String _prefix)
	{
		path = _path; prefix = _prefix;
		//load the appropriate collection
		final String collectionName = ApplicationSetup.getProperty("trec.collection.class", "TRECCollection");
		collectionTREC = CollectionFactory.loadCollection(collectionName);
		if (collectionTREC == null)
		{
			logger.fatal("Collection class named "+ collectionName + " not found, aborting");
		}

		//load the appropriate indexer
		String indexerName = ApplicationSetup.getProperty(
			"trec.indexer.class",
			ApplicationSetup.BLOCK_INDEXING
				? "BlockIndexer"
				: "BasicIndexer");
		if (indexerName.indexOf('.') == -1)
			indexerName = "org.terrier.structures.indexing.classical."+indexerName;
		else if (indexerName.startsWith("uk.ac.gla.terrier"))
			indexerName = indexerName.replaceAll("uk.ac.gla.terrier", "org.terrier");
		try{
			indexer = (Indexer) Class.forName(indexerName)
				.getConstructor(String.class, String.class)
				.newInstance(path, prefix);
		} catch (ClassNotFoundException e) {
			logger.fatal("Indexer class named "+ indexerName + " not found", e);
		} catch (InstantiationException ie) {
			logger.fatal("Error while instantiating Indexer class named "+ indexerName + " : " + ie.getCause(), ie);
		} catch (Exception e){
			logger.fatal("Indexer class named "+ indexerName + "problem", e);
		}
	}

	/**
	 * A default constructor that initialised the data structures
	 * to use for indexing.
	 */
	public CopyOfTRECIndexingboilerplate() {
		this(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX);
		System.out.println("here in trecindexing cnstructor");
	}
	
	/**
	 * Calls the method index(Collection[]) of the
	 * class Indexer in order to build the data
	 * structures for a set of collections. This 
	 * particular method of the Indexer uses a 
	 * set of builders for a subset of the collection
	 * and builds separate data structures, which are 
	 * later merged.
	 */
	public void index() {
		if (Index.existsIndex(path, prefix))
		{
			logger.fatal("Cannot index while an index exists at "+path + ","+ prefix);
			return;
		}
		indexer.index(new Collection[] {collectionTREC});
		try{
			collectionTREC.close();
		} catch (Exception e) {
			logger.warn("problem closing collection", e);
		}
		System.out.println("done indexing");
	}
	
	/**
	 * Building the inverted file.
	 */
	public void createInvertedFile() {
		if (Index.existsIndex(path, prefix))
		{
			Index i = Index.createIndex();
			if (i == null) {}
			else if (i.hasIndexStructure("inverted"))
			{
				logger.fatal("Cannot create an inverted structure while an index with a inverted structure exists at "+path + ","+ prefix);
				return;
			}
			else if (! i.hasIndexStructure("direct"))
			{
				logger.fatal("Cannot create an inverted structure without a direct structure in the index at "+path + ","+ prefix);
				return;
			}
		}
		else
		{
			logger.fatal("Cannot create an inverted structure without an index at "+path + ","+ prefix);
			return;
		}
		if(logger.isInfoEnabled())
			logger.info("Started building the inverted index...");
		long beginTimestamp = System.currentTimeMillis();
		indexer.createInvertedIndex();
		long endTimestamp = System.currentTimeMillis();
		if(logger.isInfoEnabled()){
			logger.info("Finished building the inverted index...");
			double seconds = (endTimestamp - beginTimestamp) / 1000.0d;
			logger.info("Time elapsed for inverted file: " + seconds);
		}
	}
	
	/**
	 * Builds the direct file and lexicon. This method goes through the 
	 * input files specified in the <tt>collections.spec</tt> file 
	 * and processes them in groups of n documents, where n is specified 
	 * by the property <tt>bundle.size</tt>. Then, it merges the 
	 * temporary lexicon files. If it necessary, it calls for the 
	 * optimisation of the identifiers assigned to terms.
	 */
	public void createDirectFile() {
		if (Index.existsIndex(path, prefix))
		{
			Index i = Index.createIndex();
			if (i == null) {}
			else if (i.hasIndexStructure("direct"))
			{	
				logger.fatal("Cannot create a direct structure while an index with a direct structure exists at "+path + ","+ prefix);
				return;
			}
		}	
		long startTime = System.currentTimeMillis();
		indexer.createDirectIndex(new Collection[] {collectionTREC});
		long endTime = System.currentTimeMillis();
		if(logger.isInfoEnabled())	
			logger.info("Direct index built in "+((endTime-startTime)/1000.0D) 
						 + " seconds.");
		try{
			collectionTREC.close();
		} catch (Exception e) {
			logger.warn("problem closing collection", e);
		}
	}
	
	/**
	 * Builds the inverted file from scratch, single pass method
	 */
	public void createSinglePass(){
		if (Index.existsIndex(path, prefix))
		{
			Index i = Index.createIndex(path, prefix);
			if (i.hasIndexStructure("inverted"))
			{
				logger.fatal("Cannot create an inverted structure while an index with a inverted structure exists at "+path + ","+ prefix);
				return;
			}
		}
		System.err.println("Starting building the inverted file "
			+ (ApplicationSetup.BLOCK_INDEXING ? "(with blocks)" : "") 
			+ "...");
		final long beginTimestamp = System.currentTimeMillis();
		BasicSinglePassIndexer _indexer;
		if (ApplicationSetup.BLOCK_INDEXING)
			_indexer = new BlockSinglePassIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX);
		else
			_indexer = new BasicSinglePassIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX);
		_indexer.index(new Collection[] {collectionTREC});
		long endTimestamp = System.currentTimeMillis();
		System.err.println("Finished building the inverted index...");
		double seconds = (endTimestamp - beginTimestamp) / 1000.0d;
		System.err.println("Time elapsed for inverted file: " + seconds);
		try{
			collectionTREC.close();
		} catch (Exception e) {
			logger.warn("problem closing collection", e);
		}
	}
	
	/** 
	 * Used for testing purposes.
	 * @param args the command line arguments.
	 */
	public static void main(String[] args)
	{Scanner input=new Scanner(System.in);
		System.out.println("Folders for saving the frequency statistics and the index both need to be created before starting with this");
	System.out.println("Enter the path where the documents are saved Example /home/bhargava/Documents/nilamsentence");
    String sentencepath=input.nextLine();
    System.out.println("Enter the path where the list for the documents are saved Example /home/bhargava/Documents/nilamlistforterrier/");
	String listpath=input.nextLine();
	System.out.println("Enter the path where indexes are to be saved Example /home/bhargava/Documents/nilamindex/");
	String indexpath=input.nextLine();
	System.out.println("Enter the path where the frequency statistics to be used in feature computation are to be saved Example /home/bhargava/Documents/nilamfreqstat/");
	String freqstatpath=input.nextLine();
	System.out.println("Enter the query");
	String query=input.nextLine();
	System.out.println("Enter the path for terrier home Example /home/bhargava/Documents/terrier-4.0/");
	String terrierhomepath=input.nextLine();
	System.out.println("Enter a path for the retrieval from interactive query");
	String interactivequerypath=input.nextLine();
	String terrierquery="+"+query.replaceAll(" "," +");
	System.out.println(terrierquery);
	
	System.setProperty("terrier.home",terrierhomepath);
    //reading from each hour folder in the sentencefolder to be indexed
	Path dir = Paths.get(sentencepath);
	try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
		for (Path file: stream) {Path dirfolder = Paths.get(indexpath+file.getFileName().toString());//path for storing index
		if(file.getFileName().toString().contains(".directory"))
			continue;
		Files.createDirectory(dirfolder);
		System.out.println("created index folder"+dirfolder);
		ApplicationSetup.setProperty("terrier.index.path", dirfolder.toString());
		ApplicationSetup.setProperty("collection.spec", listpath+file.getFileName().toString());//path for list for terrier indexing
		ApplicationSetup.loadCommonProperties();
		long startTime = System.currentTimeMillis();
		CopyOfTRECIndexingboilerplate t = new CopyOfTRECIndexingboilerplate();
		System.out.println("initialized trec indexing");
		t.index();
		System.out.println("done indexing");
		InteractiveQuerying iq=new InteractiveQuerying(interactivequerypath);
		iq.processQuery("q1",terrierquery, 1.0);//change query according to the event
		//result file name and docid in this file 
		Scanner in=new Scanner(new File(interactivequerypath));//path for taking things from interactive query
		// BufferedWriter bw=new BufferedWriter(new FileWriter(new File("home/bhargava/Documents/firstsetuptry/frequency)))
		Index index = Index.createIndex();
		PostingIndex<?> di = index.getDirectIndex();
		DocumentIndex doi = index.getDocumentIndex();
		Lexicon<String> lex = index.getLexicon();
		Path dirfolderfreq = Paths.get(freqstatpath+file.getFileName().toString());//path for saving statistics to
		try {
			Files.createDirectory(dirfolderfreq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to create directory");
			e.printStackTrace();
		}
		//saving file wise frequency statistics using a map object stored in a file
		while(in.hasNext())
		{String filename=in.next();
		System.out.println(filename);
        
		String[] ar=filename.split("/");
		int docid = in.nextInt(); //docids are 0-based
		HashMap<String,Double> freq=new HashMap<String,Double>();
		IterablePosting postings = di.getPostings((BitIndexPointer)doi.getDocumentEntry(docid));
		while (postings.next() != IterablePosting.EOL) {
			Map.Entry<String,LexiconEntry> lee = lex.getLexiconEntry(postings.getId());
			//System.out.println(lee.getKey() + " " + postings.getFrequency());
			freq.put(lee.getKey(),(double) postings.getFrequency());
		}
		//writng hashmap
		FileOutputStream fout = new FileOutputStream(dirfolderfreq.toString()+"/"+ar[ar.length-1]);
		ObjectOutputStream oos = new ObjectOutputStream(fout);   
		oos.writeObject(freq);
		oos.close();

		}
		long endTime = System.currentTimeMillis();
		if(logger.isInfoEnabled())
			logger.info("Elapsed time="+((endTime-startTime)/1000.0D));
		}
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} 
	}


}