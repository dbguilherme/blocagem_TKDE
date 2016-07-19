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

import DataStructures.AbstractBlock;
import DataStructures.Attribute;
import DataStructures.Comparison;
import DataStructures.EntityProfile;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.tokenisers.InterfaceTokeniser;
import uk.ac.shef.wit.simmetrics.tokenisers.TokeniserQGram2;
import uk.ac.shef.wit.simmetrics.tokenisers.TokeniserQGram3;
import uk.ac.shef.wit.simmetrics.tokenisers.TokeniserWhitespace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * @author gap2
 */
public class ExecuteBlockComparisons {
 
    private final EntityProfile[] dataset1;
    private final EntityProfile[] dataset2;
    
    
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
        return entityProfiles.toArray(new EntityProfile[entityProfiles.size()]);
    }

	public Set<Attribute> exportEntityA(int entityIds1) {
		return dataset1[entityIds1].getAttributes();
		
	}
	public Set<Attribute> exportEntityB(int entityIds1) {
		if(dataset2!=null)
			return dataset2[entityIds1].getAttributes();
		return dataset1[entityIds1].getAttributes();
		
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
		 return ProfileComparison.getJaccardSimilarity(dataset1[entityIds1].getAttributes(), 
                 dataset1[entityIds2].getAttributes());
	}

//	
	int count=0;
	public double  getSImilarityAttribute (int entityIds1, int entityIds2, String[] names){
		double sim=0.0;
		//String vector[]=new String[atributos_value.length+1];
		Set<Attribute> profile1= new HashSet(); 
		Set<Attribute> profile2= new HashSet();
		//((new Converter()).atributos_value[2]);
		TokeniserQGram3 tok =new TokeniserQGram3();
		JaccardSimilarity jc =new JaccardSimilarity(tok);
		JaroWinkler jw = new JaroWinkler();
		Levenshtein le= new Levenshtein();
		//String[] name=((new Converter()).atributos_value);
		profile1= dataset1[entityIds1].getAttributes();
		if(dataset2!=null)		
			profile2=dataset2[entityIds2].getAttributes();
		else
			profile2=dataset1[entityIds2].getAttributes();
		
		String[] vetA =new String[names.length];
		String[] vetB =new String[names.length];
		try{
			for(Attribute att:profile1){
				for (int i = 1; i < vetA.length; i++) {
					if(att.getName().equals(names[i]))
		    			vetA[i]= att.getValue().toLowerCase().trim();
				}
			}
			for(Attribute att:profile2){
				for (int i = 1; i < vetB.length; i++) {
					if(att.getName().equals(names[i]))
		    			vetB[i]= att.getValue().toLowerCase().trim();
				}
			}
	
//			for (int i = 1; i < vetB.length && i < vetA.length; i++) {
//				System.out.println(i +" xxxx -->" + vetA[i] + "  "+ vetB[i]);
//			}
		
			for (int i = 1; i < vetB.length && i < vetA.length; i++) {
				if(!(vetA[i]==null) && !(vetB[i]==null) && !vetA[i].isEmpty() && !vetB[i].isEmpty()  )
				{
					sim+=le.getSimilarity(vetA[i], vetB[i]);
					//if(sim>0.2)
					//System.out.println(vetA[i] + " ---- " +  vetB[i] + "  "+ sim);
				}
					
			}
			
		//System.out.println("sim -> " + ((sim)/vetB.length));
		}catch(Exception e ){
			System.out.println("erro " +e.getMessage());
			e.printStackTrace();
			 return ProfileComparison.getJaccardSimilarity(dataset1[entityIds1].getAttributes(), 
	                 dataset1[entityIds2].getAttributes()); 
		}
		//System.out.println(((sim)/(vetB.length)) + " "+ vetB.length);
		 return ((sim)/(vetB.length-1));
	}
}