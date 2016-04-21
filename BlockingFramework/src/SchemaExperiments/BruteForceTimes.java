package SchemaExperiments;

import DataStructures.EntityProfile;
import Utilities.ProfileComparison;
import Utilities.StatisticsUtilities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gap2
 */
public class BruteForceTimes {

    public static void main(String[] args) throws IOException {
        List<Double>[] resolutionTimes = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            resolutionTimes[i] = new ArrayList<>();
        }

        for (int iteration = 0; iteration < 10; iteration++) {
            System.out.println("Current iteration\t:\t" + (iteration+1));
            for (int datasetId = 0; datasetId < 4; datasetId++) {
                List<EntityProfile>[] entityProfiles = Utilities.getEntities(datasetId);
                EntityProfile[] dataset = entityProfiles[0].toArray(new EntityProfile[entityProfiles[0].size()]);

                long time1 = System.currentTimeMillis();
                for (int i = 0; i < dataset.length; i++) {
                    for (int j = i + 1; j < dataset.length; j++) {
                        ProfileComparison.getJaccardSimilarity(dataset[i].getAttributes(),
                                dataset[j].getAttributes());
                    }
                }
                long time2 = System.currentTimeMillis();
                double totalTime = time2 - time1;
                System.out.println("Total time\t:\t" + totalTime);
                resolutionTimes[datasetId].add(totalTime);
            }
        }
        
        for (int i = 0; i < 4; i++) {
            System.out.println("Av. BF-time for dataset " + (i+1) + "\t:\t" + StatisticsUtilities.getMeanValue(resolutionTimes[i]));
        }
    }
}