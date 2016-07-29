package Experiments;

import DataStructures.AbstractBlock;
import DataStructures.EntityProfile;
import BlockProcessing.AbstractEfficiencyMethod;
import BlockProcessing.BlockRefinement.BlockPruning;
import BlockProcessing.BlockRefinement.BlockScheduling;
import BlockProcessing.BlockRefinement.ComparisonsBasedBlockPurging;
import BlockProcessing.ComparisonRefinement.BilateralDuplicatePropagation;
import Utilities.BlockStatistics;
import Utilities.ExportBlocks;
import Utilities.SerializationUtilities;

import java.io.IOException;
import java.util.List;

import BlockBuilding.MemoryBased.TokenBlocking;

/**
 *
 * @author gap2
 */
public class BlockRefinementWorkflows {

    public static void main(String[] args) throws IOException {
//        String mainDirectory = "/bigwork/nhvqgpap/bigDataset/";
//        String[] indexDirs = {mainDirectory + "indices/characterTrigramAC1",
//            mainDirectory + "indices/characterTrigramAC2"};
//       
//        
//        String duplicatesPath = mainDirectory + "data/groundtruth";

        String mainDirectory = System.getProperty("user.home")+"/Dropbox/blocagem/bases/base_clean_serializada";
        String profilesPathA= mainDirectory+"/dblp";
    	String profilesPathB= mainDirectory+"/scholar";
    	String duplicatesPath =  mainDirectory+ "/groundtruth"; 
    	List[] profiles = new List[2];
		profiles[0] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPathA);
		profiles[1] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPathB);
		TokenBlocking imtb = new TokenBlocking(profiles);
		 List<AbstractBlock> blocks = imtb.buildBlocks();
    	
        
      //  ExportBlocks exportBlocks = new ExportBlocks(indexDirs);
      //  List<AbstractBlock> blocks = exportBlocks.getBlocks();
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
