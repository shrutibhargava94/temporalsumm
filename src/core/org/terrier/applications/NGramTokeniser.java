package org.terrier.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.terrier.indexing.tokenisation.TokenStream;
import org.terrier.indexing.tokenisation.Tokeniser;
import org.terrier.utility.ApplicationSetup;
import org.terrier.utility.Files;

public class NGramTokeniser extends Tokeniser {
	/** The maximum number of digits that are allowed in valid terms. */
	protected final static int maxNumOfDigitsPerTerm = 4;
	/**
	 * The maximum number of consecutive same letters or digits that are
	 * allowed in valid terms.
	 */
	protected final static int maxNumOfSameConseqLettersPerTerm = 3;
	/**
	 * Whether tokens longer than MAX_TERM_LENGTH should be dropped.
	 */
	protected final static boolean DROP_LONG_TOKENS = true;
	
	static final boolean LOWERCASE = Boolean.parseBoolean(ApplicationSetup.getProperty("lowercase", "true"));
	static final int MAX_TERM_LENGTH = ApplicationSetup.MAX_TERM_LENGTH;
	
	static class NGramTokenStream extends TokenStream
	{
		int ch;
		boolean eos = false;
		int ngramCounter = 0;
		int ngrams = 2;
		Reader br;
		StringBuilder sw = new StringBuilder();

		public NGramTokenStream(Reader _br)
		{
			this.br = _br;
			if (this.br == null)
			{
				this.eos = true;
			}
		}
		
		@Override
		public boolean hasNext() {
			return ! eos;
		}
		
		@Override
		public String next() 
		{
			try{
				ch = this.br.read();
				while(ch != -1)
				{	
                    /*If on entering you encounter full stop character empty the sw buffer*/ 
                    if(ch != -1 && (ch == '.' || ch == '\n'))
                    {
                        sw.setLength(0);
                        sw.trimToSize();
                        ngramCounter = 0;
                    }
                    else if(ch == 32 && ngramCounter == 1)
                    {   
                    	if(sw.length() > 0)
						{
							if(sw.charAt(sw.length()-1) != ' ')
								sw.append((char) ch);
						} 
                    }
		
					/* skip non-alphanumeric characters and skip space character only if string buffer is empty*/
					while (ch != -1 && !(Character.isLetter((char)ch) 
							|| Character.getType((char)ch) == Character.NON_SPACING_MARK) && (ch != ' ' || ngramCounter == 0) 
					) 
					{
						ch = br.read();
					}
					
				
					if(ch == ' ' && ngramCounter == ngrams - 1)
					{	if(sw.length() > 0)
						{
							if(sw.charAt(sw.length()-1) != ' ')
								sw.append((char) ch);
						}
						ch = br.read();
					}
					//now accept all alphanumeric characters and few punctuation characters like ' ' , ',' , ':' , ';' and tall punctuation characters like different kinds of brackets etc. 
					while (ch != -1 && ch != '\n' && (ch == ' ' || ch == ',' || ch == ':' || ch == ';' || ch == '\'' || ch == '"' || ch == '*' || ch == '/' || ch == '?'
							|| Character.getType((char)ch) == Character.DASH_PUNCTUATION || Character.getType((char)ch) == Character.CURRENCY_SYMBOL
							|| Character.getType((char)ch) == Character.START_PUNCTUATION || Character.getType((char)ch) == Character.END_PUNCTUATION/*ch == '[' || ch == ']' || ch == '(' || ch == ')' || ch == '{' || ch == '}'*/
							|| Character.isLetterOrDigit((char)ch) || Character.getType((char)ch) == Character.NON_SPACING_MARK))
					{
						if(ch == ' ' && sw.length() > 0)
						{	if(sw.charAt(sw.length()-1) != ' ')
							{
								if(++ngramCounter == ngrams)
								{	sw.append((char)ch);
									break;
								}
							}
							else
							{	
								ch = br.read();
								continue;
							}
						}
						
						/* add character to word so far except punctuation characters */
						if(ch != ',' && ch != ':' && ch != ';' && ch != '"' && ch != '*' && ch != '\''
							&& Character.getType((char)ch) != Character.START_PUNCTUATION && Character.getType((char)ch) != Character.END_PUNCTUATION 
							&& Character.getType((char)ch) != Character.CURRENCY_SYMBOL && !Character.isDigit((char)ch)) /*ch != '[' && ch != ']' && ch != '(' && ch != ')' && ch != '{' && ch != '}'*/
							sw.append((char)ch);
						
						ch = br.read();
						
					}
					
					String s = sw.toString().trim();
					checkDigits(s);
					if(ngramCounter == 0 || ch == '.' || ch == '\n')
					{
						sw.setLength(0);
						sw.trimToSize();
						ngramCounter = 0;
					}
					else
					{	sw.delete(0, sw.indexOf(" ") + 1);
						sw.trimToSize();
						ngramCounter = ngrams - 1;
					}
					if (s.length() > 0)
						return s.toLowerCase();
				}
				eos = true;
				return null;
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		}
		
	}
	
