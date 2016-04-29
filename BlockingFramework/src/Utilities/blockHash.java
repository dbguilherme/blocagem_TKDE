package Utilities;

import java.util.List;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Comparison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import DataStructures.AbstractBlock;
import DataStructures.Uri;

public class blockHash {
	
	public static HashMap<Integer,  ArrayList<DataStructures.Comparison>> deep = new HashMap<Integer, ArrayList<DataStructures.Comparison>>();
	protected final static String names[]=(new Converter()).atributos_value;
	public static int blockSize[] = new int[10];
	public static int blockNumber[] = new int[10];
	
	
	public static void produceHash(List<AbstractBlock> blocks, ExecuteBlockComparisons ebc){
		
		
		//for (int i = 0; i < 10; i++) {
		//  ArrayList<DataStructures.Comparison> array= new ArrayList<DataStructures.Comparison>();
			
			//HashMap<Integer, ArrayList<DataStructures.Comparison>> hashA = new HashMap<Integer, ArrayList<DataStructures.Comparison>>();
			//hashA.put(0, null);
	//		deep.put(i,array);
	//	}
		//
		//int i=0;
		for ( AbstractBlock b:blocks) {
			
			if(b!=null){
				ComparisonIterator comparisonit = b.getComparisonIterator();
				b.getBlockIndex();
				while(comparisonit.hasNext()){
					DataStructures.Comparison c=comparisonit.next();
					
					Double sim=ebc.getSImilarityAttribute(c.getEntityId1(),c.getEntityId2(),names);
					c.sim=sim;
					//int array;
					c.teste=b.getBlockIndex();
					//System.out.println("b.getBlockIndex();  " + b.getBlockIndex());
//					ArrayList<DataStructures.Comparison> array;
//					array=deep.get(((int)Math.floor(sim*10)));
//					if(array!=null){
//						
//						
//						
//							array.add(c);
//							blockNumber[((int)Math.floor(sim*10))]++;
//						//	System.out.println("aqui");
//							deep.put(blockNumber[((int)Math.floor(sim*10))],array);
							blockSize[((int)Math.floor(sim*10))]++;
						
//					}else{
//						ArrayList<DataStructures.Comparison> arrayN= new ArrayList<DataStructures.Comparison>();
//						arrayN.add(c);
//						//hashA = new HashMap<Integer, ArrayList<DataStructures.Comparison>>();
//						deep.put(blockNumber[((int)Math.floor(sim*10))]++, array);
//						blockSize[((int)Math.floor(sim*10))]++;
//					}
					
					//deep.put(((int)Math.floor(sim*10)), hashA);
					//if((deep.get(((int)Math.ceil(sim*10))))==null){
						
					//}
						
					
					//put(, arg1)
					
				}
				
			}
			
		}
//		int count=0;
//		for(Integer e: deep.keySet()){
//			for(Integer f:deep.get(e).keySet()){
//				if(deep.get(e).get(f)!=null){
//					Iterator<DataStructures.Comparison> it = deep.get(e).get(f).iterator();
//					while(it.hasNext()){
//						DataStructures.Comparison c= it.next();
//						System.out.println("*****************" +c.sim + " " + c.getEntityId1() + " " +e + " "+ f);
//						count++;
//					}
//				}
//			}
//		}
		
		ArrayList<DataStructures.Comparison> x = deep.get(0);
		
		System.out.println("*****************" +x.get(x.size()-1).teste);
		//blocks.get(0);
		
	}

}
