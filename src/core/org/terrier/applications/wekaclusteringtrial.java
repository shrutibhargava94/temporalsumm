package org.terrier.applications;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import weka.attributeSelection.AttributeSelection;
import weka.clusterers.SimpleKMeans;
import weka.clusterers.XMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToNominal;
 
class wekaclusteringtrial
{
 
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
 
	public static void main(String[] args) throws Exception {
		SimpleKMeans kmeans = new SimpleKMeans();
 XMeans xmeans=new XMeans();
 xmeans.setMinNumClusters(2);
 xmeans.setMaxNumClusters(100);
		/*kmeans.setSeed(10);
 
		//important parameter to set: preserver order, number of cluster.
		kmeans.setPreserveInstancesOrder(true);
		kmeans.setNumClusters(5);*/
 
		BufferedReader datafile = readDataFile("//home/bhargava/Documents/trialclustering.arff"); 
		Instances data = new Instances(datafile);
		String[] options = new String[2];
		 options[0] = "-R";                                    // "range"
		 options[1] = "13";
		  StringToNominal filter = new StringToNominal();
		  filter.setAttributeRange("last");
		  filter.setInputFormat(data);
		  Remove remove = new Remove();                         // new instance of filter
		  remove.setOptions(options);                           // set options
		  remove.setInputFormat(data);                          // inform filter about dataset **AFTER** setting options
		  Instances newData = Filter.useFilter(data, remove);
		  Instances filteredInstance = Filter.useFilter(data, filter);

		 
		//kmeans.buildClusterer(filteredInstance);
		  xmeans.buildClusterer(newData);
 
		// This array returns the cluster number (starting with 0) for each instance
		// The array has as many elements as the number of instances
		//int[] assignments = kmeans.getAssignments();
 //kmeans.
		  for(int  index=0;index<newData.numInstances();index++)
		System.out.println(xmeans.clusterInstance(newData.instance(index))+" "+data.instance(index).toString());

		/*ArrayList<clusterpostprocess> clusterresult=new ArrayList<clusterpostprocess>();
		int i=0;
		for(int clusterNum : assignments) {
		   // System.out.printf("Instance %d -> Cluster %d \n", i, clusterNum);
		   // System.out.println(data.instance(i));
		    clusterpostprocess cp=new clusterpostprocess();
		    cp.attributelist=data.instance(i).toString();
		    cp.cluster=clusterNum;
		    cp.inst=i;
		    clusterresult.add(cp);
		    i++;
		}
		
		Collections.sort(clusterresult,new Comparator<clusterpostprocess>(){
	    	public int compare(clusterpostprocess s1,clusterpostprocess s2)
	    	{if(s1.cluster==s2.cluster)
	    		return s2.inst-s1.inst;
	    	else
	    		return s1.cluster-s2.cluster;
	    	}
	    });
		
		for(int i1=0;i1<clusterresult.size();i1++)
			System.out.println(clusterresult.get(i1).cluster+" "+clusterresult.get(i1).inst+" "+clusterresult.get(i1).attributelist);*/
	}
}
	
