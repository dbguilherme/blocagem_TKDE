package Utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;

import javax.management.InstanceAlreadyExistsException;

import sun.security.jca.GetInstance.Instance;
import weka.clusterers.SimpleKMeans;

import  java.io.*;
import  java.util.*;
import  weka.core.*;
import  weka.filters.Filter;
import  weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.experiment.Stats;
import weka.classifiers.rules.DecisionTable;
 
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
		HashMap<Integer,LinkedList<String>> newhashMap= new HashMap<Integer,LinkedList<String>>();
		//important parameter to set: preserver order, number of cluster.
		kmeans.setPreserveInstancesOrder(true);
		kmeans.setNumClusters(tamanho);
 
		BufferedReader datafile = readDataFile(file); 
		Instances data = new Instances(datafile);
		DistanceFunction m_DistanceFunction = new EuclideanDistance();
		m_DistanceFunction.setInstances(data);
		
		//kmeans.
		kmeans.buildClusterer(data);
 
		// This array returns the cluster number (starting with 0) for each instance
		// The array has as many elements as the number of instances
		int[] assignments = kmeans.getAssignments();
		Instances centroids = kmeans.getClusterCentroids();		
		
		int i=0;
		double vector[]=new double[tamanho*3];
		String vInstance[]=new String[tamanho*3];
		int position[]=new int[tamanho*3];
		for (int j = 0; j < tamanho*3; j++) {
			vector[j]=10000000;
		}
		int posição=0;
		for(int clusterNum : assignments) {
			//example.put(clusterNum,data.get(i).toString());
			////put(clusterNum,data.get(i));
			//System.out.println(data.get(i) + "          "+  centroids.instance(clusterNum));
			if(m_DistanceFunction.distance(centroids.instance(clusterNum), data.get(i),100)<vector[clusterNum]){
				vector[clusterNum]=m_DistanceFunction.distance(centroids.instance(clusterNum), data.get(i),100);
				vInstance[clusterNum]=data.get(i).toString();
				position[clusterNum]=i;
			}			
		    i++;
		}		
		
		posição=i;
		i=0;
		for(int clusterNum : assignments) {
			//example.put(clusterNum,data.get(i).toString());
			////put(clusterNum,data.get(i));
		//	System.out.println(clusterNum + " ------  " + data.get(i) + "          ");
			if(m_DistanceFunction.distance(centroids.instance(clusterNum), data.get(i),100)<vector[clusterNum+tamanho]){
				if(position[clusterNum]!=i && data.get(i).toString().split(",")[5]=="1"){
					vector[tamanho+clusterNum]=m_DistanceFunction.distance(centroids.instance(clusterNum), data.get(i),100);
					vInstance[tamanho+clusterNum]=data.get(i).toString();
					position[tamanho+clusterNum]=i;
				}
			}			
		    i++;
		}
			
		
//		for (int j = 0; j < vInstance.length; j++) {
//			System.out.println(j + "   "+ vector[j] +"   "+ vInstance[j] + " ---------------------  "+ position[j]);
//		}
		
		for (int j = 0; j < tamanho*2; j++) {
			if(example.get(position[j])!=null){
		    	LinkedList<String> l = example.get(position[j]);
		    	//System.err.println(data.get(clusterNum));
		    	l.add(position[j]+ " "+vInstance[j]);
		    	//l.add(i+ " ");
		    	example.put(position[j], l);
		    }else{
		    	LinkedList<String> l = new LinkedList<String>();
		    	l.add(position[j]+ " "+vInstance[j]);
		    	example.put(position[j], l);
		    	//i++;
		    } 
		   // i++;

		}
		//if(m_DistanceFunction.distance(centroids.instance(clusterNum), data.get(i),100))
	    		
//		for (i = 0; i < centroids.numInstances(); i++) {
//			  //System.out.println( "Centroid "+(i + 1)+": " + centroids.instance(i));
//			  String cent[]=centroids.instance(i).toString().split(",");
//			  LinkedList<String> x = example.get(i);
//			  
//			  double[] diff=new double[cent.length];
//			  double[] vfinal=new double[x.size()];
//			  String linha[]=new String[x.size()];
// 			  for (int j = 0; j < x.size(); j++) {
//				try{
//				//  System.out.println(j+ "  xxxx   " + x.get(j));
//				  linha[j]=x.get(j).split(" ")[0];
//				  String local[]=x.get(j).split(" ")[1].split(",");
//				  for (int k = 1; k < local.length-1; k++) {
//					  diff[k]=Math.abs(Double.parseDouble(cent[k])- Double.parseDouble(local[k])); 
//					  vfinal[j]+=diff[k];
//				  }	
//				  Instance n = new Ins
//				 
//				//  System.out.println("valor---"+ vfinal[j]);
//				}catch(Exception e ){					
//					System.out.println("LEMEENTO ---"+e.getMessage());
//					e.printStackTrace();
//				}
//			  }
// 			  double menor=vfinal[0];
// 			  int n = 0;
//			  for (int j = 0; j < vfinal.length; j++) {
//				  if(menor>vfinal[j]){
//					  menor=vfinal[j];
//					  n=j;
//					  
//				  }
//			 }
//			  LinkedList<String> l = new LinkedList<String>();
//			  l.add(x.get(n));
//			  newhashMap.put(Integer.parseInt(linha[n]), l);
//			
//		}		
		
//		System.out.println("xxxxxxxxxxxx" + example.size());
//		for (int j = 0; j < example.size(); j++) {
//			LinkedList<String> l = example.get(j);
//			if(l==null)
//				continue;
//			for (int k = 0; k < l.size(); k++) {
//				System.out.print(l.get(k) + "  ---------");
//			}
//			System.out.println();						
//		}
		//System.out.println("size--" + newhashMap.size());
		return example;
	}
}