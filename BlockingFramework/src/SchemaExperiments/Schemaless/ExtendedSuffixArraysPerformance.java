package SchemaExperiments.Schemaless;

import DataStructures.AbstractBlock;
import BlockBuilding.MemoryBased.ExtendedSuffixArraysBlocking;
import BlockProcessing.ComparisonRefinement.ComparisonPropagation;
import SchemaExperiments.Utilities;
import Utilities.BlockStatistics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G.A.P. II
 */
public class ExtendedSuffixArraysPerformance {

    public static void main(String[] args) throws IOException {
        int[] maxBlockSizes = {5, 10, 20};
        int[] minSuffixLengths = {3, 5};
        for (int datasetId = 0; datasetId < 4; datasetId++) {
            System.out.println("\n\nCurrent dataset id\t:\t" + (datasetId + 1));

            List<Double> averageComparisons = new ArrayList<>();
            List<Double> averagePc = new ArrayList<>();
            List<Double> averagePq = new ArrayList<>();
            List<Double> averageRr = new ArrayList<>();
            for (int maxBlockSize : maxBlockSizes) {
                for (int minSuffixLength : minSuffixLengths) {
                    ExtendedSuffixArraysBlocking blockingMethod = new ExtendedSuffixArraysBlocking(maxBlockSize, minSuffixLength, Utilities.getEntities(datasetId));
                    List<AbstractBlock> blocks = blockingMethod.buildBlocks();

                    ComparisonPropagation cp = new ComparisonPropagation();
                    cp.applyProcessing(blocks);

                    BlockStatistics bStats = new BlockStatistics(blocks, Utilities.getGroundTruth(datasetId));
                    bStats.applyProcessing();
                    double[] metrics = bStats.applyProcessing();

                    averageComparisons.add(metrics[2]);
                    averagePc.add(metrics[0]);
                    averagePq.add(metrics[1]);
                    double bfComparisons = blockingMethod.getBruteForceComparisons();
                    double rr = 1 - metrics[2] / bfComparisons;
                    averageRr.add(rr);
                }
            }
            Utilities.printOutcome(averageComparisons, "Comparisons");
            Utilities.printOutcome(averagePc, "PC");
            Utilities.printOutcome(averagePq, "PQ");
            Utilities.printOutcome(averageRr, "RR");
        }
    }
}
