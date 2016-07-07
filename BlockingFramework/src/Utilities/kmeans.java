package Utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.NetworkInterface;

import javax.management.InstanceAlreadyExistsException;

import sun.security.jca.GetInstance.Instance;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import  java.io.*;
import  java.util.*;
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
 
	
	
	
	public static Instances  run(String file, int tamanho, Instances trainingInstances, List<Double> sampleMatches, List<Double> sampleNonMatches) throws Exception {
		
		sampleMatches.clear();
		sampleNonMatches.clear();
		SimpleKMeans kmeans = new SimpleKMeans();
		EM em=new EM();
		HashMap<Integer,LinkedList<String>> example = new HashMap<Integer,LinkedList<String>>();
		kmeans.setSeed(10);
		HashMap<Integer,LinkedList<String>> newhashMap= new HashMap<Integer,LinkedList<String>>();
		//important parameter to set: preserver order, number of cluster.
		kmeans.setPreserveInstancesOrder(true);
		kmeans.setNumClusters(tamanho);
 
		//em.setMaxIterations(100);
		//em.setNumClusters(80);
		
		BufferedReader datafile = readDataFile(file); 
		
		Instances data = new Instances(datafile);
		//data.setClassIndex(data.numAttributes()-1);
		DistanceFunction m_DistanceFunction = new EuclideanDistance();
		m_DistanceFunction.setInstances(data);
		
		//kmeans.
		kmeans.buildClusterer(data);
		
		//em
		//DenseInstance insta=new DenseInstance
		em.buildClusterer(data);
		
		
		int cluster[][] = new int[em.numberOfClusters()][2];
		int clusterFinal[]=new int[em.numberOfClusters()];
		int p=0,n=0;
		for (int i = 0; i < data.size(); i++) {
		//	System.out.println(em.clusterInstance(data.get(i)) + "  ----"  + data.get(i).toString().split(",")[5]);
			if(data.get(i).toString().split(",")[5].equals("1"))
				(cluster[em.clusterInstance(data.get(i))][1])++;
			else
				(cluster[em.clusterInstance(data.get(i))][0])++;
		}
		for (int i = 0; i < cluster.length; i++) {
			if(cluster[i][0]>cluster[i][1])
			{
				clusterFinal[i]=0;
				n++;
			}
			else{
				clusterFinal[i]=1;
				p++;
			}
		}
		
		System.out.println("zzzzzzzzzzzz"+ clusterFinal[0] +"  "+clusterFinal[1] +"  "+clusterFinal[2]);
		double[][][] matrix=em.getClusterModelsNumericAtts();
		for (int i = 0; i < matrix.length; i++) {
			double[] instanceValues = new double[6];
			for (int j = 0; j < matrix[i].length; j++) {
				//System.out.println(matrix[i][j][0]);
				instanceValues[j]=matrix[i][j][0];
			}
			instanceValues[5] = clusterFinal[i];
			DenseInstance newInstance = new DenseInstance(1.0, instanceValues);
			//newInstance.setClassValue(clusterFinal[i]);
			//System.out.println(newInstance.value(0));
			//newInstance.setDataset(trainingInstances);
			
			//trainingInstances.add(newInstance); 
			
			
		}
//		for (int k = 0; k < 3; k++) {
//			for (int k2 = 0; k2 < 6; k2++) {
//				System.out.print( trainingInstances.get(k).value(k2) +"  ");
//			}
//			System.out.println();
//		}
//		sampleMatches.add((double) p);
//		sampleNonMatches.add((double) n);
		
		
		//int[] assignments = kmeans.getAssignments();
		Instances centroids = kmeans.getClusterCentroids();		
		
		
		p=n=0;
		for(weka.core.Instance clusterNum : centroids) {
			//example.put(clusterNum,data.get(i).toString());
			////put(clusterNum,data.get(i));
			//System.out.println(data.get(i) + "          "+  centroids.instance(clusterNum));					
		    trainingInstances.add(clusterNum);
		    if(clusterNum.toString().split(",")[5].equals("1"))
		    	p++;
		    else
		    	n++;
		}		
		
		for (int k = 0; k < 3; k++) {
			for (int k2 = 0; k2 < 6; k2++) {
				System.out.print( trainingInstances.get(k).value(k2) +"  ");
			}
		System.out.println();
	}
		sampleMatches.add((double) p);
		sampleNonMatches.add((double) n);
		
		
		
		
		
		
		
		
		
		
		
//		instanceValues[5] = 1;
//		DenseInstance newInstance = new DenseInstance(1.0, instanceValues);
//		newInstance.setDataset(trainingInstances);
		
		//newInstance.setDataset(trainingInstances);
		// This array returns the cluster number (starting with 0) for each instance
		// The array has as many elements as the number of instances
//		int[] assignments = kmeans.getAssignments();
//		Instances centroids = kmeans.getClusterCentroids();		
////		
//		
//		

		
//		int[] assignments = em.get  .getAssignments();
//		Instances centroids = kmeans.getClusterCentroids();
//////////////////////////////////////////		
//		int i=0;
//		double vector[]=new double[tamanho*3];
//		String vInstance[]=new String[tamanho*3];
//		int position[]=new int[tamanho*3];
//		for (int j = 0; j < tamanho*3; j++) {
//			vector[j]=10000000;
//		}
//		int posição=0;
//		for(int clusterNum : assignments) {
//			//example.put(clusterNum,data.get(i).toString());
//			////put(clusterNum,data.get(i));
//			//System.out.println(data.get(i) + "          "+  centroids.instance(clusterNum));
//			if(m_DistanceFunction.distance(centroids.instance(clusterNum), data.get(i),100)<vector[clusterNum]){
//				vector[clusterNum]=m_DistanceFunction.distance(centroids.instance(clusterNum), data.get(i),100);
//				vInstance[clusterNum]=data.get(i).toString();
//				position[clusterNum]=i;
//			}			
//		    i++;
//		}		
		
//		posição=i;
//		i=0;
//		for(int clusterNum : assignments) {
//			//example.put(clusterNum,data.get(i).toString());
//			////put(clusterNum,data.get(i));
//		//	System.out.println(clusterNum + " ------  " + data.get(i) + "          ");
//			if(m_DistanceFunction.distance(centroids.instance(clusterNum), data.get(i),100)<vector[clusterNum+tamanho]){
//				if(position[clusterNum]!=i){
//					vector[tamanho+clusterNum]=m_DistanceFunction.distance(centroids.instance(clusterNum), data.get(i),100);
//					vInstance[tamanho+clusterNum]=data.get(i).toString();
//					position[tamanho+clusterNum]=i;
//				}
//			}			
//		    i++;
//		}
////			
		
//		for (int j = 0; j < vInstance.length; j++) {
//			System.out.println(j + "   "+ vector[j] +"   "+ vInstance[j] + " ---------------------  "+ position[j]);
//		}
		
//		for (int j = 0; j < tamanho; j++) {
//			if(example.get(position[j])!=null){
//		    	LinkedList<String> l = example.get(position[j]);
//		    	//System.err.println(data.get(clusterNum));
//		    	l.add(position[j]+ " "+vInstance[j]);
//		    	//l.add(i+ " ");
//		    	example.put(position[j], l);
//		    }else{
//		    	LinkedList<String> l = new LinkedList<String>();
//		    	l.add(position[j]+ " "+vInstance[j]);
//		    	example.put(position[j], l);
//		    	//i++;
//		    } 
//		   // i++;
//
//		}
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
		System.out.println("trainingInstances 0---" + trainingInstances.size());
		return trainingInstances;
	}
}