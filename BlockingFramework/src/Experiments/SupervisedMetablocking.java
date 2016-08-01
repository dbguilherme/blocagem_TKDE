package Experiments;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import BlockBuilding.MemoryBased.ExtendedQGramsBlocking;
import BlockBuilding.MemoryBased.QGramsBlocking;
import BlockBuilding.MemoryBased.TokenBlocking;
import BlockProcessing.AbstractEfficiencyMethod;
import BlockProcessing.BlockRefinement.ComparisonsBasedBlockPurging;
import BlockProcessing.BlockRefinement.SizeBasedBlockPurging;
import BlockProcessing.ComparisonRefinement.BilateralDuplicatePropagation;
import DataStructures.AbstractBlock;
import DataStructures.Comparison;
import DataStructures.EntityIndex;
import DataStructures.EntityProfile;
import DataStructures.IdDuplicates;
import SupervisedMetablocking.SupervisedCNP;
import SupervisedMetablocking.SupervisedWEP;
import Utilities.BlockStatistics;
import Utilities.ComparisonIterator;
import Utilities.Converter;
import Utilities.ExecuteBlockComparisons;
import Utilities.ExportBlocks;
import Utilities.SerializationUtilities;
import Utilities.blockHash;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import sun.font.StrikeCache;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;

import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.SelectedTag;


/**
 *
 * @author gap2
 */
public class SupervisedMetablocking {

	private final static int ITERATIONS = 10;
	//String  atributos_value[][] = {{ "id", "name" , "age","postcode", "state","given_name","date_of_birth","suburb","address_2","address_1","surname","soc_sec_id","phone_number","street_number"},{ "id", "title" , "authors", "venue", "year"}};
	
//	private static Classifier[] getSupervisedCepClassifiers() {
//		NaiveBayes naiveBayes = new NaiveBayes();
//		naiveBayes.setUseKernelEstimator(false);
//		naiveBayes.setUseSupervisedDiscretization(true);
//
//		BayesNet bayesNet = new BayesNet();
//
//		Classifier[] classifiers = new Classifier[2];
//		classifiers[0] = naiveBayes;
//		classifiers[1] = bayesNet;
//		return classifiers;
//	}

//	private static Classifier[] getSupervisedCnpClassifiers() {
//		return getSupervisedCepClassifiers();
//	}

	private static Classifier[] getSupervisedWepClassifiers() {
//		NaiveBayes naiveBayes = new NaiveBayes();
//		//naiveBayes.setUseKernelEstimator(false);
//		//naiveBayes.setUseSupervisedDiscretization(false);
//
//		
//		//RandomForest rf= new RandomForest();
//		RandomForest rf = new RandomForest();
//	//	rf.setNumTrees(10);
//		
//	//	rf.setNumTrees(200);
//		//rf.setNumTrees(10);
//		//rf.
//		//rf.setN
//		//Ibk
//		J48 j48 = new J48();
//		j48.setMinNumObj(2);
//		j48.setConfidenceFactor((float) 0.05);
//
////		LibSVM libSVM=new LibSVM();
//		SMO smo = new SMO();
//		//smo.setBuildLogisticModels(true);
////		smo.setKernel(new RBFKernel());
////		smo.setC(9.0);
//
//		BayesNet bayesNet = new BayesNet();
//		//bayesNet.
////		LibSVM sv = new LibSVM();
////		sv.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_POLYNOMIAL, LibSVM.TAGS_KERNELTYPE));
//		//sv.setKernelType(LibSVM.KERNELTYPE_POLYNOMIAL);
//		//sv.setCost(2);
//
//
//
//
//		//		svm_parameter param;
//		//		param = new svm_parameter();
//		//		
//		//		param=	get_grid(param);
//		//		// default values
//		//		
//		//		sv.setGamma(param.gamma);
//		//		sv.setCost(param.C);
//
//
//		//	svm_problem prob = new svm_problem();
//
//		//String options = ( "-S 0 -K 0 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1" );
//		//String[] optionsArray = options.split( " " );
//		//		try {
//		//			sv.setOptions(optionsArray);
//		//		} catch (Exception e) {
//		//			// TODO Auto-generated catch block
//		//			e.printStackTrace();
//		//		}
//		//classifiers[3].setOptions( optionsArray );
//
//		Classifier[] classifiers = new Classifier[1];
//		classifiers[0] =naiveBayes;// naiveBayes;
//		///classifiers[1] = j48;
//		//classifiers[2] = smo;
//	//	classifiers[1] = j48;
//	//	classifiers[2] = rf;
		
	     NaiveBayes naiveBayes = new NaiveBayes();
	        naiveBayes.setUseKernelEstimator(false);
	        naiveBayes.setUseSupervisedDiscretization(false);

	        J48 j48 = new J48();
	        j48.setMinNumObj(5);
	        j48.setConfidenceFactor((float) 0.10);

	        SMO smo = new SMO();
	       //((Object) smo).setBuildLogisticModels(true);
	        smo.setKernel(new PolyKernel());
	        smo.setC(9.0);

	        BayesNet bayesNet = new BayesNet();

	        Classifier[] classifiers = new Classifier[1];
	        classifiers[0] = naiveBayes;
//	        classifiers[1] = j48;
//	        classifiers[2] = smo;
//	        classifiers[3] = bayesNet;
		return classifiers;
	}


