package SchemaExperiments.Scalability;

import DataStructures.AbstractBlock;
import BlockBuilding.MemoryBased.SchemaBased.StandardBlocking;
import BlockBuilding.MemoryBased.TokenBlocking;
import BlockProcessing.BlockRefinement.BlockFiltering;
import BlockProcessing.ComparisonRefinement.ComparisonPropagation;
import DataStructures.SchemaBasedProfiles.ProfileType;
import Utilities.BlockStatistics;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author G.A.P. II
 */
public class StandardBlockingExperiment {

    private final static double FILTERING_RATIO = 0.64;

    public static void main(String[] args) throws IOException {
        int[][] blockingKeys = {{0, 1}, {2, 3}, {4, 5}};
        for (int datasetId = 0; datasetId < 7; datasetId++) {
            System.out.println("\n\nCurrent dataset id\t:\t" + (datasetId + 1));

            for (int blockKeyId = 0; blockKeyId < 3; blockKeyId++) {
                System.out.println("\n\nCurrent blocking key id\t:\t" + (blockKeyId + 1));
                StandardBlocking blockingMethod = new StandardBlocking(blockingKeys[blockKeyId], ProfileType.SYNTHETIC_PROFILE, Utilities.getEntities(datasetId));
                List<AbstractBlock> blocks = blockingMethod.buildBlocks();

                ComparisonPropagation cp = new ComparisonPropagation();
                cp.applyProcessing(blocks);

                BlockStatistics bStats = new BlockStatistics(blocks, Utilities.getGroundTruth(datasetId));
                bStats.applyProcessing();
            }

            TokenBlocking blockingMethod = new TokenBlocking(Utilities.getEntities(datasetId));
            List<AbstractBlock> blocks = blockingMethod.buildBlocks();

            BlockFiltering bf = new BlockFiltering(FILTERING_RATIO);
            bf.applyProcessing(blocks);

            ComparisonPropagation cp = new ComparisonPropagation();
            cp.applyProcessing(blocks);

            BlockStatistics bStats = new BlockStatistics(blocks, Utilities.getGroundTruth(datasetId));
            bStats.applyProcessing();
        }
    }
}
