package Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import DataStructures.AbstractBlock;
import DataStructures.Comparison;
import DataStructures.EntityIndex;



import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Arrays.asList;

public class testeThread_Conta {
    
    static class testeThreads implements Callable<int[]> {
        private  int from;
        private  int to;
    	List<AbstractBlock> blocks;
    	ExecuteBlockComparisons ebc;
    	ArrayList<Comparison>[] listComparison;
    	private final Lock lock = new ReentrantLock();
		private int[] nblocks;
    //	
        
        testeThreads(int from,int to, List<AbstractBlock> blocks, int[] nblocks, ExecuteBlockComparisons ebc,int[] blockSize) {
            this.from = from;
            this.to = to;
           this.blocks=blocks;
           this.ebc=ebc;
           this.nblocks=blockSize;
        }
        
  
        public int[] call()   throws Exception{
        	
//    		EntityIndex entityIndex = new EntityIndex(blocks);
//    		
//    		for (int i = from; i < to; i++) {
//    			AbstractBlock b=blocks.get(i);
//    			if(b!=null){
//    				ComparisonIterator comparisonit = b.getComparisonIterator();
//    				b.getBlockIndex();
//    				while(comparisonit.hasNext()){
//    					DataStructures.Comparison c=comparisonit.next();
//
//    					final List<Integer> commonBlockIndices = entityIndex.getCommonBlockIndices(b.getBlockIndex(), c);
//    					if (commonBlockIndices == null) {
//    						continue;
//    					}
//
//    					Double sim=ebc.getSImilarityAttribute(c.getEntityId1(),c.getEntityId2(),Converter.atributos_value);
//    					c.sim=sim;
//    					if(sim==10)
//    						sim--;
//    					 try {
//    				            lock.lock();
//    				            nblocks[((int)Math.floor(sim*10))]++;
//    				        } finally {
//    				            lock.unlock();
//    				        }
//    					
//    				}
//    			}
//    		}
    	
    		return nblocks;
        }                
    }
    
    public static int teste (List<AbstractBlock> blocks, int[] nblocks, ExecuteBlockComparisons ebc) throws Exception {
    	ExecutorService executor = Executors.newFixedThreadPool(8);
    	long startingTime = System.currentTimeMillis();
    	//Collections.shuffle(blocks);
//    	List<Future<ArrayList<Comparison>[]>> results= executor.invokeAll(asList(
//                new testeThreads(0,blocks.size(),blocks,nblocks,ebc,listComparison)
//        ));

    	List<Future<int[]>> results= executor.invokeAll(asList(
               new testeThreads(0,blocks.size()/4,blocks,nblocks,ebc,nblocks ),
               new testeThreads(blocks.size()/4 ,blocks.size()/2,blocks,nblocks,ebc,nblocks),
               new testeThreads(blocks.size()/2,3*blocks.size()/4, blocks,nblocks,ebc,nblocks)  ,
               new testeThreads(3*blocks.size()/4,blocks.size(), blocks,nblocks,ebc,nblocks)      ));
    	//listComparison =((Future<ArrayList<Comparison>[]>) executor).get();
     //  = executor.invokeAll(asList(
//            new testeThreads(0,blocks.size()/2,blocks,nblocks,ebc,listComparison), new testeThreads(blocks.size()/2 ,blocks.size(),blocks,nblocks,ebc,listComparison)
//        ));
       // executor.shutdown();
       // executor.
    //	 ArrayList<Comparison>[] listComparison1= results.get();
//        for(Future<int[]> result: results){
//        	
//        	int[] array=result.get();
//        	for (int i = 0; i < array.length; i++) {
//        		System.out.println(i + "numero de pares results " +array[i]);
//			}
//        	
//        	
//        }
    	 executor.shutdown();
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
//    }    
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
