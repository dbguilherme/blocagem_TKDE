package Utilities;

import DataStructures.AbstractBlock;
import DataStructures.Comparison;
import DataStructures.EntityIndex;
import DataStructures.IdDuplicates;
import SupervisedMetablocking.AbstractSupervisedMetablocking;
import SupervisedMetablocking.SupervisedWEP;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Arrays.asList;

import java.io.BufferedWriter;

public class testeThread  {
    
    static class testeThreads implements Callable<ArrayList<Comparison>[]> {
        

		private  int from;
        private  int to;
    	List<AbstractBlock> blocks;
    	EntityIndex entityIndex;
    	ExecuteBlockComparisons ebc;
    	Classifier classifier;
    	ArrayList<Comparison>[] listComparison;
    	private final Lock lock = new ReentrantLock();
    	List<Integer>  retainedEntities1;
    	List<Integer>  retainedEntities2;
    //	
        
       testeThreads(int from,int to, List<AbstractBlock> blocks, EntityIndex entityIndex, Classifier classifier,ArrayList<Comparison>[] listComparison, List<Integer> retainedEntities1, List<Integer> retainedEntities2) {
            this.from = from;
            this.to = to;
           this.blocks=blocks;
           this.entityIndex=entityIndex;
           this.classifier=classifier;
           this.listComparison=listComparison;
           this.retainedEntities1=retainedEntities1;
           this.retainedEntities2=retainedEntities2;
           //super (null, null, null,null);
        }
        
  
        public ArrayList<Comparison>[] call()   throws Exception{
        	
        	
        	 for (AbstractBlock block : blocks) {
                 ComparisonIterator iterator = block.getComparisonIterator();
                 while (iterator.hasNext()) {
                 	
                     Comparison comparison = iterator.next();
                     final List<Integer> commonBlockIndices = entityIndex.getCommonBlockIndices(block.getBlockIndex(), comparison);
                     if (commonBlockIndices == null) {
                         continue;
                     }

                   //  if (trainingSet.contains(comparison)) {
                  //    //  continue;
                  //   }
                   //  if(count++%1000000==1)
                   //  	System.out.println("processados -->" + count);
                     Instance currentInstance = getFeatures(0, commonBlockIndices, comparison,1.0);
                     if(currentInstance!=null){
                         int instanceLabel = (int) classifier.classifyInstance(currentInstance);  
     	                if (instanceLabel == 0) {
     	                	
     	                    retainedEntities1.add(comparison.getEntityId1());
     	                    retainedEntities2.add(comparison.getEntityId2());
     	                }
                     }
                 }
//        	EntityIndex entityIndex = new EntityIndex(blocks);
////			
//			//List<Comparison>[] listComparison= new ArrayList<Comparison>()[10];
//			Random r = new Random();
//			for (int i = from; i < to; i++) {		
//				//blocks_select.add(blocks.get(i));
//				//if(blocks.get(i).getNoOfComparisons()>2)
//				{
//					AbstractBlock b = blocks.get(i);
//					List<Comparison> c = b.getComparisons();
//					
//					for(Comparison com:c){
//						
//						com.teste=blocks.get(i).getBlockIndex();
//						
//						//com.sim=ebc.getSImilarityAttribute(com.getEntityId1(),com.getEntityId2(),Converter.atributos_value);
//								//Double sim=ebc.getSImilarityAttribute(c.getEntityId1(),c.getEntityId2(),Converter.atributos_value);		
//								
//						int level=(int) Math.floor(com.sim*10);
//						
//						final List<Integer> commonBlockIndices =  entityIndex.getCommonBlockIndices(com.teste, com);
//						if (commonBlockIndices == null) {
//							continue;
//						}
//						getFeaturesB(0, commonBlockIndices, com,0.0);
//						
//						double raccb = 0;
//						for (Integer index : commonBlockIndices) {
//							raccb += 1.0 / comparisonsPerBlock[index];
//						}
//						if (raccb < 1.0E-6) {
//							raccb = 1.0E-6;
//						}
//
//						
////						if(commonBlockIndices.size()<1)
////							System.out.println(commonBlockIndices.size());
//						if(com.sim>= ((double)level*0.1) && com.sim<= ((double)(level+1)*0.1)){							
//							int temp=r.nextInt(nblocks[level]);
//							if((temp<500) || (temp<(nblocks[level]*0.1)))
//							{
//								 try {
//		    				            lock.lock();
//		    				            if(com!=null)
//		    				            	listComparison[level].add(com);
//		    				        } finally {
//		    				            lock.unlock();
//		    				        }
//							}
//   							
//						}
//					}
//				}
//			}		
//			for (int j= 0; j < 10; j++) {
//				System.err.println("list size ---"+ listComparison[j].size());			
//			}		
            return listComparison;
        }
			return listComparison;                
    }
    
