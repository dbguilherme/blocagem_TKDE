package SchemaExperiments.Scalability;

import DataStructures.AbstractBlock;
import BlockBuilding.MemoryBased.SchemaBased.SuffixArrays;
import BlockBuilding.MemoryBased.SuffixArraysBlocking;
import BlockProcessing.ComparisonRefinement.ComparisonPropagation;
import DataStructures.SchemaBasedProfiles.ProfileType;
import Utilities.BlockStatistics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G.A.P. II
 */
public class SuffixArraysExperiment {

    public static void main(String[] args) throws IOException {
        int[] maxBlockSizes = {5, 10, 20};
        int[] minSuffixLengths = {3, 5};
        int[][] blockingKeys = {{0, 1}, {2, 3}, {4, 5}};
        for (int datasetId = 0; datasetId < 7; datasetId++) {
            System.out.println("\n\nCurrent dataset id\t:\t" + (datasetId + 1));

            for (int blockKeyId = 0; blockKeyId < 3; blockKeyId++) {
                List<Double> averageComparisons = new ArrayList<>();
                List<Double> averagePc = new ArrayList<>();
                List<Double> averagePq = new ArrayList<>();
                for (int maxBlockSize : maxBlockSizes) {
                    for (int minSuffixLength : minSuffixLengths) {
                        System.out.println("\n\nCurrent blocking key id\t:\t" + (blockKeyId + 1));
                        SuffixArrays blockingMethod = new SuffixArrays(maxBlockSize, minSuffixLength, blockingKeys[blockKeyId], ProfileType.SYNTHETIC_PROFILE, Utilities.getEntities(datasetId));
                        List<AbstractBlock> blocks = blockingMethod.buildBlocks();

                        ComparisonPropagation cp = new ComparisonPropagation();
                        cp.applyProcessing(blocks);

                        BlockStatistics bStats = new BlockStatistics(blocks, Utilities.getGroundTruth(datasetId));
                        double[] metrics = bStats.applyProcessing();

                        averageComparisons.add(metrics[2]);
                        averagePc.add(metrics[0]);
                        averagePq.add(metrics[1]);
                    }
                }
                Utilities.printOutcome(averageComparisons, "Comparisons");
                Utilities.printOutcome(averagePc, "PC");
                Utilities.printOutcome(averagePq, "PQ");
            }

            List<Double> averageComparisons = new ArrayList<>();
            List<Double> averagePc = new ArrayList<>();
            List<Double> averagePq = new ArrayList<>();
            for (int maxBlockSize : maxBlockSizes) {
                for (int minSuffixLength : minSuffixLengths) {
                    SuffixArraysBlocking blockingMethod = new SuffixArraysBlocking(maxBlockSize, minSuffixLength, Utilities.getEntities(datasetId));
                    List<AbstractBlock> blocks = blockingMethod.buildBlocks();

                    ComparisonPropagation cp = new ComparisonPropagation();
                    cp.applyProcessing(blocks);

                    BlockStatistics bStats = new BlockStatistics(blocks, Utilities.getGroundTruth(datasetId));
                    bStats.applyProcessing();
                    double[] metrics = bStats.applyProcessing();

                    averageComparisons.add(metrics[2]);
                    averagePc.add(metrics[0]);
                    averagePq.add(metrics[1]);
                }
            }
            Utilities.printOutcome(averageComparisons, "Comparisons");
            Utilities.printOutcome(averagePc, "PC");
            Utilities.printOutcome(averagePq, "PQ");
        }
    }
}
