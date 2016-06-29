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
		HashMap<Integer,LinkedList<String>> newhashMap= new HashMap<Integer,LinkedList<String>>();
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
			  //System.out.println( "Centroid "+(i + 1)+": " + centroids.instance(i));
			  String cent[]=centroids.instance(i).toString().split(",");
			  LinkedList<String> x = example.get(i);
			  
			  double[] diff=new double[cent.length];
			  double[] vfinal=new double[x.size()];
			  String linha[]=new String[x.size()];
 			  for (int j = 0; j < x.size(); j++) {
				try{
				//  System.out.println(j+ "  xxxx   " + x.get(j));
				  linha[j]=x.get(j).split(" ")[0];
				  String local[]=x.get(j).split(" ")[1].split(",");
				  for (int k = 0; k < local.length-1; k++) {
					  diff[k]=Math.abs(Double.parseDouble(cent[k])- Double.parseDouble(local[k])); 
					  vfinal[j]+=diff[k];
				  }		
				//  System.out.println("valor---"+ vfinal[j]);
				}catch(Exception e ){					
					System.out.println("LEMEENTO ---"+e.getMessage());
					e.printStackTrace();
				}
			  }
 			  double menor=vfinal[0];
 			  int n = 0;
			  for (int j = 0; j < vfinal.length; j++) {
				  if(menor>vfinal[j]){
					  menor=vfinal[j];
					  n=j;
					  
				  }
			 }
			  LinkedList<String> l = new LinkedList<String>();
			  l.add(x.get(n));
			  newhashMap.put(Integer.parseInt(linha[n]), l);
			//  System.out.println("escolhido -->  "+ l + "  N "+ n  +"   ");
//			  for (int j = 0; j < diff.length; j++) {
//				
//			} 
		}		
		
	//	System.out.println("xxxxxxxxxxxx" + i);
//		for (int j = 1; j < example.size(); j++) {
//			LinkedList<String> l = example.get(j);
//			if(l==null)
//				continue;
//			for (int k = 0; k < l.size(); k++) {
//				System.out.print(l.get(k) + "  ---------");
//			}
//			System.out.println();
//						
//		}
	//	System.out.println("size--" + newhashMap.size());
		return newhashMap;
	}
}