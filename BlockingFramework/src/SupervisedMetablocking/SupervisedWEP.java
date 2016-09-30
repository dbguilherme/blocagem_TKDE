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

package SupervisedMetablocking;

import DataStructures.AbstractBlock;
import DataStructures.Attribute;
import DataStructures.BilateralBlock;
import DataStructures.Comparison;
import DataStructures.DecomposedBlock;
import DataStructures.EntityProfile;
import DataStructures.IdDuplicates;
import Utilities.ComparisonIterator;
import Utilities.Converter;
import Utilities.ExecuteBlockComparisons;
import Utilities.FileUtilities;
import Utilities.testeThread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 *
 * @author gap2
 */

public class SupervisedWEP extends AbstractSupervisedMetablocking {
    
    private List<Integer> retainedEntities1;
    private List<Integer> retainedEntities2;
    Statement st;
  //  Connection con;
    
    public int getCount (){
    	
    	return count;
    }
    
    public SupervisedWEP (int noOfClassifiers, List<AbstractBlock> bls, Set<IdDuplicates> duplicatePairs, ExecuteBlockComparisons ebc) {
        super (noOfClassifiers, bls, duplicatePairs,ebc);
    }
    int count=0;
    @Override
    protected void applyClassifier(Classifier classifier) throws Exception {
    	
    	
    //	System.out.println("testando thread " + testeThread.teste(blocks,entityIndex,classifier,null,retainedEntities1,retainedEntities2));
    	
        for (AbstractBlock block : blocks) {
            ComparisonIterator iterator = block.getComparisonIterator();
            while (iterator.hasNext()) {
            	
                Comparison comparison = iterator.next();
                final List<Integer> commonBlockIndices = entityIndex.getCommonBlockIndices(block.getBlockIndex(), comparison);
                if (commonBlockIndices == null) {
                    continue;
                }

                if (trainingSet.contains(comparison)) {
                  //  continue;
                }
                if(count++%1000000==1)
                	System.out.println("processados -->" + count);
                Instance currentInstance = getFeatures(NON_DUPLICATE, commonBlockIndices, comparison,1.0);
                if(currentInstance!=null){
                    int instanceLabel = (int) classifier.classifyInstance(currentInstance);  
	                if (instanceLabel == DUPLICATE) {
	                	
	                    retainedEntities1.add(comparison.getEntityId1());
	                    retainedEntities2.add(comparison.getEntityId2());
	                }
                }
            }
        }
    }

    @Override
    protected List<AbstractBlock> gatherComparisons() {
        int[] entityIds1 = Converter.convertCollectionToArray(retainedEntities1);
        int[] entityIds2 = Converter.convertCollectionToArray(retainedEntities2);
        
        boolean cleanCleanER = blocks.get(0) instanceof BilateralBlock;
        final List<AbstractBlock> newBlocks = new ArrayList<AbstractBlock>();
        newBlocks.add(new DecomposedBlock(cleanCleanER, entityIds1, entityIds2));
        return newBlocks;
    }

