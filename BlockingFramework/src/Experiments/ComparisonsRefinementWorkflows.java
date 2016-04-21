package Experiments;

import BlockProcessing.ComparisonRefinement.ComparisonScheduling;
import BlockProcessing.ComparisonRefinement.BilateralDuplicatePropagation;
import BlockProcessing.ComparisonRefinement.AbstractDuplicatePropagation;
import BlockProcessing.ComparisonRefinement.ComparisonPruning;
import BlockProcessing.ComparisonRefinement.ComparisonPropagation;
import DataStructures.AbstractBlock;
import DataStructures.EntityIndex;
import BlockProcessing.AbstractEfficiencyMethod;
import BlockProcessing.BlockRefinement.BlockScheduling;
import BlockProcessing.BlockRefinement.ComparisonsBasedBlockPurging;
import Utilities.ExportBlocks;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gap2
 */
public class ComparisonsRefinementWorkflows {

    public static void main(String[] args) throws IOException {
        String mainDirectory = "/opt/data/frameworkData/";
        String[] indexDirs = {mainDirectory + "indices/tokenBlockingDBP",
            mainDirectory + "indices/tokenBlockingIMDB"};
        String duplicatesPath = mainDirectory + "erData/moviesIdGroundTruth";
        
        ExportBlocks exportBlocks = new ExportBlocks(indexDirs);
        List<AbstractBlock> blocks = exportBlocks.getBlocks();
        System.out.println("Blocks\t:\t" + blocks.size());

        AbstractEfficiencyMethod blockPurging = new ComparisonsBasedBlockPurging();
        blockPurging.applyProcessing(blocks);

        EntityIndex entityIndex = new EntityIndex(blocks);
        List<AbstractEfficiencyMethod> workflow = new ArrayList<AbstractEfficiencyMethod>();
        workflow.add(new BlockScheduling());
        workflow.add(new ComparisonPropagation(entityIndex));
        workflow.add(new ComparisonPruning(entityIndex));
        workflow.add(new ComparisonScheduling(false, entityIndex));

        int noOfMethods = workflow.size();
        for (int j = 0; j < noOfMethods - 1; j++) {
            AbstractEfficiencyMethod currentMethod = workflow.get(j);
            System.out.println("\n\nMethod\t:\t" + currentMethod.getName());
            long startingTime = System.currentTimeMillis();
            currentMethod.applyProcessing(blocks);
            long elapsedTime = System.currentTimeMillis() - startingTime;
            System.out.println("Time (in miliseconds)\t:\t" + elapsedTime);
        }
        AbstractDuplicatePropagation adp = new BilateralDuplicatePropagation( duplicatesPath);
        AbstractEfficiencyMethod currentMethod = workflow.get(workflow.size()-1);
        System.out.println("\n\nMethod\t:\t" + currentMethod.getName());
        long startingTime = System.currentTimeMillis();
        currentMethod.applyProcessing(blocks, adp);
        long elapsedTime = System.currentTimeMillis() - startingTime;
        System.out.println("Time (in miliseconds)\t:\t" + elapsedTime);
    }
}