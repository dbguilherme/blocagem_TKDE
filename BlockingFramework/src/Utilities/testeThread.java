package Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import DataStructures.AbstractBlock;
import DataStructures.Comparison;
import DataStructures.EntityIndex;



import java.util.*;
import java.util.concurrent.*;
import static java.util.Arrays.asList;

public class testeThread {
    
    static class testeThreads implements Callable<ArrayList<Comparison>[]> {
        private  int from;
        private  int to;
    	List<AbstractBlock> blocks;
    	int[] nblocks;
    	ExecuteBlockComparisons ebc;
    	ArrayList<Comparison>[] listComparison;
    //	
        
        testeThreads(int from,int to, List<AbstractBlock> blocks, int[] nblocks, ExecuteBlockComparisons ebc,ArrayList<Comparison>[] listComparison) {
            this.from = from;
            this.to = to;
           this.blocks=blocks;
           this.nblocks=nblocks;
           this.ebc=ebc;
           this.listComparison=listComparison;
        }
        
  
        public ArrayList<Comparison>[] call()   throws Exception{
        	EntityIndex entityIndex = new EntityIndex(blocks);
//			
			//List<Comparison>[] listComparison= new ArrayList<Comparison>()[10];
			Random r = new Random();
			for (int i = from; i < to; i++) {		
				//blocks_select.add(blocks.get(i));
				//if(blocks.get(i).getNoOfComparisons()>2)
				{
					AbstractBlock b = blocks.get(i);
					List<Comparison> c = b.getComparisons();
					
					for(Comparison com:c){
						
						com.teste=blocks.get(i).getBlockIndex();
						com.sim=ebc.getSImilarityAttribute(com.getEntityId1(),com.getEntityId2(),Converter.atributos_value);
								//Double sim=ebc.getSImilarityAttribute(c.getEntityId1(),c.getEntityId2(),Converter.atributos_value);		
								
						int level=(int) Math.floor(com.sim*10);
						
						final List<Integer> commonBlockIndices =  entityIndex.getCommonBlockIndices(com.teste, com);
						if (commonBlockIndices == null) {
							continue;
						}
//						if(commonBlockIndices.size()<1)
//							System.out.println(commonBlockIndices.size());
						if(com.sim>= ((double)level*0.1) && com.sim<= ((double)(level+1)*0.1)){							
							int temp=r.nextInt(nblocks[level]);
							if((temp<500) || (temp<(nblocks[level]*0.1)))
							{
								//continue;
								listComparison[level].add(com);
							}
							
						}
					}
				}
			}		
//			for (int j= 0; j < 10; j++) {
//				System.err.println("list size ---"+ listComparison[j].size());			
//			}		
            return listComparison;
        }                
    }
    
    public static int teste (List<AbstractBlock> blocks, int[] nblocks, ExecuteBlockComparisons ebc, ArrayList<Comparison>[] listComparison) throws Exception {
    	ExecutorService executor = Executors.newFixedThreadPool(1);
    	long startingTime = System.currentTimeMillis();
    	Collections.shuffle(blocks);
//    	List<Future<ArrayList<Comparison>[]>> results= executor.invokeAll(asList(
//                new testeThreads(0,blocks.size(),blocks,nblocks,ebc,listComparison)
//        ));

    	List<Future<ArrayList<Comparison>[]>> results= executor.invokeAll(asList(
               new testeThreads(0,blocks.size()/4,blocks,nblocks,ebc,listComparison),
               new testeThreads(blocks.size()/4 ,blocks.size()/2,blocks,nblocks,ebc,listComparison),
               new testeThreads(blocks.size()/2,3*blocks.size()/4, blocks,nblocks,ebc,listComparison)  ,
               new testeThreads(3*blocks.size()/4,blocks.size(), blocks,nblocks,ebc,listComparison)      ));
    	//listComparison =((Future<ArrayList<Comparison>[]>) executor).get();
     //  = executor.invokeAll(asList(
//            new testeThreads(0,blocks.size()/2,blocks,nblocks,ebc,listComparison), new testeThreads(blocks.size()/2 ,blocks.size(),blocks,nblocks,ebc,listComparison)
//        ));
       // executor.shutdown();
       // executor.
    //	 ArrayList<Comparison>[] listComparison1= results.get();
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
