package Experiments;

import DataStructures.AbstractBlock;
import BlockProcessing.AbstractEfficiencyMethod;
import BlockProcessing.BlockRefinement.BlockPruning;
import BlockProcessing.BlockRefinement.BlockScheduling;
import BlockProcessing.BlockRefinement.ComparisonsBasedBlockPurging;
import BlockProcessing.ComparisonRefinement.BilateralDuplicatePropagation;
import Utilities.BlockStatistics;
import Utilities.ExportBlocks;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author gap2
 */
public class BlockRefinementWorkflows {

    public static void main(String[] args) throws IOException {
        String mainDirectory = "/bigwork/nhvqgpap/bigDataset/";
        String[] indexDirs = {mainDirectory + "indices/characterTrigramAC1",
            mainDirectory + "indices/characterTrigramAC2"};
        String duplicatesPath = mainDirectory + "data/groundtruth";

        ExportBlocks exportBlocks = new ExportBlocks(indexDirs);
        List<AbstractBlock> blocks = exportBlocks.getBlocks();
        System.out.println("Blocks\t:\t" + blocks.size());

        long startingTime = System.currentTimeMillis();
        BlockStatistics blockStats = new BlockStatistics(blocks, new BilateralDuplicatePropagation(duplicatesPath));
        blockStats.applyProcessing();
        long elapsedTime = System.currentTimeMillis() - startingTime;
        System.out.println("Time (in miliseconds)\t:\t" + elapsedTime);

        AbstractEfficiencyMethod blockPurging = new ComparisonsBasedBlockPurging();//SizeBasedBlockPurging();
        System.out.println("\n\nMethod\t:\t" + blockPurging.getName());
        blockPurging.applyProcessing(blocks);
        startingTime = System.currentTimeMillis();
        blockStats.getPerformance(blocks, new BilateralDuplicatePropagation(duplicatesPath));
        elapsedTime = System.currentTimeMillis() - startingTime;
        System.out.println("Time (in miliseconds)\t:\t" + elapsedTime);

        BlockScheduling blockScheduling = new BlockScheduling();
        System.out.println("\n\nMethod\t:\t" + blockScheduling.getName());
        startingTime = System.currentTimeMillis();
        blockScheduling.applyProcessing(blocks, new BilateralDuplicatePropagation(duplicatesPath));
        elapsedTime = System.currentTimeMillis() - startingTime;
        System.out.println("Time (in miliseconds)\t:\t" + elapsedTime);

        BlockPruning blockPruning = new BlockPruning();
        System.out.println("\n\nMethod\t:\t" + blockPruning.getName());
        startingTime = System.currentTimeMillis();
        blockPruning.applyProcessing(blocks, new BilateralDuplicatePropagation(duplicatesPath));
        elapsedTime = System.currentTimeMillis() - startingTime;
        System.out.println("Time (in miliseconds)\t:\t" + elapsedTime);
    }
}
