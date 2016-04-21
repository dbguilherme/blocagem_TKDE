package SchemaExperiments.Scalability;

import DataStructures.EntityProfile;
import DataStructures.SchemaBasedProfiles.AbstractProfile;
import DataStructures.SchemaBasedProfiles.SyntheticProfile;
import BlockProcessing.ComparisonRefinement.AbstractDuplicatePropagation;
import BlockProcessing.ComparisonRefinement.UnilateralDuplicatePropagation;
import Utilities.SerializationUtilities;
import Utilities.StatisticsUtilities;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G.A.P. II
 */
public class Utilities {

    private final static String mainGTDirectory = "E:\\Data\\groundTruth\\";
//    private final static String mainGTDirectory = "/media/gap2/Data/Data/groundtruth/";
//    private final static String mainGTDirectory = "/home/gpapadakis/data/groundtruth/";
    private final static AbstractDuplicatePropagation[] adp = {
        new UnilateralDuplicatePropagation(mainGTDirectory + "10KIdDuplicates"),
        new UnilateralDuplicatePropagation(mainGTDirectory + "50KIdDuplicates"),
        new UnilateralDuplicatePropagation(mainGTDirectory + "100KIdDuplicates"),
        new UnilateralDuplicatePropagation(mainGTDirectory + "200KIdDuplicates"),
        new UnilateralDuplicatePropagation(mainGTDirectory + "300KIdDuplicates"),
        new UnilateralDuplicatePropagation(mainGTDirectory + "1MIdDuplicates"),
        new UnilateralDuplicatePropagation(mainGTDirectory + "2MIdDuplicates")
    };

    private final static String profilesDirectory = "E:\\Data\\profiles\\";
//    private static final String profilesDirectory = "/media/gap2/Data/Data/profiles/";
//    private static final String profilesDirectory = "/home/gpapadakis/data/profiles/";
    private static final String[] profilesPaths = {profilesDirectory + "10Kprofiles",
        profilesDirectory + "50Kprofiles",
        profilesDirectory + "100Kprofiles",
        profilesDirectory + "200Kprofiles",
        profilesDirectory + "300Kprofiles",
        profilesDirectory + "1Mprofiles",
        profilesDirectory + "2Mprofiles"
    };

    public static List<EntityProfile>[] getEntities(int datasetId) {
        List<EntityProfile>[] profiles = new List[1];
        profiles[0] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPaths[datasetId]);
            
        return profiles;
    }

    public static String[] getEntitiesPath(int datasetId) {
        String[] paths = {profilesPaths[datasetId]};
        return paths;
    }

    public static List<AbstractProfile> getEntityCollection(int datasetId) {
        List<EntityProfile> entityProfiles = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPaths[datasetId]);

        List<AbstractProfile> profiles = new ArrayList<>();
        for (EntityProfile eProfile : entityProfiles) {
            profiles.add(new SyntheticProfile(eProfile));
        }
        return profiles;
    }

    public static AbstractDuplicatePropagation getGroundTruth(int datasetId) {
        return adp[datasetId];
    }

    public static void printOutcome(List<Double> instances, String measure) {
        double meanValue = StatisticsUtilities.getMeanValue(instances);
        System.out.println("Average " + measure + "\t:\t" + meanValue);
        System.out.println("Standard Deviation " + measure + "\t:\t" + StatisticsUtilities.getStandardDeviation(meanValue, instances));
    }
}