    @Override
    protected void initializeDataStructures() {
        detectedDuplicates = new HashSet<IdDuplicates>();
        retainedEntities1 = new ArrayList<Integer>();
        retainedEntities2 = new ArrayList<Integer>();
       // con=  new myql_connection().create_conection("scholar_clear");
		
//		try {
//			st = con.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("the-file-name.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }

    

    int armazena=0;
    protected void processComparisons(int classifierId, int iteration, BufferedWriter writer1, BufferedWriter writer2, BufferedWriter writer3,BufferedWriter writer4, double th) {
        System.out.println("\n\nProcessing comparisons...");
       // create_conection("tese_scholar_clean");
        int[] entityIds1 = Converter.convertCollectionToArray(retainedEntities1);
        int[] entityIds2 = Converter.convertCollectionToArray(retainedEntities2);
        int teste=0;
        for (int i = 0; i < entityIds1.length; i++) {
        	//System.out.println(entityIds1[i] +" ---" + entityIds2[i]);
        	teste++;
            Comparison comparison = new Comparison(dirtyER, entityIds1[i], entityIds2[i],0.0);
            if (areMatching(comparison)) {
                final IdDuplicates matchingPair = new IdDuplicates(entityIds1[i], entityIds2[i]);
                detectedDuplicates.add(matchingPair);  
                //System.out.println("match ->>>>" +entityIds1[i] +" ---" + entityIds2[i]);
            }
        }
               
        System.out.println("Executed comparisons blocking\t:\t" 	);
        System.out.println("Executed comparisons\t:\t" + entityIds1.length);
        System.out.println("Detected duplicates\t:\t" + detectedDuplicates.size());
        sampleComparisons[classifierId].add((double)entityIds1.length);
        sampleDuplicates[classifierId].add((double)detectedDuplicates.size());
        try {
        	if(classifierId==0){
        		Double d =((double)detectedDuplicates.size())/(duplicates.size())*100.0;
        		writer1.write("ExecutedComparisons " + (entityIds1.length) + " DetectedDuplicates " + detectedDuplicates.size() + " PC " + d + " sampleMatches "+ sampleMatches.get(0) +  " "+  sampleNonMatches.get(0) + " " +sampleNonMatchesNotUsed.get(0)+" th " + th +" \n");
        		//armazena++;
        	}else
        	if(classifierId==1){
        		Double d =(sampleDuplicates[classifierId].get(armazena))/(duplicates.size())*100.0;
        		writer2.write("ExecutedComparisons " + (entityIds1.length) + " DetectedDuplicates " + detectedDuplicates.size() + " PC " + d + " sampleMatches "+ sampleMatches.get(0) +  " samplesNMatch "+  sampleNonMatches.get(0) + " time " + overheadTimes[classifierId].get(iteration) +" \n");
        		//armazena++;
        	}else
        	if(classifierId==2){
        		Double d =(sampleDuplicates[classifierId].get(armazena))/(duplicates.size())*100.0;
        		writer3.write("ExecutedComparisons " + (entityIds1.length) + " DetectedDuplicates " + detectedDuplicates.size() + " PC " + d + " sampleMatches "+ sampleMatches.get(0) +  " samplesNMatch "+  sampleNonMatches.get(0) + " time " + overheadTimes[classifierId].get(iteration) +" \n");
        		armazena++;
        	}
//        	else
//            	if(classifierId==3){
//            		Double d =(sampleDuplicates[classifierId].get(armazena))/(duplicates.size())*100.0;
//            		writer4.write("ExecutedComparisons " + (entityIds1.length) + " DetectedDuplicates " + detectedDuplicates.size() + " PC " + d + " sampleMatches "+ sampleMatches.get(iteration) +  " samplesNMatch "+  sampleNonMatches.get(iteration) + " time " + overheadTimes[classifierId].get(iteration) +" \n");
//            		armazena++;
//            	}
        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
  
    
    
    
    @Override
	protected void savePairs(int xxx, ExecuteBlockComparisons ebc) {
    	 System.out.println("\n\nProcessing comparisons...");
    		PrintStream pstxt = null;


    		try {
    			pstxt = new PrintStream(new FileOutputStream(new File("/tmp/out.txt"),false));
    		} catch (FileNotFoundException e1) {
    			e1.printStackTrace();
    		}
          int[] entityIds1 = Converter.convertCollectionToArray(retainedEntities1);
          int[] entityIds2 = Converter.convertCollectionToArray(retainedEntities2);
          String concatStringA;
          String concatStringB;
          
          int teste=0;
          for (int i1 = 0; i1 < entityIds1.length; i1++) {
          	//System.out.println(i1 +"  "+entityIds1[i1] +" ---" + entityIds2[i1]);
	           teste++;
//	           Set<Attribute> setAtributtes = ebc.exportEntityA(entityIds1[i1]);
//	           String sA[]=Converter.createVector(setAtributtes,entityIds1[i1]);
//	           concatStringA=sA[0]+"::";
//	           for (int j = 0; j < sA.length; j++) {
//	        	   concatStringA=concatStringA.concat(sA[j]+":");
//	           }
//	           //System.out.println(concatStringA);
//	           setAtributtes = ebc.exportEntityB(entityIds2[i1]);
//	           String sB[]=Converter.createVector(setAtributtes,entityIds2[i1]);
//	           
//	           concatStringB= sB[0]+"::";
//	           for (int j = 0; j < sB.length; j++) {
//	        	   concatStringB=concatStringB.concat(sB[j]+":");
//	           }
//	           //System.out.println(" 		"+concatStringB);
//	           String label="false";
//	             Comparison comparison = new Comparison(dirtyER, entityIds1[i1], entityIds2[i1],0.0);
//	             if (areMatching(comparison)) {
//	                 label="true";
//	             }
//	            try {
//	            	pstxt.print(concatStringA +": "+ ebc.getSImilarity(entityIds1[i1],entityIds2[i1])+ ": "+ concatStringB +" :"+label);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//          	try {
//  				load_data_db(entityIds1[i1], entityIds2[i1],st);
//  			} catch (SQLException e) {
//  				e.printStackTrace();
//  			}
//              Comparison comparison = new Comparison(dirtyER, entityIds1[i1], entityIds2[i1]);
//              if (areMatching(comparison)) {
//                  final IdDuplicates matchingPair = new IdDuplicates(entityIds1[i1], entityIds2[i1]);
//                  detectedDuplicates.add(matchingPair);  
//                 // System.out.println("match ->>>>" +entityIds1[i] +" ---" + entityIds2[i]);
//              }
//          }
          } 
          System.out.println("Executed comparisons blocking\t:\t" +teste	);
          System.out.println("Executed comparisons\t:\t" + entityIds1.length);
          System.out.println("Detected duplicates\t:\t" + detectedDuplicates.size());
         // sampleComparisons[0].add((double)entityIds1.length);
        //  sampleDuplicates[0].add((double)detectedDuplicates.size());
         
    }
    
	
}