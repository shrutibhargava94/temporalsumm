package org.terrier.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;



class timestamp
{public static void main(String args[])
	{/*File f1=new File("/home/bhargava/Downloads/TS13-TrainingData/training-topics-nuggets.tsv");
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
		br.readLine();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	try {
		while((line=br.readLine())!=null)
		{
			String[] fields=line.split("\t");	
			System.out.println(Long.parseLong(fields[2]));
			Timestamp ts=new Timestamp(Long.parseLong(fields[2]));
			Date d=new Date(Long.parseLong(fields[2]));
			//System.out.println(d);
			long milliseconds = ts.getTime() + (ts.getNanos() / 1000000);
			Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
	        cal.setTime(d);
	       /*System.out.println(cal.get(Calendar.YEAR)
	                + "/" + cal.get(Calendar.MONTH)
	                + "/" + cal.get(Calendar.DATE)
	                + " " + cal.get(Calendar.HOUR)
	                + ":" + cal.get(Calendar.MINUTE)
	                + ":" + cal.get(Calendar.SECOND)
	                + (cal.get(Calendar.AM_PM)==0?"AM":"PM")
	                );
	      //  System.out.println(milliseconds);
	        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        
	       
	         isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	        String date=isoFormat.format(new java.util.Date (Long.parseLong(fields[2])*1000));
	        System.out.println(date+" "+fields[5]);
	        
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
	}*/
	//Date date=new Date("2012-08-11-13");
	//String s="2012-08-11 13:00:00";
	//System.out.println(Timestamp.valueOf(s).getTime());
			//Calendar.set(Calendar.DAY_OF_MONTH,11);
	//Timestamp ts=new Timestamp("2012-08-11 13:00:00");
	//System.out.println(ts.getTime());
	
	
	String timehour="2012-02-24-00";
	
	DateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH");
	
	format.setTimeZone(TimeZone.getTimeZone("UTC"));
	Date date = null;
	try {
		date = format.parse(timehour);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println(date.getTime()/1000);
	
	}
	
}