	public static svm_parameter get_grid(svm_parameter param) {
		PrintWriter out =null;
		BufferedReader in=null;
		BufferedReader in_t=null;
		Process proc = null;
		String line=null;		
		String userHome = System.getProperty("user.home");
		System.out.println(userHome);
		try {
			proc = Runtime.getRuntime().exec("/bin/bash", null, new File(userHome+"/Downloads/libsvm-3.20/tools/"));
			if (proc != null) {
				in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		if (proc != null) {
			System.out.println("antes do grid.py----------python grid.py /tmp/test.libsvm");
			out.println("python grid.py /tmp/test.libsvm");//reverrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
			try {
				while ((line = in.readLine()) != null ) {
					if(!line.contains("[local]")){
						String arg[]=line.split(" ");
						param.C= Double.parseDouble(arg[0]);
						param.gamma= Double.parseDouble(arg[1]);
						System.out.println("valor c  " + param.C + "  " + param.gamma);
						return param;
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("depois do grid.py----------");
		System.out.println("depois do grid.py----------");
		return null;    
	}

	

	private static ArrayList<Comparison>[] testeParaGerarAscomparações(List<AbstractBlock> blocks, int[] nblocks, ExecuteBlockComparisons ebc) {

		List<AbstractBlock> blocks_select = new ArrayList<AbstractBlock>();
		ArrayList<Comparison>[] listComparison = (ArrayList<Comparison>[])new ArrayList[10];
		EntityIndex entityIndex = new EntityIndex(blocks);
		for (int i = 0; i < 10; i++) {
			listComparison[i]= new ArrayList<Comparison>();
		}
		//List<Comparison>[] listComparison= new ArrayList<Comparison>()[10];
		Random r = new Random();
		for (int i = 0; i < blocks.size(); i++) {		
			//blocks_select.add(blocks.get(i));
			//if(blocks.get(i).getNoOfComparisons()>2)
			{
				AbstractBlock b = blocks.get(i);
				List<Comparison> c = b.getComparisons();
				
				for(Comparison com:c){
					
					com.teste=blocks.get(i).getBlockIndex();
					com.sim=ebc.getSImilarity(com.getEntityId1(),com.getEntityId2());
					int level=(int) Math.floor(com.sim*10);
					
					final List<Integer> commonBlockIndices =  entityIndex.getCommonBlockIndices(com.teste, com);
					if (commonBlockIndices == null) {
						continue;
					}
//					if(commonBlockIndices.size()<1)
//						System.out.println(commonBlockIndices.size());
					if(com.sim>= ((double)level*0.1) && com.sim<= ((double)(level+1)*0.1)){							
						int temp=r.nextInt(nblocks[level]);
						if((temp<500) || (temp<(nblocks[level]*0.1))){
							//continue;
							listComparison[level].add(com);
						}
						
					}
				}
			}
		}		
		for (int j= 0; j < 10; j++) {
			System.err.println("list size ---"+ listComparison[j].size());			
		}		
		return listComparison;
	}
	
	
	public static void main(String[] args) throws IOException, Exception {
		System.out.println( System.getProperty("user.home"));
		String mainDirectory;
		String profilesPathA=null;
		String profilesPathB=null;
		String groundTruthPath = null;
		
		switch(args[0]){
		case "1":	       
			mainDirectory = System.getProperty("user.home")+"/Dropbox/blocagem/bases/sintetica";
			profilesPathA =  mainDirectory+"/"+args[1]+"profiles"	;	
			groundTruthPath =  mainDirectory+"/"+args[1]+"IdDuplicates";	
			System.out.println("-----------"+mainDirectory);
			break;
		case "2":
			mainDirectory = System.getProperty("user.home")+"/Dropbox/blocagem/bases/base_clean_serializada";
			profilesPathA= mainDirectory+"/dblp";
			profilesPathB= mainDirectory+"/scholar";
			groundTruthPath =  mainDirectory+ "/groundtruth"; 
			break;
		case "3":
			mainDirectory = System.getProperty("user.home")+"/Dropbox/blocagem/bases/movies";

			profilesPathA= mainDirectory+"/token/dataset1_imdb";
			profilesPathB= mainDirectory+"/token/dataset2_dbpedia";
			groundTruthPath =  mainDirectory+ "/ground/groundtruth"; 
			break;
		case "4":
			mainDirectory = System.getProperty("user.home")+"/Dropbox/blocagem/bases/acm";
			profilesPathA= mainDirectory+"/dataset";
			//profilesPathB= mainDirectory+"/dataset2_gp";

			groundTruthPath =  mainDirectory+ "/groundtruth"; 
			break;
			
		case "5":
			mainDirectory = System.getProperty("user.home")+"/Dropbox/blocagem/bases/cddb/";
			profilesPathA =  mainDirectory+"/"+"cddbProfiles"	;	
			groundTruthPath =  mainDirectory+"/"+"cddbIdDuplicates";	
			break;
		}
	
		Set<IdDuplicates> duplicatePairs = (HashSet<IdDuplicates>) SerializationUtilities.loadSerializedObject(groundTruthPath);
		System.out.println("Existing duplicates\t:\t" + duplicatePairs.size());

		List<AbstractBlock> blocks = null;

		List<EntityProfile>[] profiles ;
		if(profilesPathB != null){
					profiles = new List[2];
					profiles[0] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPathA);
					profiles[1] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPathB);
					TokenBlocking imtb = new TokenBlocking(profiles);

					 blocks = imtb.buildBlocks();					 
					//ExtendedQGramsBlocking method = new ExtendedQGramsBlocking(0.95, 3, profiles);
					//blocks = method.buildBlocks();
			//		QGramsBlocking imtb = new QGramsBlocking(3, profiles);
			//		blocks = imtb.buildBlocks();

					SizeBasedBlockPurging sbb= new SizeBasedBlockPurging();
					sbb.applyProcessing(blocks);
		}else	{
			profiles= new List[1];
			profiles[0] = (List<EntityProfile>) SerializationUtilities.loadSerializedObject(profilesPathA);
			TokenBlocking imtb = new TokenBlocking(profiles);

			 blocks = imtb.buildBlocks();			
			//ExtendedQGramsBlocking method = new ExtendedQGramsBlocking(0.95, 3, profiles);
			//QGramsBlocking imtb = new QGramsBlocking(6, profiles);
			//blocks = method.buildBlocks();			 
			AbstractEfficiencyMethod blockPurging = new ComparisonsBasedBlockPurging(1.005);
			blockPurging.applyProcessing(blocks);
		}
		//		String mainDirectory = "/home/guilherme/Transferências/";
		//	        String[] profilesPath = {   
		

		//		System.out.println(" database  1  "+ profiles[0].size()  +"  data 2 ->" + profiles[1].size());
		


		//System.out.println(" numero comparações --> "+ num_blocks);


		String[] profilesPath;
		if(profilesPathB!=null){
			 profilesPath=new String[2];
			 profilesPath[0]=profilesPathA;
			 profilesPath[1]=profilesPathB;
		}else{
			 profilesPath=new String[1];
			 profilesPath[0]=profilesPathA;
		}
		int num_blocks=0;

		//            System.out.println("\n\n\n\n\n======================= Supervised CEP =======================");

		Classifier[] classifiers = getSupervisedWepClassifiers();
		//            SupervisedCEP scep = new SupervisedCEP(classifiers.length, blocks, duplicatePairs);
		//            for (int j = 0; j < ITERATIONS; j++) {
		//                scep.applyProcessing(j, classifiers, ebc);
		//            }
		//            scep.printStatistics();
		//
		//		            System.out.println("\n\n\n\n\n======================= Supervised CNP =======================");
		//		            classifiers = getSupervisedCnpClassifiers();
		//		            SupervisedCNP scnp = new SupervisedCNP(classifiers.length, blocks, duplicatePairs);
		//		            for (int j = 0; j < 5; j++) {
		//		                scnp.applyProcessing(j, classifiers, ebc);
		////		                BlockStatistics blockStats = new BlockStatistics(blocks, new BilateralDuplicatePropagation(mainDirectory+ "/groundtruth"));
		////		     		   double teste[]=blockStats.applyProcessing();
		////		     		   System.out.println("------------" +teste[0] + "  "+ teste[1] + "  "+ teste[0]);
		//		            }
		//		            scnp.printStatistics();
		Random r=new Random();
		int n=r.nextInt(100);

		BufferedWriter writer1 = new BufferedWriter(new FileWriter(System.getProperty("user.home")+"/Dropbox/blocagem/saida50K_classificador1"+profilesPathA.split("/")[profilesPathA.split("/").length-1]));
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(System.getProperty("user.home")+"/Dropbox/blocagem/saida50K_classificador2"+profilesPathA.split("/")[profilesPathA.split("/").length-1]));
		BufferedWriter writer3 = new BufferedWriter(new FileWriter(System.getProperty("user.home")+"/Dropbox/blocagem/saida50K_classificador3"+profilesPathA.split("/")[profilesPathA.split("/").length-1]));
		BufferedWriter writer4 = new BufferedWriter(new FileWriter(System.getProperty("user.home")+"/Dropbox/blocagem/saida50K_classificador4"+profilesPathA.split("/")[profilesPathA.split("/").length-1]));

       
		ExecuteBlockComparisons ebc = new ExecuteBlockComparisons(profilesPath);
		classifiers = getSupervisedWepClassifiers();
		SupervisedWEP swep;

		//new EntityIndex(blocks).enumerateBlocks(blocks);;	
		File f=new File("/tmp/lock");
		f.delete();

		System.out.println("\n\n\n\n\n======================= Supervised WEP =======================");
		int i=1,j=5;
		//for (int i = 1; i <= 2;i++)
		{
			swep = new SupervisedWEP(classifiers.length, blocks, duplicatePairs,ebc);

			//blockHash.produceHash(blocks, ebc);

			int tamanho = 10;
			while(tamanho <=10000)
			{				

				writer1.write("level "+tamanho +"\n");
				writer2.write("level "+tamanho +"\n");
				writer3.write("level "+tamanho +"\n");
				writer4.write("level "+tamanho +"\n");

				for (j = 0;j< 10; j++) 
				{
					swep.applyProcessing(j, classifiers, ebc, tamanho, writer1,writer2,writer3,writer4,i,profilesPathA.split("/")[profilesPathA.split("/").length-1]);

					writer1.flush();
					writer2.flush();
					writer3.flush();
					writer4.flush();
				}
				swep.printStatistics();
				//swep.printStatisticsB(writer);
				System.out.println("size of level : "+ tamanho);

				if(tamanho==5)
					tamanho=10;
				else if(tamanho==10)
					tamanho=50;
				else if(tamanho==50)
					tamanho*=2;
				else if(tamanho==100)
					tamanho=500;
				
				else if( tamanho==500)
					tamanho=1000;
				else tamanho*=tamanho;

			}
		}
		writer1.close();
		writer2.close();
		writer3.close();
	}
	//  }
}
