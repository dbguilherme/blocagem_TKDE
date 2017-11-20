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

package DataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gap2
 */

public class EntityIndex implements Serializable {
    
    private static final long serialVersionUID = 13483254243447L;
    
    private int datasetLimit;
    private int noOfEntities;
    private int validEntities1;
    private int validEntities2;
    public int[][] entityBlocks;
   // public ArrayList<Integer>[] records = (ArrayList<Integer>[])new ArrayList[68000];
    public EntityIndex (List<AbstractBlock> blocks) {
        if (blocks.isEmpty()) {
            System.err.println("Entity index received an empty block collection as input!");
            return;
        }
        
        if (blocks.get(0) instanceof DecomposedBlock) {
            System.err.println("The entity index is incompatible with a set of decomposed blocks!");
            System.err.println("Its functionalities can be carried out with same efficiency through a linear search of all comparisons!");
            return;
        }
        
        enumerateBlocks(blocks);
        setNoOfEntities(blocks);
        indexEntities(blocks);
    }
    
    public void enumerateBlocks(List<AbstractBlock> blocks) {
        int blockIndex = 0;
        for (AbstractBlock block : blocks) {
            block.setBlockIndex(blockIndex);
           // System.out.println("enumerate " + blockIndex +" --- "+ ((UnilateralBlock)block).text );
           //if(((BilateralBlock)block).text.equals("DataBlade"))
            {
            	//System.out.println("enumerate " + blockIndex +" --- "+ ((BilateralBlock)block).text + " "+ " " + ((BilateralBlock)block).getIndex2Entities().toString());
//            	for (int i = 0; i < ((UnilateralBlock)block).getEntities().length; i++) {
//            		System.out.print(((UnilateralBlock)block).getEntities()[i]+" ");
//				}
//            	System.out.println();
//            	for (int i = 0; i < ((BilateralBlock)block).getIndex2Entities().length; i++) {
//            		System.out.print(((BilateralBlock)block).getIndex2Entities()[i]+" ");
//				}
//            	System.out.println();
            }
            	
            blockIndex++;
        }
    }
    
    public List<Integer> getCommonBlockIndices(int blockIndex, Comparison comparison) {
        int[] blocks1 = entityBlocks[comparison.getEntityId1()];
        int[] blocks2 = entityBlocks[comparison.getEntityId2()+datasetLimit];

        boolean firstCommonIndex = false;
        int noOfBlocks1 = blocks1.length;
        int noOfBlocks2 = blocks2.length;
        final List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < noOfBlocks1; i++) {
            for (int j = 0; j < noOfBlocks2; j++) {
                if (blocks2[j] < blocks1[i]) {
                    continue;
                }

                if (blocks1[i] < blocks2[j]) {
                    break;
                }

                if (blocks1[i] == blocks2[j]) {
                    if (!firstCommonIndex) 
                    {
                        firstCommonIndex = true;
                        if (blocks1[i] != blockIndex) {
                        	 
                        		
                            return null;
                        }
                    }
                    indices.add(blocks1[i]);
                }
            }
        }
//        if(comparison.sim>=0.0){
//    		for (int j2 = 0; j2 < blocks2.length && j2 < blocks1.length; j2++) {
//    			System.out.println("certo blockIndex "+ blocks1[j2]+ " "+ blocks2[j2] + " " + indices.size());
//			}
//    		System.out.println("*******************");
//        }
        return indices;
    }
    
    public int testBlock(int blockIndex, Comparison comparison) {
        int[] blocks1 = entityBlocks[comparison.getEntityId1()];
        int[] blocks2 = entityBlocks[comparison.getEntityId2()+datasetLimit];

        boolean firstCommonIndex = false;
        int noOfBlocks1 = blocks1.length;
        int noOfBlocks2 = blocks2.length;
        final List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < noOfBlocks1; i++) {
            for (int j = 0; j < noOfBlocks2; j++) {
                if (blocks2[j] < blocks1[i]) {
                    continue;
                }

                if (blocks1[i] < blocks2[j]) {
                    break;
                }

                if (blocks1[i] == blocks2[j]) {
                	 if (blocks1[i] != blockIndex) 
                		 return -1;
                	 else
                		 return 1;
                }
            }
        }
