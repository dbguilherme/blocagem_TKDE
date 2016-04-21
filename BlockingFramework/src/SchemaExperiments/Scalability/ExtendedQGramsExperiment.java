package SchemaExperiments.Scalability;

import BlockBuilding.MemoryBased.ExtendedQGramsBlocking;
import DataStructures.AbstractBlock;
import BlockBuilding.MemoryBased.SchemaBased.ExtendedCharacterNGrams;
import BlockProcessing.BlockRefinement.BlockFiltering;
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
public class ExtendedQGramsExperiment {

    private final static double FILTERING_RATIO = 0.64;

    public static void main(String[] args) throws IOException {
        int[] qs = {2, 3};
        double[] thresholds = {0.8, 0.9};
        int[][] blockingKeys = {{0, 1}, {2, 3}, {4, 5}};
        for (int datasetId = 0; datasetId < 7; datasetId++) {
            System.out.println("\n\nCurrent dataset id\t:\t" + (datasetId + 1));

            for (int blockKeyId = 0; blockKeyId < 3; blockKeyId++) {
                System.out.println("\n\nCurrent blocking key id\t:\t" + (blockKeyId + 1));

                List<Double> averageComparisons = new ArrayList<>();
                List<Double> averagePc = new ArrayList<>();
                List<Double> averagePq = new ArrayList<>();
                for (int q : qs) {
                    for (double t : thresholds) {
                        ExtendedCharacterNGrams blockingMethod = new ExtendedCharacterNGrams(t, q, blockingKeys[blockKeyId], ProfileType.SYNTHETIC_PROFILE, Utilities.getEntities(datasetId));
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
            for (int q : qs) {
                for (double t : thresholds) {
                    ExtendedQGramsBlocking blockingMethod = new ExtendedQGramsBlocking(t, q, Utilities.getEntities(datasetId));
                    List<AbstractBlock> blocks = blockingMethod.buildBlocks();

                    BlockFiltering bf = new BlockFiltering(FILTERING_RATIO);
                    bf.applyProcessing(blocks);

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
