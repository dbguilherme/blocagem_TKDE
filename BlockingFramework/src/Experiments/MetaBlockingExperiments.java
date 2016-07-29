package Experiments;

import DataStructures.AbstractBlock;
import DataStructures.EntityProfile;
import BlockProcessing.AbstractEfficiencyMethod;
import BlockProcessing.BlockRefinement.ComparisonsBasedBlockPurging;
import BlockProcessing.BlockRefinement.SizeBasedBlockPurging;
import BlockProcessing.ComparisonRefinement.AbstractDuplicatePropagation;
import BlockProcessing.ComparisonRefinement.BilateralDuplicatePropagation;
import BlockProcessing.ComparisonRefinement.UnilateralDuplicatePropagation;
import MetaBlocking.WeightedEdgePruning;
import MetaBlocking.CardinalityEdgePruning;
import Utilities.BlockStatistics;
import MetaBlocking.CardinalityNodePruning;
import MetaBlocking.WeightedNodePruning;
import MetaBlocking.WeightingScheme;
import Utilities.ExportBlocks;
import Utilities.SerializationUtilities;

import java.io.IOException;
import java.util.List;

import BlockBuilding.MemoryBased.TokenBlocking;

/**
 * vi
 *
 * @author gap2
 */
public class MetaBlockingExperiments {

    /**
     * Experiments of Table 2 in the paper 
     * "Meta-Blocking: Taking Entity Resolution to the Next Level"
     * in TKDE 2014
     * 
     */
    
    public static List<AbstractBlock> getBlocks(String[] indexDirs) throws IOException {
        ExportBlocks exportBlocks = new ExportBlocks(indexDirs);
        List<AbstractBlock> blocks = exportBlocks.getBlocks();
        System.out.println("Blocks\t:\t" + blocks.size());
        
        SizeBasedBlockPurging blockPurging = new SizeBasedBlockPurging();
        blockPurging.applyProcessing(blocks);
        System.out.println("Blocks remaining after block purging\t:\t" + blocks.size());
        
//        BlockStatistics blStats1 = new BlockStatistics(blocks, new BilateralDuplicatePropagation("C:\\Data\\Movies\\moviesIdGroundTruth"));
//        blStats1.applyProcessing();
        
        return blocks;
    }
    
    public static void main(String[] args) throws IOException {
    	
    	
//    	String mainDirectory = System.getProperty("user.home")+"/Dropbox/blocagem/bases/sintetica/";
//		String profilesPathA = mainDirectory+"50Kprofiles";
//		//String profilesPathB = mainDirectory+"/token/dataset2_dbpedia";
//		String duplicatesPath =  mainDirectory+ "50KIdDuplicates"; 
    	
    	String mainDirectory = System.getProperty("user.home")+"/Dropbox/blocagem/bases/base_clean_serializada";
    	String profilesPathA= mainDirectory+"/dblp";
    	String profilesPathB= mainDirectory+"/scholar";
    	String duplicatesPath =  mainDirectory+ "/groundtruth"; 
    	
		List[] profiles = new List[2];
		profiles[0] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPathA);
		profiles[1] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPathB);
		TokenBlocking imtb = new TokenBlocking(profiles);
		
		// final AbstractDuplicatePropagation adp = new UnilateralDuplicatePropagation(duplicatesPath);
         //System.out.println("Existing Duplicates\t:\t" + adp.getDuplicates().size());

		
    	//String mainDirectory = "C:\\Data\\Movies\\";
        //String[] indexDir = { mainDirectory+"Indices\\tokenBlocking" };
       // String duplicatesPath = mainDirectory + "moviesIdGroundTruth";
            
        System.out.println("\n\n\n\n\n");
        System.out.println("=================================================");
        System.out.println("++++++++++++++++Weight Edge Pruning++++++++++++++");
        System.out.println("=================================================");
        for (WeightingScheme scheme : WeightingScheme.values()) {
            System.out.println("\n\n\n\n\nWeighting scheme\t:\t" + scheme);
            List<AbstractBlock> blocks = imtb.buildBlocks();
           // List<AbstractBlock> blocks = imtb.buildBlocks();
         //   List<AbstractBlock> blocks = getBlocks(indexDir);
          //  AbstractEfficiencyMethod blockPurging = new ComparisonsBasedBlockPurging(1.025);
			//blockPurging.applyProcessing(blocks);
            AbstractDuplicatePropagation adp = new BilateralDuplicatePropagation(duplicatesPath);
            
            WeightedEdgePruning ep = new WeightedEdgePruning(scheme);
            ep.applyProcessing(blocks);
            
            BlockStatistics blStats = new BlockStatistics(blocks, adp);
            blStats.applyProcessing();
        }

        System.out.println("\n\n\n\n\n");
        System.out.println("=================================================");
        System.out.println("++++++++++++++++++++Top-K Edges++++++++++++++++++");
        System.out.println("=================================================");
        for (WeightingScheme scheme : WeightingScheme.values()) {
            System.out.println("\n\n\n\n\nWeighting scheme\t:\t" + scheme);
           // List<AbstractBlock> blocks = getBlocks(indexDir);
            List<AbstractBlock> blocks = imtb.buildBlocks();
            AbstractDuplicatePropagation adp = new BilateralDuplicatePropagation(duplicatesPath);
            
            CardinalityEdgePruning tked = new CardinalityEdgePruning(scheme);
            tked.applyProcessing(blocks);
            
            BlockStatistics blStats = new BlockStatistics(blocks, adp);
            blStats.applyProcessing();
        }

        System.out.println("\n\n\n\n\n");
        System.out.println("=================================================");
        System.out.println("++++++++++++++++Weight Node Pruning++++++++++++++");
        System.out.println("=================================================");
        for (WeightingScheme scheme : WeightingScheme.values()) {
            System.out.println("\n\n\n\n\nWeighting scheme\t:\t" + scheme);
            //List<AbstractBlock> blocks = getBlocks(indexDir);
           List<AbstractBlock> blocks = imtb.buildBlocks();
            AbstractDuplicatePropagation adp = new BilateralDuplicatePropagation(duplicatesPath);
            
            WeightedNodePruning np = new WeightedNodePruning(scheme);
            np.applyProcessing(blocks);
            
            BlockStatistics blStats = new BlockStatistics(blocks, adp);
            blStats.applyProcessing();
        }

        System.out.println("\n\n\n\n\n");
        System.out.println("=================================================");
        System.out.println("++++++++++++++++k-Nearest Entities+++++++++++++++");
        System.out.println("=================================================");
        for (WeightingScheme scheme : WeightingScheme.values()) {
            System.out.println("\n\n\n\n\nWeighting scheme\t:\t" + scheme);
           // List<AbstractBlock> blocks = getBlocks(indexDir);
           List<AbstractBlock> blocks = imtb.buildBlocks();
            AbstractDuplicatePropagation adp = new BilateralDuplicatePropagation(duplicatesPath);
            
            CardinalityNodePruning knen = new CardinalityNodePruning(scheme);
            knen.applyProcessing(blocks);
            
            BlockStatistics blStats = new BlockStatistics(blocks, adp);
            blStats.applyProcessing();
        }
    }
}