//        if(comparison.sim>=0.0){
//    		for (int j2 = 0; j2 < blocks2.length && j2 < blocks1.length; j2++) {
//    			System.out.println("certo blockIndex "+ blocks1[j2]+ " "+ blocks2[j2] + " " + indices.size());
//			}
//    		System.out.println("*******************");
//        }
        return 1;
    }
    
    public List<Integer> getCommonBlockIndices_cpy(int blockIndex, Comparison comparison) {
        int[] blocks1 = entityBlocks[comparison.getEntityId1()];
        int[] blocks2 = entityBlocks[comparison.getEntityId2()+datasetLimit];

        boolean firstCommonIndex = false;
        int noOfBlocks1 = blocks1.length;
        int noOfBlocks2 = blocks2.length;
        final List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < noOfBlocks1; i++) {
            for (int j = 0; j < noOfBlocks2; j++) {
                if (blocks2[j] < blocks1[i]) {
                    continue;
                }

                if (blocks1[i] < blocks2[j]) {
                    break;
                }

                if (blocks1[i] == blocks2[j]) {
//                    if (!firstCommonIndex) 
//                    {
//                        firstCommonIndex = true;
////                        if (blocks1[i] != blockIndex) {
////                        	 
////                        		
////                            return null;
////                        }
//                    }
                    indices.add(blocks1[i]);
                }
            }
        }
