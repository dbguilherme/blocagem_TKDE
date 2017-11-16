/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    Copyright (C) 2015 George Antony Papadakis (gpapadis@yahoo.gr)
 */

package Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import DataStructures.AbstractBlock;
import DataStructures.Attribute;
import DataStructures.Comparison;
import DataStructures.EntityProfile;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;
import uk.ac.shef.wit.simmetrics.tokenisers.TokeniserQGram3;


/**
 *
 * @author gap2
 */
public class ExecuteBlockComparisons {
 
    private  final EntityProfile[] dataset1;
    private final EntityProfile[] dataset2;
    Map<String,Integer> index = new HashMap<String,Integer>(); 
    public double temp_limiar=0.00;

//    public void exportdataset(){
//    	dataset1[0].getAttributes()
//    }
    public ExecuteBlockComparisons(String[] profilesPath) {
        dataset1 = loadProfiles(profilesPath[0]);
        System.out.println("Entities 1\t:\t" + dataset1.length);
        if (profilesPath.length == 2) {
            dataset2 = loadProfiles(profilesPath[1]);
            System.out.println("Entities 2\t:\t" + dataset2.length);
        } else {
            dataset2 = null;
        }
    }
    
    
    
    public ExecuteBlockComparisons(List<EntityProfile>[] profiles) {
        dataset1 = profiles[0].toArray(new EntityProfile[profiles[0].size()]);
        System.out.println("Entities 1\t:\t" + dataset1.length);
        if (profiles.length == 2) {
            dataset2 = profiles[1].toArray(new EntityProfile[profiles[1].size()]);
            System.out.println("Entities 2\t:\t" + dataset2.length);
        } else {
            dataset2 = null;
        }
    }
    
    public long comparisonExecution(List<AbstractBlock> blocks) {
        long startingTime = System.currentTimeMillis();
        for (AbstractBlock block : blocks) {
            ComparisonIterator iterator = block.getComparisonIterator();
            while (iterator.hasNext()) {
                Comparison comparison = iterator.next();
                if (dataset2 != null) {
                    ProfileComparison.getJaccardSimilarity(dataset1[comparison.getEntityId1()].getAttributes(), 
                                                           dataset2[comparison.getEntityId2()].getAttributes());
                } else {
                    ProfileComparison.getJaccardSimilarity(dataset1[comparison.getEntityId1()].getAttributes(), 
                                                           dataset1[comparison.getEntityId2()].getAttributes());
                }
            }
        }
        long endingTime = System.currentTimeMillis();
        return endingTime-startingTime;
    }
    
    private EntityProfile[] loadProfiles(String profilesPath) {
        List<EntityProfile> entityProfiles = (ArrayList<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPath);
        EntityProfile[] e =entityProfiles.toArray(new EntityProfile[entityProfiles.size()]);
       // Map<string, integer> mapaNomes = new HashMap<string, integer>();
        
        int contador=1;
        for (EntityProfile entityProfile : entityProfiles) {
        	//entityProfile.getatt(entityProfile);
        	//entityProfile.x="";
        	entityProfile.len= 0;
        	for ( Attribute att : entityProfile.getAttributes() ) {
        		//entityProfile.x=entityProfile.x.concat(att.getValue().toLowerCase().trim().replaceAll("[\\W]|_", " ")+ " --- ");
        		String splitted[]=att.getValue().toLowerCase().split(" ");
        		entityProfile.len+=splitted.length;
        		
//        			for (int i = 0; i < splitted.length; i++) {
//						
//					
//        		
//	        			if((splitted[i].length())==0)
//	        				continue;
//						if(index.get(splitted[i])!=null) {
//							entityProfile.set.add(index.get(splitted[i]));					
//						}
//						else {
//							index.put(splitted[i], contador);
//							entityProfile.set.add(contador++);
//						}
//        			}	
				
        		//if(index.get(key))
    		}
        	//System.out.println("  " + entityProfile.set.size());
        	
		}
        return e;
    }

