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
	
	static HashMap<Integer, HashMap<Integer, ArrayList<DataStructures.Comparison>>> deep = new HashMap<Integer, HashMap<Integer, ArrayList<DataStructures.Comparison>>>();
	protected final static String names[]=(new Converter()).atributos_value;

	public static void produceHash(List<AbstractBlock> blocks, ExecuteBlockComparisons ebc){
		
		Collections.sort(blocks, new Comparator<AbstractBlock>() {
			public int compare(AbstractBlock c1, AbstractBlock c2) {
				if (c1.getNoOfComparisons() > c2.getNoOfComparisons()) return -1;
				if (c1.getNoOfComparisons() < c2.getNoOfComparisons()) return 1;
				return 0;
			}});
		
		for (int i = 0; i < 10; i++) {
			ArrayList<DataStructures.Comparison> array= new ArrayList<DataStructures.Comparison>();
			
			HashMap<Integer, ArrayList<DataStructures.Comparison>> hashA = new HashMap<Integer, ArrayList<DataStructures.Comparison>>();
			hashA.put(0, array);
			deep.put(i,hashA);
		}
		HashMap<Integer, ArrayList<DataStructures.Comparison>> hashB;
		HashMap<Integer, ArrayList<DataStructures.Comparison>> hashA;
		int i=0;
		for ( AbstractBlock b:blocks) {
			
			if(b!=null){
				ComparisonIterator comparisonit = b.getComparisonIterator();
				b.getBlockIndex();
				while(comparisonit.hasNext()){
					DataStructures.Comparison c=comparisonit.next();
					
					Double sim=ebc.getSImilarityAttribute(c.getEntityId1(),c.getEntityId2(),names);
					c.sim=sim;
					hashA=deep.get(((int)Math.floor(sim*10)));
					if(hashA!=null){
						ArrayList<DataStructures.Comparison> a= hashA.get(i);
						if(a==null){
							ArrayList<DataStructures.Comparison> array= new ArrayList<DataStructures.Comparison>();
							array.add(c);
							hashA.put(i,array);
						}else{
							a.add(c);
							hashA.put(i,a);
						}
					}else{
						ArrayList<DataStructures.Comparison> array= new ArrayList<DataStructures.Comparison>();
						array.add(c);
						hashA = new HashMap<Integer, ArrayList<DataStructures.Comparison>>();
						hashA.put(i, array);
						
					}
					
					deep.put(((int)Math.floor(sim*10)), hashA);
					//if((deep.get(((int)Math.ceil(sim*10))))==null){
						
					//}
						
					
					//put(, arg1)
					
				}
				
			}
			i++;
		}
		int count=0;
		for(Integer e: deep.keySet()){
			for(Integer f:deep.get(e).keySet()){
				if(deep.get(e).get(f)!=null){
					Iterator<DataStructures.Comparison> it = deep.get(e).get(f).iterator();
					while(it.hasNext()){
						DataStructures.Comparison c= it.next();
						System.out.println("*****************" +c.sim + " " + c.getEntityId1() + " " +e + " "+ f);
						count++;
					}
				}
			}
		}
		
		//ArrayList<DataStructures.Comparison> x = deep.get(1).get(1);
		
		System.out.println("*****************" +count);
		//blocks.get(0);
		
	}

}
