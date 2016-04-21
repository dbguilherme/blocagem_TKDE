package SchemaExperiments.TimeMeasurements;

import BlockBuilding.MemoryBased.ExtendedSortedNeighborhoodBlocking;
import BlockBuilding.MemoryBased.SchemaBased.ExtendedSortedNeighborhood;
import BlockProcessing.BlockRefinement.BlockFiltering;
import BlockProcessing.ComparisonRefinement.ComparisonPropagation;
import DataStructures.AbstractBlock;
import SchemaExperiments.Utilities;
import Utilities.ExecuteBlockComparisons;
import Utilities.StatisticsUtilities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gap2
 */
public class ExtendedSortedNeighborhoodPerformance {

    private final static double FILTERING_RATIO = 0.64;

    public static void main(String[] args) throws IOException {
        int[] windows = {2, 3, 5, 7, 10};
        int[][] blockingKeys = {{0, 1}, {2, 3}, {4, 5}};
        for (int datasetId = 0; datasetId < 4; datasetId++) {
            System.out.println("\n\nCurrent dataset id\t:\t" + (datasetId + 1));

            List<Double>[] overheadTimes = new List[4];
            List<Double>[] resolutionTimes = new List[4];
            for (int i = 0; i < 4; i++) {
                overheadTimes[i] = new ArrayList<>();
                resolutionTimes[i] = new ArrayList<>();
            }

            for (int window : windows) {
                System.out.println("\n\nCurrent window\t:\t" + window);

                for (int blockKeyId = 0; blockKeyId < 3; blockKeyId++) {
                    System.out.println("\n\nCurrent blocking key id\t:\t" + (blockKeyId + 1));

                    for (int iteration = 0; iteration < 10; iteration++) {
                        long time1 = System.currentTimeMillis();
                        ExtendedSortedNeighborhood blockingMethod = new ExtendedSortedNeighborhood(window, blockingKeys[blockKeyId], Utilities.getProfileType(datasetId), Utilities.getEntities(datasetId));
                        List<AbstractBlock> blocks = blockingMethod.buildBlocks();

                        ComparisonPropagation cp = new ComparisonPropagation();
                        cp.applyProcessing(blocks);

                        long time2 = System.currentTimeMillis();
                        long overheadTime = time2 - time1;
                        overheadTimes[blockKeyId].add(new Double(overheadTime));
                        System.out.println("Overhead time\t:\t" + overheadTime);

                        ExecuteBlockComparisons ebc = new ExecuteBlockComparisons(Utilities.getEntitiesPath(datasetId));
                        long comparisonsTime = ebc.comparisonExecution(blocks);
                        System.out.println("Resolution time\t:\t" + comparisonsTime);
                        resolutionTimes[blockKeyId].add(new Double(comparisonsTime + overheadTime));
                    }
                }

                long time1 = System.currentTimeMillis();

                for (int iteration = 0; iteration < 10; iteration++) {
                    ExtendedSortedNeighborhoodBlocking blockingMethod = new ExtendedSortedNeighborhoodBlocking(window, Utilities.getEntities(datasetId));
                    List<AbstractBlock> blocks = blockingMethod.buildBlocks();

                    BlockFiltering bf = new BlockFiltering(FILTERING_RATIO);
                    bf.applyProcessing(blocks);

                    ComparisonPropagation cp = new ComparisonPropagation();
                    cp.applyProcessing(blocks);

                    long time2 = System.currentTimeMillis();
                    long overheadTime = time2 - time1;
                    overheadTimes[3].add(new Double(overheadTime));
                    System.out.println("Overhead time\t:\t" + overheadTime);

                    ExecuteBlockComparisons ebc = new ExecuteBlockComparisons(Utilities.getEntitiesPath(datasetId));
                    long comparisonsTime = ebc.comparisonExecution(blocks);
                    System.out.println("Resolution time\t:\t" + comparisonsTime);
                    resolutionTimes[3].add(new Double(comparisonsTime + overheadTime));
                }
            }

            for (int i = 0; i < 4; i++) {
                double averageOTime = StatisticsUtilities.getMeanValue(overheadTimes[i]);
                double averageRTime = StatisticsUtilities.getMeanValue(resolutionTimes[i]);
                double stDevOTime = StatisticsUtilities.getStandardDeviation(averageOTime, overheadTimes[i]);
                double stDevRTime = StatisticsUtilities.getStandardDeviation(averageRTime, resolutionTimes[i]);
                System.out.println("\n\nMethod\t:\t" + (i + 1) + "\t:\tAverage Overhead time=" + averageOTime);
                System.out.println("Method\t:\t" + (i + 1) + "\t:\tAverage Resolution time=" + averageRTime);
                System.out.println("Method\t:\t" + (i + 1) + "\t:\tSt. Dev. Overhead time=" + stDevOTime);
                System.out.println("Method\t:\t" + (i + 1) + "\t:\tSt. Dev. Resolution time=" + stDevRTime);
            }
        }
    }
}
