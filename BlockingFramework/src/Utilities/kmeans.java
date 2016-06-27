package Utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;

import sun.security.jca.GetInstance.Instance;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
 
public class kmeans {
 
	
	
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
 
	public static HashMap<Integer,LinkedList<String>> run(String file, int tamanho) throws Exception {
		SimpleKMeans kmeans = new SimpleKMeans();
		HashMap<Integer,LinkedList<String>> example = new HashMap<Integer,LinkedList<String>>();
		kmeans.setSeed(10);
 
		//important parameter to set: preserver order, number of cluster.
		kmeans.setPreserveInstancesOrder(true);
		kmeans.setNumClusters(tamanho);
 
		BufferedReader datafile = readDataFile(file); 
		Instances data = new Instances(datafile);
 
 
		kmeans.buildClusterer(data);
 
		// This array returns the cluster number (starting with 0) for each instance
		// The array has as many elements as the number of instances
		int[] assignments = kmeans.getAssignments();
 
		int i=0;
		for(int clusterNum : assignments) {
			//example.put(clusterNum,data.get(i).toString());
			//put(clusterNum,data.get(i));
		    if(example.get(clusterNum)!=null){
		    	LinkedList<String> l = example.get(clusterNum);
		    	//System.err.println(data.get(clusterNum));
		    	///++;
		    	l.add(i+ " "+data.get(i).toString());
		    	//l.add(i+ " ");
		    	example.put(clusterNum, l);
		    }else{
		    	LinkedList<String> l = new LinkedList<String>();
		    	l.add(i+ " "+data.get(i).toString());
		    //	l.add(i+ " ");
		    	example.put(clusterNum, l);
		    	//i++;
		    }  	
		    
			
		    i++;
		}
		
		
		
		Instances centroids = kmeans.getClusterCentroids();
		
		
		for (i = 0; i < centroids.numInstances(); i++) {
			  System.out.println( "Centroid "+(i + 1)+": " + centroids.instance(i));
			  
			  for (int j = 0; j < data.numInstances(); j++) {
				  System.out.println( data.instance(i)+" is in cluster "+ (kmeans.clusterInstance(data.instance(i))+1) );
				  if(kmeans.clusterInstance(data.instance(i))==i){
					 centroids.
					  
				  }
			  }
		}
		
		
		//kmeans.
		System.out.println("xxxxxxxxxxxx" + i);
		for (int j = 1; j < example.size(); j++) {
			LinkedList<String> l = example.get(j);
			if(l==null)
				continue;
			for (int k = 0; k < l.size(); k++) {
				System.out.print(l.get(k) + "  ---------");
			}
			System.out.println();
						
		}
		
		return example;
	}
}