    public static int teste (List<AbstractBlock> blocks, EntityIndex entityIndex, Classifier classifier, ArrayList<Comparison>[] listComparison, List<Integer> retainedEntities1, List<Integer> retainedEntities2) throws Exception {
    	ExecutorService executor = Executors.newFixedThreadPool(8);
    	long startingTime = System.currentTimeMillis();
    	Collections.shuffle(blocks);
//    	List<Future<ArrayList<Comparison>[]>> results= executor.invokeAll(asList(
//                new testeThreads(0,blocks.size(),blocks,nblocks,ebc,listComparison)
//        ));

    	List<Future<ArrayList<Comparison>[]>> results= executor.invokeAll(asList(
               new testeThreads(0,blocks.size()/4,blocks,entityIndex,classifier,listComparison, retainedEntities1,retainedEntities2),
               new testeThreads(blocks.size()/4 ,blocks.size()/2,blocks,entityIndex,classifier,listComparison, retainedEntities1,retainedEntities2),
               new testeThreads(blocks.size()/2,3*blocks.size()/4, blocks,entityIndex,classifier,listComparison, retainedEntities1,retainedEntities2)  ,
               new testeThreads(3*blocks.size()/4,blocks.size(), blocks,entityIndex,classifier,listComparison, retainedEntities1,retainedEntities2)      ));
    	//listComparison =((Future<ArrayList<Comparison>[]>) executor).get();
     //  = executor.invokeAll(asList(
//            new testeThreads(0,blocks.size()/2,blocks,nblocks,ebc,listComparison), new testeThreads(blocks.size()/2 ,blocks.size(),blocks,nblocks,ebc,listComparison)
//        ));
       // executor.shutdown();
       // executor.
    //	 ArrayList<Comparison>[] listComparison1= results.get();
       
    	 executor.shutdown();
    	for(Future<ArrayList<Comparison>[]> result: results){
        	ArrayList<Comparison>[] array=result.get();
        	for (int i = 0; i < array.length; i++) {
        		System.out.println(i + "results " +array[i].size());
			}        	
        }
        double overheadTime = System.currentTimeMillis()-startingTime;
		System.out.println(" Overhead time\t:\t" + overheadTime);
        return 0;
    }
    
//    public static void main(String[] args) throws Exception {
//        
//        ExecutorService executor = Executors.newFixedThreadPool(4);
//        List <Future<Long>> results = executor.invokeAll(asList(
//            new testeThreads(0, 3), new testeThreads(4, 1_00000000), new testeThreads(10_000000, 1_000_000000)
//        ));
//        executor.shutdown();
//        
//        for (Future<Long> result : results) {
//            System.out.println(result.get());
//        }                
    }