	@Override
	public TokenStream tokenise(final Reader reader) {
		return new NGramTokenStream(reader);
	}

	/**
	 * Checks whether a term is shorter than the maximum allowed length,
	 * and whether a term does not have many numerical digits or many
	 * consecutive same digits or letters.
	 * @param s String the term to check if it is valid.
	 * @return String the term if it is valid, otherwise it returns null.
	 */
	static String check(String s) {
		//if the s is null
		//or if it is longer than a specified length
		s = s.trim();
		final int length = s.length();
		int counter = 0;
		int ch = -1;
		int chNew = -1;
		for(int i=0;i<length;i++)
		{
			chNew = s.charAt(i);
			if (ch == chNew)
				counter++;
			else
				counter = 1;
			ch = chNew;
			/* if it contains more than 3 consequtive same letters,
			   or more than 4 digits, then discard the term. */
			if (counter > maxNumOfSameConseqLettersPerTerm)
				return "";
		}
		return LOWERCASE ? s.toLowerCase() : s;
	}

	static void checkDigits(String s){
		s=s.trim();
		final int length = s.length();
		int counter = 0;
		int chNew = -1;
		for(int i=0;i<length;i++)
		{
			chNew = s.charAt(i);
			if (Character.isDigit(chNew))
				counter++;
		}
		
		/* if it contains digits in the term then output the term. */
		if (counter > 0)
			System.out.println(s+":"+counter);
	}
		
	public static void main(String[] args) throws IOException{
		NGramTokeniser tokeniser = new NGramTokeniser();
		/*ArrayList<String> generatedFileList = new ArrayList<String>();
		try{
			//opening the address_collection file
			BufferedReader br = Files.openFileReader("C:\\Users\\z.fernando\\Documents\\terrier-4.0\\terrier-4.0-win\\etc\\collection.spec");
			//iterate through each entry of the address_collection file
			String filename = br.readLine();
			while (filename != null) {
				//if the line starts with #, then assume it is 
				//a comment and proceed to the next one
				if (filename.startsWith("#")) {
					filename = br.readLine();
					continue;
				}
				generatedFileList.add(filename);
				filename = br.readLine();
			}
			br.close();
		}catch(IOException ioe) {
			System.err.println("problem opening address list of files in NYTCollection: " +ioe);	
		}
		File file3 = new File("C:\\Users\\z.fernando\\Documents\\terrier-4.0\\discrepancy.txt");
		FileOutputStream fos2 = new FileOutputStream(file3);
		PrintStream p2 = new PrintStream(fos2);
		for(String filename:generatedFileList){
		BufferedReader in = Files.openFileReader(filename);
		if(in.readLine() == null)
			continue;
		
		File file = new File(filename);
		*/
		File file = new File("/home/bhargava/Documents/ngramtrial");
		//NYTCorpusDocumentParser nytdocparser = new NYTCorpusDocumentParser();
		//NYTCorpusDocument nytdoc = nytdocparser.parseNYTCorpusDocumentFromFile(file, false);
		//TokenStream toks = tokeniser.tokenise(new StringReader("My, (name's hola) is [B�r�ck]: Obama. $bada: [di] dum: dum"));
		TokenStream toks = null;
		try {
			toks = tokeniser.tokenise(new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8")));
			while(toks.hasNext())
			{
			System.out.println(toks.next());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		/*System.out.println("Filename:"+filename);
		if(nytdoc != null){

			if(nytdoc.getBody() != null)
				toks = tokeniser.tokenise(new StringReader(nytdoc.getBody()));
			else
				toks = tokeniser.tokenise(new StringReader(""));
			while(toks.hasNext())
			{	
				String token = toks.next();
				
				if(token !=null)
				{	String trimmed = token.trim();
					int words =trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
					if(words > 2)
					{	System.out.println(token + ":" + words);
						//System.out.println(filename + ":" + token + ":" + words);
						//p2.println(filename + ":" + token + ":" + words);
					}//else
						//System.out.println(token + ":" + words);
				}
				//p.println(token);
			}
			//p.println(counter);
			
			//fos.close();
			//p.close();
		}
		}
		fos2.close();
		p2.close();*/
	}
}

