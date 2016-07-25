package Experiments;

import DataStructures.AbstractBlock;
import DataStructures.Attribute;
import DataStructures.Comparison;
import DataStructures.EntityProfile;
import BlockProcessing.AbstractEfficiencyMethod;
import BlockProcessing.BlockRefinement.ComparisonsBasedBlockPurging;
import BlockProcessing.ComparisonRefinement.AbstractDuplicatePropagation;
import BlockProcessing.ComparisonRefinement.UnilateralDuplicatePropagation;
import MetaBlocking.WeightedEdgePruning;
import Utilities.BlockStatistics;
import Utilities.SerializationUtilities;
import MetaBlocking.WeightingScheme;
import Utilities.ExportBlocks;
import Utilities.ProfileComparison;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import BlockBuilding.MemoryBased.TokenBlocking;

/**
 *
 * @author gap2
 */
public class MetaBlockingOnDisk {

    public static void main(String[] args) throws IOException {
    	String mainDirectory = System.getProperty("user.home")+"/Dropbox/blocagem/bases/sintetica/";
        //       String[] datasets = {"10K", "50K", "100K", "200K", "300K", "1M", "2M"};
        String[] datasets = {"10K"};
        for (String dataset : datasets) {
            System.out.println("\n\nCurrent dataset name\t:\t" + dataset);
            String profilesPath = mainDirectory + dataset + "profiles";
            List[] profiles = new List[1];
    		profiles[0] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPath);
    		//profiles[1] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPathB);
    		TokenBlocking imtb = new TokenBlocking(profiles);
    		
    		
           // String[] indexPath = { mainDirectory + dataset + "index" };

            final List<EntityProfile> entityProfiles = (ArrayList<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPath);
            System.out.println("Total Entity Profiles\t:\t" + entityProfiles.size());

            final AbstractDuplicatePropagation duplicatePropagation = new UnilateralDuplicatePropagation(mainDirectory + dataset + "IdDuplicates");
            System.out.println("Existing Duplicates\t:\t" + duplicatePropagation.getDuplicates().size());

            long startingTime = System.currentTimeMillis();

//            TokenBlocking tokenBlocking = new TokenBlocking(profilesPath, indexPath);
//            tokenBlocking.applyProcessing();
            
            System.out.println("\n\n");
            System.out.println("=================================================");
            System.out.println("+++Meta-Blocking using Weight Edge Pruning+++++++");
            System.out.println("=================================================");
            for (WeightingScheme scheme : WeightingScheme.values()) {
                System.out.println("\n\n\n\n\nWeighting scheme\t:\t" + scheme);
                List<AbstractBlock> blocks = imtb.buildBlocks();
//                ExportBlocks exportBlocks = new ExportBlocks(indexPath);
//                List<AbstractBlock> blocks = exportBlocks.getBlocks();
                System.out.println("Blocks\t:\t" + blocks.size());

                AbstractEfficiencyMethod blockPurging = new ComparisonsBasedBlockPurging(1.025);
                blockPurging.applyProcessing(blocks);
                    //SizeBasedBlockPurging blockPurging = new SizeBasedBlockPurging();
                //blockPurging.applyProcessing(blocks);
                System.out.println("Blocks remaining after block purging\t:\t" + blocks.size());

                WeightedEdgePruning ep = new WeightedEdgePruning(scheme);
                ep.applyProcessing(blocks);

                BlockStatistics blStats = new BlockStatistics(blocks, duplicatePropagation);
                blStats.applyProcessing();

//                for (AbstractBlock block : blocks) {
//                    Iterator<Comparison> iterator = block.getComparisonIterator();
//                    while (iterator.hasNext()) {
//                        Comparison comparison = iterator.next();
//                        EntityProfile profile1 = entityProfiles.get(comparison.getEntityId1());
//                        EntityProfile profile2 = entityProfiles.get(comparison.getEntityId2());
//
//                        double similarity = ProfileComparison.getJaccardSimilarity(profile1.getAttributes(), profile2.getAttributes());
//                       // System.out.println("\n\nMATCH\t:\t" + similarity);
//                      //  System.out.print("URL : " + profile1.getEntityUrl() + ", ");
////                        for (Attribute attribute : profile1.getAttributes()) {
////                          //  System.out.print(attribute.getName() + " = " + attribute.getValue() + ", ");
////                        }
////                        System.out.print("\nURL : " + profile2.getEntityUrl() + ", ");
////                        for (Attribute attribute : profile2.getAttributes()) {
////                            System.out.print(attribute.getName() + " = " + attribute.getValue() + ", ");
////                        }
////                        System.out.println();
//                    }
//                }
            }
        }
    }
}