	public Set<Attribute> exportEntityA(int entityIds1) {
		return dataset1[entityIds1].getAttributes();
		
	}
	public Set<Attribute> exportEntityB(int entityIds1) {
		if(dataset2!=null)
			return dataset2[entityIds1].getAttributes();
		return dataset1[entityIds1].getAttributes();
		
	}
	public double jaccardSimilarity_l(int entityIds1 , int entityIds2, int inter ) {
		
//			Vector<Integer> a=dataset1[entityIds1].set;
//			Vector<Integer> b;
//			if(dataset2!=null)
//				b=dataset2[entityIds2].set;
//			else
//				b=dataset1[entityIds2].set;
//			
//			Set<Integer> s1 = new LinkedHashSet<Integer>();
//	        for(int i =0; i< a.size(); i++){
//	            s1.add(a.get(i));
//	        }
//	        Set<Integer> s2 = new LinkedHashSet<Integer>();
//	        for(int i =0; i< b.size(); i++){
//	            s2.add(b.get(i));
//	        }
//	        Set<Integer> intersection = new LinkedHashSet<>(s1);
//	        intersection.retainAll(s2);
//	        Set<Integer> union = new LinkedHashSet<Integer>(s1); 
//	        union.addAll(s2); 
//	        double ant=(double)intersection.size()/ (double)union.size();
	        
//	        double dep=((double)inter)/(a.size()+b.size()-inter);
//	        if(Math.abs(ant-dep)>0.2)
//	        	System.out.println("sim " + ant +" "+ dep );
        return ((double)inter)/(dataset1[entityIds1].len+dataset1[entityIds2].len-inter);
    }
	
	public double  getSImilarity (int entityIds1, int entityIds2){
//		for(Attribute att:dataset1[entityIds1].getAttributes()){
//			//if(att.getName().value(""))
//    			//profile1.add(att);
//			System.out.print(att.getValue()+ " :");
//		}
//		System.out.print( " ");
//		for(Attribute att:dataset2[entityIds2].getAttributes()){
//			//if(att.getName().value(""))
//    			//profile1.add(att);
//			System.out.print(att.getValue()+ " :");
//		}
//		System.out.println();
		if(dataset2==null){
			return ProfileComparison.getJaccardSimilarity(dataset1[entityIds1].getAttributes(), 
	                 dataset1[entityIds2].getAttributes());
		}else
			return ProfileComparison.getJaccardSimilarity(dataset1[entityIds1].getAttributes(), 
	                 dataset2[entityIds2].getAttributes());
		
		 
	}

	public double  getSimilarityAttribute (int entityIds1, int entityIds2){

		TokeniserQGram3 tok =new TokeniserQGram3();
		JaccardSimilarity jc =new JaccardSimilarity();
		//JaroWinkler jw = new JaroWinkler();
		//Levenshtein le= new Levenshtein();
		//System.out.println(dataset1[entityIds1].x + " --------" + dataset1[entityIds2].x);
		if(dataset2!=null)
			return jc.getSimilarity(dataset1[entityIds1].x,dataset2[entityIds2].x );
		else
			return jc.getSimilarity(dataset1[entityIds1].x,dataset1[entityIds2].x);
		
	}



	public void print(int entityId1, int entityId2) {
		//System.out.println();
		System.out.println(dataset1[entityId1].x);
		System.out.println(dataset1[entityId2].x);
		Vector<Integer> a=dataset1[entityId1].set;
		Vector<Integer> b=dataset1[entityId2].set;
//		for (int i = 0; i < a.size() && i< b.size(); i++) {
//			System.out.println("print a "+ a.get(i)+"  "+ b.get(i));
//		}
		Set<Integer> s1 = new LinkedHashSet<Integer>();
        for(int i =0; i< a.size(); i++){
            s1.add(a.get(i));
        }
        Set<Integer> s2 = new LinkedHashSet<Integer>();
        for(int i =0; i< b.size(); i++){
            s2.add(b.get(i));
        }
        Set<Integer> intersection = new LinkedHashSet<>(s1);
        intersection.retainAll(s2);
		System.out.print("xxx " +intersection.size());
	}
}