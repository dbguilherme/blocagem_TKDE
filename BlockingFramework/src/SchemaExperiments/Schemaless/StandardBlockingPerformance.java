package SchemaExperiments.Schemaless;

import DataStructures.AbstractBlock;
import BlockBuilding.MemoryBased.TokenBlocking;
import BlockProcessing.BlockRefinement.BlockFiltering;
import BlockProcessing.ComparisonRefinement.ComparisonPropagation;
import SchemaExperiments.Utilities;
import Utilities.BlockStatistics;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author G.A.P. II
 */
public class StandardBlockingPerformance {

    private final static double FILTERING_RATIO = 0.64;

    public static void main(String[] args) throws IOException {
        for (int datasetId = 0; datasetId < 4; datasetId++) {
            System.out.println("\n\nCurrent dataset id\t:\t" + (datasetId + 1));

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
