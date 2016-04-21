package Experiments;

import DataStructures.AbstractBlock;
import BlockBuilding.MemoryBased.TokenBlocking;
import BlockProcessing.AbstractEfficiencyMethod;
import BlockProcessing.BlockRefinement.ComparisonsBasedBlockPurging;
import BlockProcessing.ComparisonRefinement.AbstractDuplicatePropagation;
import BlockProcessing.ComparisonRefinement.ComparisonPropagation;
import BlockProcessing.ComparisonRefinement.UnilateralDuplicatePropagation;
import DataStructures.EntityProfile;
import Utilities.BlockStatistics;
import Utilities.ExecuteBlockComparisons;
import Utilities.SerializationUtilities;
import java.util.List;

/**
 *
 * @author gap2
 */

public class InMemoryExperiments {
    public static void main (String[] args) {
        String mainDirectory = "C:\\Data\\BlockAnalysis\\";
        String[] profilesPath = { mainDirectory+"profiles\\10Kprofiles",  
                                  mainDirectory+"profiles\\50Kprofiles",
                                  mainDirectory+"profiles\\100Kprofiles",
                                  mainDirectory+"profiles\\200Kprofiles",  
                                  mainDirectory+"profiles\\300Kprofiles",  
                                  mainDirectory+"profiles\\1Mprofiles",    
                                  mainDirectory+"profiles\\2Mprofiles"
        };
        
        String[] groundTruthPath = { mainDirectory+"groundtruth\\10KIdDuplicates",  
                                     mainDirectory+"groundtruth\\50KIdDuplicates",
                                     mainDirectory+"groundtruth\\100KIdDuplicates",
                                     mainDirectory+"groundtruth\\200KIdDuplicates",  
                                     mainDirectory+"groundtruth\\300KIdDuplicates",  
                                     mainDirectory+"groundtruth\\1MIdDuplicates",    
                                     mainDirectory+"groundtruth\\2MIdDuplicates"
        };

        for (int i = 0; i < profilesPath.length; i++) {
            System.out.println("\n\n\n\n\nCurrent dataset\t:\t" + profilesPath[i]);
                       
            
            final AbstractDuplicatePropagation duplicatePropagation = new UnilateralDuplicatePropagation(groundTruthPath[i]);
            System.out.println("Existing Duplicates\t:\t" + duplicatePropagation.getDuplicates().size());
            
            long startingTime = System.currentTimeMillis();
            
            List<EntityProfile>[] profiles = new List[1];
            profiles[0] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPath[i]);
            TokenBlocking imtb = new TokenBlocking(profiles);
            List<AbstractBlock> blocks = imtb.buildBlocks();
                
            AbstractEfficiencyMethod blockPurging = new ComparisonsBasedBlockPurging();
            blockPurging.applyProcessing(blocks);
            
            ComparisonPropagation cp = new ComparisonPropagation();
            cp.applyProcessing(blocks);
//
            long endingTime = System.currentTimeMillis();
            double overheadTime = endingTime-startingTime;
            
            String[] entityPaths = { profilesPath[i] };
            ExecuteBlockComparisons ebc = new ExecuteBlockComparisons(entityPaths);
            double resolutionTime = ebc.comparisonExecution(blocks);
            System.out.println("Overhead time\t:\t" + overheadTime);
            System.out.println("Resolution time\t:\t" + resolutionTime);

            duplicatePropagation.resetDuplicates();
            BlockStatistics bStats = new BlockStatistics(blocks, duplicatePropagation);
            bStats.applyProcessing();
        }
    }
}