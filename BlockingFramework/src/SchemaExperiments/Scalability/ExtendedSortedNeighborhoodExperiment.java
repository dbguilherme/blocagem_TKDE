package SchemaExperiments.Scalability;

import BlockBuilding.MemoryBased.ExtendedSortedNeighborhoodBlocking;
import BlockBuilding.MemoryBased.SchemaBased.ExtendedSortedNeighborhood;
import BlockProcessing.BlockRefinement.BlockFiltering;
import BlockProcessing.ComparisonRefinement.ComparisonPropagation;
import DataStructures.AbstractBlock;
import DataStructures.SchemaBasedProfiles.ProfileType;
import Utilities.BlockStatistics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G.A.P. II
 */
public class ExtendedSortedNeighborhoodExperiment {

    private final static double FILTERING_RATIO = 0.64;

    public static void main(String[] args) throws IOException {
        int[] windows = {2, 3, 5, 7, 10};
        int[][] blockingKeys = {{0, 1}, {2, 3}, {4, 5}};
        for (int datasetId = 5; datasetId < 7; datasetId++) {
            System.out.println("\n\nCurrent dataset id\t:\t" + (datasetId + 1));

            for (int blockKeyId = 0; blockKeyId < 3; blockKeyId++) {
                System.out.println("\n\nCurrent blocking key id\t:\t" + (blockKeyId + 1));

                List<Double> averageComparisons = new ArrayList<>();
                List<Double> averagePc = new ArrayList<>();
                List<Double> averagePq = new ArrayList<>();
                for (int window : windows) {
                    System.out.println("\n\nCurrent window\t:\t" + window);
                    ExtendedSortedNeighborhood blockingMethod = new ExtendedSortedNeighborhood(window, blockingKeys[blockKeyId], ProfileType.SYNTHETIC_PROFILE, Utilities.getEntities(datasetId));
                    List<AbstractBlock> blocks = blockingMethod.buildBlocks();

                    ComparisonPropagation cp = new ComparisonPropagation();
                    cp.applyProcessing(blocks);

                    BlockStatistics bStats = new BlockStatistics(blocks, Utilities.getGroundTruth(datasetId));
                    double[] metrics = bStats.applyProcessing();

                    averageComparisons.add(metrics[2]);
                    averagePc.add(metrics[0]);
                    averagePq.add(metrics[1]);
                }
                Utilities.printOutcome(averageComparisons, "Comparisons");
                Utilities.printOutcome(averagePc, "PC");
                Utilities.printOutcome(averagePq, "PQ");
            }

            List<Double> averageComparisons = new ArrayList<>();
            List<Double> averagePc = new ArrayList<>();
            List<Double> averagePq = new ArrayList<>();
            for (int window : windows) {
                System.out.println("\n\nCurrent window\t:\t" + window);
                ExtendedSortedNeighborhoodBlocking blockingMethod = new ExtendedSortedNeighborhoodBlocking(window, Utilities.getEntities(datasetId));
                List<AbstractBlock> blocks = blockingMethod.buildBlocks();

                BlockFiltering bf = new BlockFiltering(FILTERING_RATIO);
                bf.applyProcessing(blocks);

                ComparisonPropagation cp = new ComparisonPropagation();
                cp.applyProcessing(blocks);

                BlockStatistics bStats = new BlockStatistics(blocks, Utilities.getGroundTruth(datasetId));
                double[] metrics = bStats.applyProcessing();

                averageComparisons.add(metrics[2]);
                averagePc.add(metrics[0]);
                averagePq.add(metrics[1]);
            }
            Utilities.printOutcome(averageComparisons, "Comparisons");
            Utilities.printOutcome(averagePc, "PC");
            Utilities.printOutcome(averagePq, "PQ");
        }
    }
}