//        if(comparison.sim>=0.0){
//    		for (int j2 = 0; j2 < blocks2.length && j2 < blocks1.length; j2++) {
//    			System.out.println("certo blockIndex "+ blocks1[j2]+ " "+ blocks2[j2] + " " + indices.size());
//			}
//    		System.out.println("*******************");
//        }
        return indices;
    }
    
    public int getDatasetLimit() {
        return datasetLimit;
    }
    
    public int[] getEntityBlocks(int entityId, int useDLimit) {   
        entityId += useDLimit*datasetLimit;
        if (noOfEntities <= entityId) {
            return null;
        }
        return entityBlocks[entityId];
    }
    
    public int getNoOfCommonBlocks(int blockIndex, Comparison comparison) {
        int[] blocks1 = entityBlocks[comparison.getEntityId1()];
        int[] blocks2 = entityBlocks[comparison.getEntityId2()+datasetLimit];
        
        boolean firstCommonIndex = false;
        int commonBlocks = 0;
        int noOfBlocks1 = blocks1.length;
        int noOfBlocks2 = blocks2.length;
        for (int i = 0; i < noOfBlocks1; i++) {
            for (int j = 0; j < noOfBlocks2; j++) {
                if (blocks2[j] < blocks1[i]) {
                    continue;
                }

                if (blocks1[i] < blocks2[j]) {
                    break;
                }

                if (blocks1[i] == blocks2[j]) {
                    commonBlocks++;
                    if (!firstCommonIndex) {
                        firstCommonIndex = true;
                        if (blocks1[i] != blockIndex) {
                            return -1;
                        }
                    }
                }
            }
        }
        
        return commonBlocks;
    }
    
    public int getNoOfEntities() {
        return noOfEntities;
    }
    
    public int getNoOfEntityBlocks(int entityId, int useDLimit) {   
        entityId += useDLimit*datasetLimit;
        if (entityBlocks[entityId] == null) {
            return -1;
        }
        
        return entityBlocks[entityId].length;
    }
    
    public List<Integer> getTotalCommonIndices(Comparison comparison) {
        final List<Integer> indices = new ArrayList<>();
        
        int[] blocks1 = entityBlocks[comparison.getEntityId1()];
        int[] blocks2 = entityBlocks[comparison.getEntityId2()+datasetLimit];
        if (blocks1.length == 0 || blocks2.length == 0) {
            return indices;
        }

        int noOfBlocks1 = blocks1.length;
        int noOfBlocks2 = blocks2.length;
        for (int i = 0; i < noOfBlocks1; i++) {
            for (int j = 0; j < noOfBlocks2; j++) {
                if (blocks2[j] < blocks1[i]) {
                    continue;
                }

                if (blocks1[i] < blocks2[j]) {
                    break;
                }

                if (blocks1[i] == blocks2[j]) {
                    indices.add(blocks1[i]);
                }
            }
        }
        
        return indices;
    }
    
    public int getTotalNoOfCommonBlocks(Comparison comparison) {
        int[] blocks1 = entityBlocks[comparison.getEntityId1()];
        int[] blocks2 = entityBlocks[comparison.getEntityId2()+datasetLimit];
        if (blocks1.length == 0 || blocks2.length == 0) {
            return 0;
        }
        
        int commonBlocks = 0;
        int noOfBlocks1 = blocks1.length;
        int noOfBlocks2 = blocks2.length;
        for (int i = 0; i < noOfBlocks1; i++) {
            for (int j = 0; j < noOfBlocks2; j++) {
                if (blocks2[j] < blocks1[i]) {
                    continue;
                }

                if (blocks1[i] < blocks2[j]) {
                    break;
                }

                if (blocks1[i] == blocks2[j]) {
                    commonBlocks++;
                }
            }
        }
        
        return commonBlocks;
    }
    
    public int getValidEntities1() {
        return validEntities1;
    }
    
    public int getValidEntities2() {
        return validEntities2;
    }
    
    private void indexBilateralEntities(List<AbstractBlock> blocks) {
        //count valid entities & blocks per entity
        validEntities1 = 0;
        validEntities2 = 0;
        int[] counters = new int[noOfEntities];
        for (AbstractBlock block : blocks) {
        	if(((BilateralBlock)block).text.equals("DataBlade"))
            	System.out.println("apagar ");
            BilateralBlock bilBlock = (BilateralBlock) block;
            for (int id1 : bilBlock.getIndex1Entities()) {
                if (counters[id1] == 0) {
                    validEntities1++;
                }
                counters[id1]++;
            }
            
            for (int id2 : bilBlock.getIndex2Entities()) {
                int entityId = datasetLimit+id2;
                if (counters[entityId] == 0) {
                    validEntities2++;
                }
                counters[entityId]++;
            }
            
        }
        
        //initialize inverted index
        entityBlocks = new int[noOfEntities][];
        for (int i = 0; i < noOfEntities; i++) {
            entityBlocks[i] = new int[counters[i]];
            counters[i] = 0;
        }
        
        //build inverted index
        for (AbstractBlock block : blocks) {
        	 if(((BilateralBlock)block).text.equals("DataBlade"))
             	System.out.println("apagar ");
            BilateralBlock bilBlock = (BilateralBlock) block;
            for (int id1 : bilBlock.getIndex1Entities()) {
                entityBlocks[id1][counters[id1]] = block.getBlockIndex();
                counters[id1]++;
//                if(records[id1]==null)
//                	records[id1]=new ArrayList<Integer>();
//                if(!records[id1].contains(block.blockIndex))
//                	records[id1].add(block.blockIndex);
            }
            
            for (int id2 : bilBlock.getIndex2Entities()) {
                int entityId = datasetLimit+id2;
                entityBlocks[entityId][counters[entityId]] = block.getBlockIndex();
                counters[entityId]++;
//                if(records[entityId]==null)
//                	records[entityId]=new ArrayList<Integer>();
//                if(!records[entityId].contains(block.blockIndex))
//                	records[entityId].add(block.blockIndex);
            }
            if(((BilateralBlock)block).text.equals("DataBlade"))
            	System.out.println("xxxxxapagar "+ counters[6101] +" " + counters[650]);
        }
    }
    
    private void indexEntities(List<AbstractBlock> blocks) {
        if (blocks.get(0) instanceof BilateralBlock) {
            indexBilateralEntities(blocks);
        } else{
            indexUnilateralEntities(blocks);
        }
    }
    
    private void indexUnilateralEntities(List<AbstractBlock> blocks) {  
        //count valid entities & blocks per entity
        validEntities1 = 0;
        int[] counters = new int[noOfEntities];
        
        for (AbstractBlock block : blocks) {
            UnilateralBlock uniBlock = (UnilateralBlock) block;
            for (int id : uniBlock.getEntities()) {
                if (counters[id] == 0) {
                    validEntities1++;
                }
                counters[id]++;
            }
        }
        
        //initialize inverted index
        entityBlocks = new int[noOfEntities][];
        for (int i = 0; i < noOfEntities; i++) {
            entityBlocks[i] = new int[counters[i]];
            counters[i] = 0;
        }
        
        //build inverted index
        for (AbstractBlock block : blocks) {
            UnilateralBlock uniBlock = (UnilateralBlock) block;
            
            for (int id : uniBlock.getEntities()) {
                entityBlocks[id][counters[id]] = block.getBlockIndex();
                counters[id]++;
                
                //if(records[]
//                if(records[id]==null)
//                	records[id]=new ArrayList<Integer>();
//                if(!records[id].contains(block.blockIndex))
//                	records[id].add(block.blockIndex);
            }
            
        }
    }
    
    public boolean isRepeated(int blockIndex, Comparison comparison) {  
        int[] blocks1 = entityBlocks[comparison.getEntityId1()];
        int[] blocks2 = entityBlocks[comparison.getEntityId2()+datasetLimit];
        
        int noOfBlocks1 = blocks1.length;
        int noOfBlocks2 = blocks2.length;
        for (int i = 0; i < noOfBlocks1; i++) {
            for (int j = 0; j < noOfBlocks2; j++) {
                if (blocks2[j] < blocks1[i]) {
                    continue;
                }

                if (blocks1[i] < blocks2[j]) {
                    break;
                }

                if (blocks1[i] == blocks2[j]) {
                    return blocks1[i] != blockIndex;
                }
            }
        }
        
        System.err.println("Error!!!!");
        return false;
    }
    
    private void setNoOfEntities(List<AbstractBlock> blocks) {
        if (blocks.get(0) instanceof BilateralBlock) {
            setNoOfBilateralEntities(blocks);
        } else {
            setNoOfUnilateralEntities(blocks);
        }
    }
    
    private void setNoOfBilateralEntities(List<AbstractBlock> blocks) {
        noOfEntities = Integer.MIN_VALUE;
        datasetLimit = Integer.MIN_VALUE;
        for (AbstractBlock block : blocks) {
            BilateralBlock bilBlock = (BilateralBlock) block;
            for (int id1 : bilBlock.getIndex1Entities()) {
                if (noOfEntities < id1+1) {
                    noOfEntities = id1+1;
                }
            }
            
            for (int id2 : bilBlock.getIndex2Entities()) {
                if (datasetLimit < id2+1) {
                    datasetLimit = id2+1;
                }
            }
        }
        
        int temp = noOfEntities;
        noOfEntities += datasetLimit;
        datasetLimit = temp;
    }
    
    private void setNoOfUnilateralEntities(List<AbstractBlock> blocks) {
        noOfEntities = Integer.MIN_VALUE;
        datasetLimit = 0;
        for (AbstractBlock block : blocks) {
            UnilateralBlock bilBlock = (UnilateralBlock) block;
            for (int id : bilBlock.getEntities()) {
                if (noOfEntities < id+1) {
                    noOfEntities = id+1;
                }
            }
        }
    }
}