	public Instance getFeatures(int i, List<Integer> commonBlockIndices, Comparison comparison, double d) {
		// TODO Auto-generated method stub
		double[] instanceValues =null;
		instanceValues = new double[6];

		int entityId2 = comparison.getEntityId2() + entityIndex.getDatasetLimit();
		//	System.out.println(noOfBlocks +"   "+ entityIndex.getNoOfEntityBlocks(comparison.getEntityId1(), 0));
		double ibf1 = Math.log(noOfBlocks/entityIndex.getNoOfEntityBlocks(comparison.getEntityId1(), 0));
		double ibf2 = Math.log(noOfBlocks/entityIndex.getNoOfEntityBlocks(comparison.getEntityId2(), 1));
		try{
			instanceValues[0] = commonBlockIndices.size()*ibf1*ibf2;	
		}catch (Exception e ){
			System.out.println(e.getMessage());
		}
//		if(flag==1.0){
//			if(instanceValues[0]<50)
//				return null;
//		}
		//CF 	IBF -RACCB 	Jaccard	Sim	,Node Degree

		double raccb = 0;
		for (Integer index : commonBlockIndices) {
			raccb += 1.0 / comparisonsPerBlock[index];
		}
		if (raccb < 1.0E-6) {
			raccb = 0.0000006;
		}

		instanceValues[1] = raccb;
		//	ProfileComparison.getJaccardSimilarity(profiles1[comparison.getEntityId1()].getAttributes(), profiles2[comparison.getEntityId2()].getAttributes());
		instanceValues[2] = commonBlockIndices.size() / (redundantCPE[comparison.getEntityId1()] + redundantCPE[entityId2] - commonBlockIndices.size());
		instanceValues[3] = nonRedundantCPE[comparison.getEntityId1()];
		instanceValues[4] = nonRedundantCPE[entityId2];;
		//instanceValues[5] = entityIndex.getNoOfEntityBlocks(comparison.getEntityId1(), 0);
		//instanceValues[6] = entityIndex.getNoOfEntityBlocks(comparison.getEntityId2(), 1);
		//		
	
		//instanceValues[5] =ProfileComparison.getJaccardSimilarity(ebcX.exportEntityA(comparison.getEntityId1()), ebcX.exportEntityB(comparison.getEntityId2()));
		//if(instanceValues[0]>200)
		instanceValues[5] =ebcX.getSimilarityAttribute(comparison.getEntityId1(), comparison.getEntityId2());
		//else
		//	instanceValues[5] =0.0;
		instanceValues[6] = match;
		//instanceValues.
		//ebcX.getSimilarityAttribute(comparison.getEntityId1(), comparison.getEntityId2());  //
		//ebcX.getSImilarityAttribute(comparison.getEntityId1(),comparison.getEntityId2(),names);


		//instanceValues[6] = match;

		Instance newInstance = new DenseInstance(1.0, instanceValues);
		newInstance.setDataset(trainingInstances);
		return newInstance;
	}
	
	
	private void getStatistics() {
		noOfBlocks = blocks.size();
		validComparisons = 0;
		int noOfEntities = entityIndex.getNoOfEntities();

		redundantCPE = new double[noOfEntities];
		nonRedundantCPE = new double[noOfEntities];
		comparisonsPerBlock = new double[(int)(blocks.size() + 1)];
		for (AbstractBlock block : blocks) {
			comparisonsPerBlock[block.getBlockIndex()] = block.getNoOfComparisons();

			ComparisonIterator iterator = block.getComparisonIterator();
			while (iterator.hasNext()) {
				Comparison comparison = iterator.next();

				int entityId2 = comparison.getEntityId2()+entityIndex.getDatasetLimit();
				redundantCPE[comparison.getEntityId1()]++;
				redundantCPE[entityId2]++;

				if (!entityIndex.isRepeated(block.getBlockIndex(), comparison)) {
					validComparisons++;
					nonRedundantCPE[comparison.getEntityId1()]++;
					nonRedundantCPE[entityId2]++;
				}
			}
		}
	}


}























//
//
//
//public class testeThread implements Runnable{
//
//	List<AbstractBlock> blocks;
//	int[] nblocks;
//	ExecuteBlockComparisons ebc;
//	
//	public testeThread(List<AbstractBlock> blocks, int[] nblocks, ExecuteBlockComparisons ebc){
//		this.blocks=blocks;
//		this.nblocks=nblocks;
//		this.ebc=ebc;

//	}
//	 public void run (){
//			
//			EntityIndex entityIndex = new EntityIndex(blocks);
//			
//			//List<Comparison>[] listComparison= new ArrayList<Comparison>()[10];
//			Random r = new Random();
//			for (int i = 0; i < blocks.size(); i++) {		
//				//blocks_select.add(blocks.get(i));
//				//if(blocks.get(i).getNoOfComparisons()>2)
//				{
//					AbstractBlock b = blocks.get(i);
//					List<Comparison> c = b.getComparisons();
//					
//					for(Comparison com:c){
//						
//						com.teste=blocks.get(i).getBlockIndex();
//						com.sim=ebc.getSImilarity(com.getEntityId1(),com.getEntityId2());
//						int level=(int) Math.floor(com.sim*10);
//						
//						final List<Integer> commonBlockIndices =  entityIndex.getCommonBlockIndices(com.teste, com);
//						if (commonBlockIndices == null) {
//							continue;
//						}
////						if(commonBlockIndices.size()<1)
////							System.out.println(commonBlockIndices.size());
//						if(com.sim>= ((double)level*0.1) && com.sim<= ((double)(level+1)*0.1)){							
//							int temp=r.nextInt(nblocks[level]);
//							if((nblocks[level]<500) || (temp<(nblocks[level]*0.1))){
//								//continue;
//								listComparison[level].add(com);
//							}
//							
//						}
//					}
//				}
//			}		
//			for (int j= 0; j < 10; j++) {
//				System.err.println("list size ---"+ listComparison[j].size());			
//			}		
//		 
//		// return 0;
//	 } 
//
//	
//}
