package Experiments;

import DataStructures.AbstractBlock;
import DataStructures.EntityIndex;
import BlockProcessing.AbstractEfficiencyMethod;
import BlockProcessing.BlockRefinement.BlockPruning;
import BlockProcessing.BlockRefinement.BlockScheduling;
import BlockProcessing.BlockRefinement.ComparisonsBasedBlockPurging;
import BlockProcessing.ComparisonRefinement.AbstractDuplicatePropagation;
import BlockProcessing.ComparisonRefinement.BilateralDuplicatePropagation;
import BlockProcessing.ComparisonRefinement.ComparisonPropagation;
import BlockProcessing.ComparisonRefinement.ComparisonPruning;
import BlockProcessing.ComparisonRefinement.ComparisonScheduling;
import Utilities.BlockStatistics;
import Utilities.ExportBlocks;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gap2
 */

public class TotalExperiments {
    public static void main (String[] args) throws IOException {
//        String mainDirectory = "/home/guilherme/Downloads/BlockingFramework/";
//        String[] indexDirs = { mainDirectory+"data/",
//            mainDirectory+"/data/" };
//        String duplicatesPath = mainDirectory + "data/groundtruth";
    	String mainDirectory = System.getProperty("user.home")+"/Dropbox/blocagem/bases/movies/";
		String[] indexDirs = { 
				mainDirectory+"/dataset1_imdb",
				mainDirectory+"/dataset2_dbpedia"};

		String duplicatesPath = mainDirectory+ "/groundtruth";  
    	
    	
        ExportBlocks exportBlocks = new ExportBlocks(indexDirs);
        List<AbstractBlock> blocks = exportBlocks.getBlocks();
        System.out.println("Blocks\t:\t" + blocks.size());
 
        AbstractDuplicatePropagation adp = new BilateralDuplicatePropagation(duplicatesPath);
        long startingTime = System.currentTimeMillis();
        BlockStatistics blockStats = new BlockStatistics(blocks, adp);
        blockStats.applyProcessing();
        long elapsedTime = System.currentTimeMillis()-startingTime;
        System.out.println("Time (in miliseconds)\t:\t" + elapsedTime);
            
        EntityIndex entityIndex = new EntityIndex(blocks);
        List<AbstractEfficiencyMethod> workflow = new ArrayList<AbstractEfficiencyMethod>();
        workflow.add(new ComparisonsBasedBlockPurging());
        workflow.add(new BlockScheduling());
        workflow.add(new BlockPruning());
        workflow.add(new ComparisonPropagation(entityIndex));
        workflow.add(new ComparisonPruning(entityIndex));
        workflow.add(new ComparisonScheduling(false, entityIndex));
        workflow.add(new ComparisonScheduling(true, entityIndex));
        
        for (AbstractEfficiencyMethod method : workflow) {
            System.out.println("\n\nCurrent method\t:\t" + method.getName());
            
            try {
                long time1 = System.currentTimeMillis();
                method.applyProcessing(blocks);
                long time2 = System.currentTimeMillis();
                System.out.println("Processing time\t:\t" + (time2-time1));
            } catch (Exception  exp) {
                exp.printStackTrace();
            }
            
            adp = new BilateralDuplicatePropagation(duplicatesPath);
            long time1 = System.currentTimeMillis();
            method.applyProcessing(blocks, adp);
            long time2 = System.currentTimeMillis();
            System.out.println("Deduplication time\t:\t" + (time2-time1));
        }
    }
}