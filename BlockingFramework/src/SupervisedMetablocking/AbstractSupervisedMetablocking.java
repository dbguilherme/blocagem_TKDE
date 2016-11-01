/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    Copyright (C) 2015 George Antony Papadakis (gpapadis@yahoo.gr)
 */

package SupervisedMetablocking;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Set;

import DataStructures.AbstractBlock;
import DataStructures.Comparison;
import DataStructures.EntityIndex;
import DataStructures.IdDuplicates;
import DataStructures.UnilateralBlock;
import Utilities.ComparisonIterator;
import Utilities.Constants;
import Utilities.Converter;
import Utilities.ExecuteBlockComparisons;
import Utilities.ProfileComparison;
import Utilities.StatisticsUtilities;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author gap2
 */

public abstract class AbstractSupervisedMetablocking implements Constants {

	protected final boolean dirtyER;
	protected int noOfAttributes;
	protected int noOfClassifiers;
	protected double noOfBlocks;
	protected double validComparisons;
	protected double[] comparisonsPerBlock;
	protected double[] nonRedundantCPE;
	protected double[] redundantCPE;
	//protected int[] Nblocks;

	protected Attribute classAttribute;
	protected ArrayList<Attribute> attributes;
	protected final EntityIndex entityIndex;
	protected Instances trainingInstances;
	// List<Class1> list = new ArrayList<Class1>();
	protected List<AbstractBlock> blocks;
	protected List<Double>[] overheadTimes;
	protected List<Double>[] resolutionTimes;
	protected List<Double> sampleMatches;
	protected List<Double> sampleNonMatches;
	protected List<Double> sampleNonMatchesNotUsed;
	protected List<Double>[] sampleComparisons;
	protected List<Double>[] sampleDuplicates;
	protected List<String> classLabels;
	protected final Set<IdDuplicates> duplicates;
	protected Set<Comparison> trainingSet;
	protected Set<IdDuplicates> detectedDuplicates;
	protected int totalPares=0;
	protected int elements[];
	protected Hashtable balance = new Hashtable();
	protected final String names[]=(new Converter()).atributos_valueA;
	int Nblocks[][];
	ExecuteBlockComparisons ebc;
	String set="";
	double th=0;

	public AbstractSupervisedMetablocking (int classifiers, List<AbstractBlock> bls, Set<IdDuplicates> duplicatePairs, ExecuteBlockComparisons ebc) {
		blocks = bls;
		dirtyER = blocks.get(0) instanceof UnilateralBlock;
		entityIndex = new EntityIndex(blocks);
		duplicates = duplicatePairs;
		noOfClassifiers = classifiers;
		this.ebc=ebc;
		getStatistics();
		prepareStatistics();
		getAttributes();
		Nblocks=conta_niveis_hash(blocks,ebc);
	}

	protected abstract void applyClassifier(Classifier classifier) throws Exception;
	protected abstract List<AbstractBlock> gatherComparisons();
	protected abstract void initializeDataStructures();
	protected abstract void processComparisons(int configurationId, int iteration, BufferedWriter writer, BufferedWriter writer2, BufferedWriter writer3,BufferedWriter writer4, double th2);
	protected abstract void savePairs(int i, ExecuteBlockComparisons ebc);
	protected abstract int getCount();


	public void applyProcessing(int iteration, Classifier[] classifiers, int tamanho, BufferedWriter writer1, BufferedWriter writer2, BufferedWriter writer3, BufferedWriter writer4, int r, String profilesPathA) throws Exception {
		elements=new int[10];

		set=profilesPathA;
		//getTrainingSet_original(iteration,ebc,tamanho,r,profilesPathA);

		getTrainingSet(iteration);
		System.out.println(trainingInstances.size() + "  ----- " +temp);

		for (int i = 0; i < classifiers.length; i++) {
			System.out.println("\n\nClassifier id\t:\t" + i);
			initializeDataStructures();


			long startingTime = System.currentTimeMillis();
			classifiers[i].buildClassifier(trainingInstances);
			applyClassifier(classifiers[i]);
			//	System.out.println("count ---> "+ getCount());
			double overheadTime = System.currentTimeMillis()-startingTime;
			System.out.println("CL"+i+" Overhead time\t:\t" + overheadTime);
			overheadTimes[i].add(overheadTime);
			//System.out.println("----------" +getCount());
			//commented out for faster experiments
			//use when measuring resolution time
			long comparisonsTime = 0;//ebc.comparisonExecution(newBlocks);
			System.out.println("CL"+i+" Classification time\t:\t" + (comparisonsTime+overheadTime));
			resolutionTimes[i].add(new Double(comparisonsTime+overheadTime));

			processComparisons(i, iteration, writer1, writer2,writer3, writer4,th);
			savePairs(i,ebc);
		}
	}

	protected boolean areMatching(Comparison comparison) {
		if (dirtyER) {
			final IdDuplicates duplicatePair1 = new IdDuplicates(comparison.getEntityId1(), comparison.getEntityId2());
			final IdDuplicates duplicatePair2 = new IdDuplicates(comparison.getEntityId2(), comparison.getEntityId1());
			return duplicates.contains(duplicatePair1) || duplicates.contains(duplicatePair2);
		}

		final IdDuplicates duplicatePair1 = new IdDuplicates(comparison.getEntityId1(), comparison.getEntityId2());
		return duplicates.contains(duplicatePair1);
	}

	private void getAttributes() {
		attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("ECBS"));
		attributes.add(new Attribute("RACCB"));
		attributes.add(new Attribute("JaccardSim"));
		attributes.add(new Attribute("NodeDegree1"));
		attributes.add(new Attribute("NodeDegree2"));
		attributes.add(new Attribute("teste1"));
		//	attributes.add(new Attribute("teste2"));
		//attributes.add(new Attribute("sim"));
		//	attributes.add(new Attribute("teste3"));

		classLabels = new ArrayList<String>();
		classLabels.add(NON_MATCH);
		classLabels.add(MATCH);

		classAttribute = new Attribute("class", classLabels);
		attributes.add(classAttribute);
		noOfAttributes = attributes.size();
	}

	private void getStatistics() {
		noOfBlocks = blocks.size();
		validComparisons = 0;
		int noOfEntities = entityIndex.getNoOfEntities();

		redundantCPE = new double[noOfEntities];
		nonRedundantCPE = new double[noOfEntities];
		comparisonsPerBlock = new double[(int)(blocks.size() + 1)];
		for (AbstractBlock block : blocks) {
			comparisonsPerBlock[block.getBlockIndex()] = block.getNoOfComparisons();

			ComparisonIterator iterator = block.getComparisonIterator();
			while (iterator.hasNext()) {
				Comparison comparison = iterator.next();

				int entityId2 = comparison.getEntityId2()+entityIndex.getDatasetLimit();
				redundantCPE[comparison.getEntityId1()]++;
				redundantCPE[entityId2]++;

				if (!entityIndex.isRepeated(block.getBlockIndex(), comparison)) {
					validComparisons++;
					nonRedundantCPE[comparison.getEntityId1()]++;
					nonRedundantCPE[entityId2]++;
				}
			}
		}
	}

	protected Instance getFeatures(int match, List<Integer> commonBlockIndices, Comparison comparison, double flag) {
		double[] instanceValues =null;
		instanceValues = new double[noOfAttributes];

		int entityId2 = comparison.getEntityId2() + entityIndex.getDatasetLimit();
		//	System.out.println(noOfBlocks +"   "+ entityIndex.getNoOfEntityBlocks(comparison.getEntityId1(), 0));
		double ibf1 = Math.log(noOfBlocks/entityIndex.getNoOfEntityBlocks(comparison.getEntityId1(), 0));
		double ibf2 = Math.log(noOfBlocks/entityIndex.getNoOfEntityBlocks(comparison.getEntityId2(), 1));
		try{
			instanceValues[0] = commonBlockIndices.size()*ibf1*ibf2;	
		}catch (Exception e ){
			System.out.println(e.getMessage());
		}
//		if(flag==1.0){
//			if(instanceValues[0]<50)
//				return null;
//		}
		//CF 	IBF -RACCB 	Jaccard	Sim	,Node Degree

		double raccb = 0;
		for (Integer index : commonBlockIndices) {
			raccb += 1.0 / comparisonsPerBlock[index];
		}
		if (raccb < 1.0E-6) {
			raccb = 0.0000006;
		}

		instanceValues[1] = raccb;
		
		//	ProfileComparison.getJaccardSimilarity(profiles1[comparison.getEntityId1()].getAttributes(), profiles2[comparison.getEntityId2()].getAttributes());
		instanceValues[2] = commonBlockIndices.size() / (redundantCPE[comparison.getEntityId1()] + redundantCPE[entityId2] - commonBlockIndices.size());
		instanceValues[3] = nonRedundantCPE[comparison.getEntityId1()];
		instanceValues[4] = nonRedundantCPE[entityId2];
		instanceValues[5] = entityIndex.getNoOfEntityBlocks(comparison.getEntityId1(), 0);
		//instanceValues[6] = entityIndex.getNoOfEntityBlocks(comparison.getEntityId2(), 1);
		//		
	
		//instanceValues[5] =ProfileComparison.getJaccardSimilarity(ebcX.exportEntityA(comparison.getEntityId1()), ebcX.exportEntityB(comparison.getEntityId2()));
		//if(instanceValues[0]>200)
		instanceValues[5] =ebc.getSimilarityAttribute(comparison.getEntityId1(), comparison.getEntityId2());
		
		//else
		//	instanceValues[5] =0.0;
		instanceValues[6] = match;
		//instanceValues.
		//ebcX.getSimilarityAttribute(comparison.getEntityId1(), comparison.getEntityId2());  //
		//ebcX.getSImilarityAttribute(comparison.getEntityId1(),comparison.getEntityId2(),names);


		//instanceValues[6] = match;

		Instance newInstance = new DenseInstance(1.0, instanceValues);
		newInstance.setDataset(trainingInstances);
		return newInstance;
	}



	int temp=0;
	protected void getTrainingSet_original(int iteration, ExecuteBlockComparisons ebc, int tamanho, int r, String profilesPathA) throws FileNotFoundException {

		sampleMatches.clear();
		sampleNonMatches.clear();
		sampleNonMatchesNotUsed.clear();
		int trueMetadata=0;
		int matchingInstances = (int) (SAMPLE_SIZE*duplicates.size());
		double nonMatchRatio = matchingInstances / (validComparisons - duplicates.size());
		System.out.println("nonMatchRatio --> " + nonMatchRatio  + " duplicates.size() "+ duplicates.size() + " validComparisons " + validComparisons);
		trainingSet = new HashSet<Comparison>(4*matchingInstances);
		trainingInstances = new Instances("trainingSet", attributes, 2*matchingInstances);
		trainingInstances.setClassIndex(noOfAttributes - 1);
		//double  vector[]={0,0,0,0,0};
		Random random= new Random(iteration);
		PrintStream pstxt = null;
		PrintStream psarff = null;

		if(true){
			//encontraPares();

			try {
				pstxt = new PrintStream(new FileOutputStream(new File("/tmp/levels_arff"+profilesPathA+".txt"),false));
				//pstxt = new PrintStream(new FileOutputStream(new File("/tmp/final_treina.txt"),false));
				psarff = new PrintStream(new FileOutputStream(new File("/tmp/levels_arff"+profilesPathA+".arff"),false));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			System.out.println("linha 251");
			psarff.println("@relation whatever");
			for (int i = 0; i < trainingInstances.numAttributes()-1 ; i++) {
				psarff.println("@attribute "+i+" numeric");			
			}		
			psarff.println("@attribute classe {0,1}");
			psarff.println("@data");
			//Vector<Comparison> randomInstances= new Vector<Comparison>(4*matchingInstances);;
			Comparison comparison;

			System.out.println("linha 260");

			//							Collections.sort(blocks, new Comparator<AbstractBlock>() {
			//								public int compare(AbstractBlock c1, AbstractBlock c2) {
			//									if (c1.getNoOfComparisons() < c2.getNoOfComparisons()) return -1;
			//									if (c1.getNoOfComparisons() > c2.getNoOfComparisons()) return 1;
			//									return 0;
			//								}});
			//Collections.shuffle(blocks);

			//
			long startingTime = System.currentTimeMillis();

			long deltaTime= System.currentTimeMillis()-startingTime;

			System.out.println("time da contagem "+ deltaTime);

			int controle=-1;
			PrintStream pstxt_level[] = new PrintStream[10];
			PrintStream psarff_level[]= new PrintStream[10];
			//HashMap<Integer, ArrayList<DataStructures.Comparison>> deep= blockHash.deep;

			//int valores[]=new int[tamanho];
			for (int i = 0; i < 10; i++) {
				try {
					pstxt_level[i] = new PrintStream(new FileOutputStream(new File("/tmp/levels_arff_level"+i+profilesPathA+".txt"),false));
					psarff_level[i] = new PrintStream(new FileOutputStream(new File("/tmp/levels_arff_level"+i+profilesPathA+".arff"),false));
					psarff_level[i].println("@relation whatever");
					for (int k = 0; k < trainingInstances.numAttributes()-1 ; k++) {					
						psarff_level[i].println("@attribute "+k+" numeric");			
					}		
					psarff_level[i].println("@attribute classe {0,1}");
					psarff_level[i].println("@data");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

			}



			int level=0;
			Double valor=0.0;
			//System.out.println("primeiroBlock[controle] -->> " + primeiroBlock[controle]);
			for (int i=0;i<blocks.size();i++) {
				ComparisonIterator iterator = blocks.get(i).getComparisonIterator();

				while (iterator.hasNext()) {
					comparison = iterator.next();

					final List<Integer> commonBlockIndices = entityIndex.getCommonBlockIndices(blocks.get(i).getBlockIndex(), comparison);
					if (commonBlockIndices == null) {
						continue;
					}

					double ibf1 = Math.log(noOfBlocks/entityIndex.getNoOfEntityBlocks(comparison.getEntityId1(), 0));
					double ibf2 = Math.log(noOfBlocks/entityIndex.getNoOfEntityBlocks(comparison.getEntityId2(), 1));
					try{
						valor = commonBlockIndices.size()*ibf1*ibf2;	
					}catch (Exception e ){
						System.out.println(e.getMessage());
					}
						if(valor>1000)
						level=35;
					else
						level=(int) Math.floor(valor/30);
					{
						//	if(comparison.sim>= ((double)level*0.1) && comparison.sim< ((double)(level+1)*0.1))
						{	
							//if(level>1 && level< 5)
							//	continue;
							int temp=random.nextInt(Nblocks[level][0]);


							if(temp> tamanho)
								continue;




//																										int match = NON_DUPLICATE; // false
//																										if (areMatching(comparison)) {
//																											if (random.nextDouble() < SAMPLE_SIZE) {
//																												//trueMetadata++;
//																												match = DUPLICATE; // true
//																											} else {
//																												continue;
//																											}
//																										} else if (nonMatchRatio <= random.nextDouble()) {
//																											continue;
//																										}

							//								if(controle==4)
							//									System.out.println("descarte " + temp +"  "+ Nblocks[controle]);
							if((getLevels(comparison,ebc,blocks.get(i).getBlockIndex(),pstxt,psarff,pstxt_level,psarff_level, nonMatchRatio, tamanho,level,blocks.get(i).getNoOfComparisons()))<=0){

							}
							//pstxt_level[level].flush();
							//psarff_level[level].flush();
							//	
						}
					}
				}
			}

			pstxt.close();
			psarff.close();
			for (int m = 0; m < 10; m++) {
				pstxt_level[m].close();
				psarff_level[m].close();
			}

			try {
				File f = new File("/tmp/lock");
				while(f.exists() ) { 
					System.out.println("sleeping................");
					Thread.sleep(1000);
				}
				f.createNewFile();

				callGeraBins();
			//	for (int i = 8; i >=8; i--)
				{
					//System.out.println("chamando allac " + i	);
					//	DiscretizeTest.run_short("/tmp/levels_arff_level"+i+".arff", "/tmp/levels_arff_level"+i+"D.arff");			
					//	DiscretizeTest.run("/tmp/levels_arff_level"+i+".arff", "/tmp/levels_arff_level"+i+"D.arff");			
					callAllac(8,r);  
				}
				//teste_tree(trainingInstances);
				//loadFileTrainingSet(trainingInstances);
				loadFileTrainingSet();
				f.delete();
			}  catch (Exception e) {
				e.printStackTrace();
			}
			System.err.println(" ");
			System.out.println("trainingSet.size() - trueMetadata)--->" + (trainingSet.size() - trueMetadata)  + "   ----------->> " + trueMetadata);
			//sampleMatches.add((double) trueMetadata);///positivos
			//	sampleNonMatches.add((double) (trainingSet.size() - trueMetadata)); //negativos
		}

	}

	 protected void getTrainingSet(int iteration) {
	        int trueMetadata = 0;
	        Random random = new Random(iteration);
	        int matchingInstances = (int) (SAMPLE_SIZE*duplicates.size()+1);
	        double nonMatchRatio = matchingInstances / (validComparisons - duplicates.size());

	        trainingSet = new HashSet<Comparison>(4*matchingInstances);
	        trainingInstances = new Instances("trainingSet", attributes, 2*matchingInstances);
	        trainingInstances.setClassIndex(noOfAttributes - 1);

	        for (AbstractBlock block : blocks) {
	            ComparisonIterator iterator = block.getComparisonIterator();
	            while (iterator.hasNext()) {
	                Comparison comparison = iterator.next();
	                final List<Integer> commonBlockIndices = entityIndex.getCommonBlockIndices(block.getBlockIndex(), comparison);
	                if (commonBlockIndices == null) {
	                    continue;
	                }

	                int match = NON_DUPLICATE; // false
	                if (areMatching(comparison)) {
	                    if (random.nextDouble() < SAMPLE_SIZE) {
	                        trueMetadata++;
	                        match = DUPLICATE; // true
	                    } else {
	                        continue;
	                    }
	                } else if (nonMatchRatio <= random.nextDouble()) {
	                    continue;
	                }

	                trainingSet.add(comparison);
	                Instance newInstance = getFeatures(match, commonBlockIndices, comparison, nonMatchRatio);
	                trainingInstances.add(newInstance);
	            }
	        }
System.out.println("  trainingSet.size() - trueMetadata)  " + (trainingSet.size() - trueMetadata) + "   ---"+ trueMetadata );
	        sampleMatches.add((double) trueMetadata);
	        sampleNonMatches.add((double) (trainingSet.size() - trueMetadata));
	        sampleNonMatchesNotUsed.add(0.0);
	    }

	private void loadFileTrainingSet() throws Exception {
		// TODO Auto-generated method stub
		BufferedReader alac_result = new BufferedReader(new FileReader("/tmp/final_treina.arff"));
		//BufferedReader alac_result = new BufferedReader(new FileReader("/tmp/levels_arffdataset1_imdb.arff"));
		Instances data = new Instances(alac_result);
		data.setClassIndex(data.numAttributes() -1);
		int countP=0,countN=0, countDesc=0;
		double positivos=0.0, negativos=0.0;
		int histograma[][]=new int[11][2];
		double lposit=1.0;
		for (Instance instance : data) {
			if((instance.value(data.numAttributes() -1))==1){
				positivos+=instance.value(data.numAttributes() -2);
				//histograma[(int) Math.floor(instance.value(data.numAttributes() -2)*10)][0]++;
				//System.out.println(instance.value(data.numAttributes()-2)+ " P");
				if(lposit>instance.value(data.numAttributes() -2)){
					//System.out.println(instance.value(data.numAttributes() -2));
					lposit=instance.value(data.numAttributes() -2);
					
				}
			}
			else{
				negativos+=instance.value(data.numAttributes() -2);
				//histograma[(int) Math.floor(instance.value(data.numAttributes() -2)*10)][1]++;
				//System.out.println(instance.value(data.numAttributes()-2)  +"   "+ (int) Math.floor(instance.value(data.numAttributes() -2)*10));
				//System.out.println(instance.value(data.numAttributes()-2)+ " N");
			}
			
			if((instance.value(data.numAttributes() -1))==1.0)  
				countP++;
			else
				countN++;
			
		}
//		for (int i = 0; i < histograma.length; i++) {
//			System.out.println("hist "+ histograma[i][0] +"  "+ histograma[i][1]);
//		}
		
	//	double limiar =Math.floor(menorP*10);
		//System.out.println("positivos --> " +lposit);//Math.ceil(a / 100.0)
		th=Math.ceil((negativos/countN)*10)/10;
		System.out.println(" media " + th);
//		if(set.contains("dblp"))
		//	th-=0.1;
		//if(set.contains("dblp"))
		//	th=0.2;
		for (Instance instance : data) {
			if((instance.value(data.numAttributes() -1)==0.0) && (instance.value(instance.numAttributes()-2))>= th)
			{				
				countDesc++;
				System.out.println("descartando.........." + instance.value(instance.numAttributes()-2));
				continue;
			}		
			//if((instance.value(data.numAttributes() -1)==0.0))
			//	System.out.println(instance.value(instance.numAttributes()-2)  + "  "+ ebcX.temp_limiar);
			trainingInstances.add(instance);
			
		}
		
		System.out.println("valores  --> Positio -> " +countP  +"  negativos -> "+(countN+countDesc) + "   countDesc -->"+countDesc);
		sampleMatches.add((double) countP);///positivos
		sampleNonMatches.add((double) (countN)); //negativos
		sampleNonMatchesNotUsed.add((double) (countDesc)); //negativos
	}


	private int[][] conta_niveis_hash(List<AbstractBlock> blocks, ExecuteBlockComparisons ebc) {

		int[][] blockSize=  new int[100][3];
		for (int i = 0; i < 100; i++) {
			blockSize[i][0]=0;
			blockSize[i][1]=0;
			blockSize[i][2]=0;
			//	primeiroBlock[i]=0;
		}
		double sim=0.0;
		
		
		for ( AbstractBlock b:blocks) {


			ComparisonIterator iterator = b.getComparisonIterator();
			Comparison c;
			while(iterator.hasNext()){			
				c= iterator.next(); 

				final List<Integer> commonBlockIndices = entityIndex.getCommonBlockIndices(b.getBlockIndex(), c);
				if (commonBlockIndices == null) {
					continue;
				}
				
				double ibf1 = Math.log(noOfBlocks/entityIndex.getNoOfEntityBlocks(c.getEntityId1(), 0));
				double ibf2 = Math.log(noOfBlocks/entityIndex.getNoOfEntityBlocks(c.getEntityId2(), 1));
				try{
					sim = commonBlockIndices.size()*ibf1*ibf2;	
				}catch (Exception e ){
					System.out.println(e.getMessage());
				}
				int level=0;
				if(sim>1000){
					level=((int)Math.floor(35));
				}else
					level=((int)Math.floor(sim/30));
				
				blockSize[level][0]++;
				if(areMatching(c))
					blockSize[level][2]++;
				else
					blockSize[level][1]++;
//				else
//					blockSize[((int)Math.floor(sim/30))][0]++;
				
				
				////////////////////////
				String label="false";
				IdDuplicates duplicatePair1 = new IdDuplicates(c.getEntityId1(), c.getEntityId2());
				if (duplicates.contains(duplicatePair1)) {
					label="true";
					//System.out.println("duplicate pair " + concatStringA + "   "+ concatStringB);
				}
				
				Set<DataStructures.Attribute> setAtributtes = ebc.exportEntityA(c.getEntityId1());
				String sA[]=Converter.createVector(setAtributtes,c.getEntityId1(),Converter.atributos_valueA);
				String concatStringA = "::";////title,

				
				for (int j = 1; j < sA.length; j++) {
					try{
						//System.err.print(sA[j]+ "  ");
						if(!sA[j].isEmpty())
							sA[j]=sA[j].replace(",", " ").replace(":", " ").replace("\n","");
						concatStringA=concatStringA.concat(sA[j]+":");				

					}catch(Exception e){
						concatStringA=concatStringA.concat(": XXX :");	
					}
				}
				
				setAtributtes = ebc.exportEntityB(c.getEntityId2());
				String sB[]=Converter.createVector(setAtributtes,c.getEntityId2(),Converter.atributos_valueB);
				//    System.out.print( "  ---- ");
				String concatStringB = "::";
				for (int j = 1; j < sB.length; j++) {
					try{
						//System.err.print(sB[j]+ "  ");
						if(!sB[j].isEmpty())
							sB[j]=sB[j].replace(",", " ").replace(":", " ").replace("\n","");
						concatStringB=concatStringB.concat(sB[j]+":");
					}catch (Exception e ){
						concatStringB=concatStringB.concat(": XXX :");
					}
				}
				//if(label=="true")
				//if(ebc.getSimilarityAttribute(concatStringA, concatStringB)>0.2)

				if(ebc.getSimilarityAttribute(concatStringA, concatStringB)>0.5)

				{
					System.out.print(concatStringA + " --- " + concatStringB);				
					System.out.print( "  "+ label +" "+ ebc.getSimilarityAttribute(concatStringA, concatStringB)   +"\n" );
				}
			}
		}
		for (int i = 0; i < 100; i++) {
			if(blockSize[i][0] !=0)
				//	perc[i]=(((double)tamanho)/(blockSize[i]));
				System.out.println(i + " tamanho do bloco "+  "  " + blockSize[i][0] + " " +  blockSize[i][1]  +"  "+ blockSize[i][2]);
			//totalPares += blockHash.blockSize[i];
		}
		return blockSize;



	}




	private void callGeraBins() throws IOException {
		String line;
		String cmd;
		String userHome = System.getProperty("user.home");
		String file ="/tmp/levels_arff"+set+ " /tmp/teste";
		int att=noOfAttributes;
		Process proc = null;		
		BufferedReader read, buf;
		//	new CriaMatrixWeka(common).criaArffActiveLearning("/tmp/levels.txt",2);
		cmd = "cd  "+ userHome+ "/Downloads/SSARP/Dedup/test5/; bash ./gera_beans.sh  " +file + "   "+ att + " "+ att;
		proc = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", cmd});

		read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		try {
			proc.waitFor();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		while ((line=buf.readLine())!=null) {
			System.out.println(line);
		}
		while (read.ready()) {
			System.out.println(read.readLine());
		}
	}

	int elemento=0;



	private void callAllac(int i, int r) throws IOException {
		////Common common=new Common();
		//common.setSortFile("/");
		//common.setElementos("3,4,5,6");
		String line;
		String cmd;
		String userHome = System.getProperty("user.home");
		//String file ="/tmp/levels_arff_level"+i+"" + " /tmp/teste";
		String file ="/tmp/levels_arff" +set + " /tmp/teste";
		System.out.println(" ----" + file + " ----");
		int att=noOfAttributes;
		Process proc = null;		
		BufferedReader read, buf;
		cmd = "cd  "+ userHome+ "/Downloads/SSARP/Dedup/test5/; bash ./SSARP2.sh  " +file + " "+ i +" " +att + "  "+ r;
		proc = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", cmd});
		System.out.println("CMD 1= " +cmd);

		read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		try {
			proc.waitFor();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		while ((line=buf.readLine())!=null) {
			System.out.println(line);
		}
		while (read.ready()) {
			System.out.println(read.readLine());
		}
		System.out.println("finaliza processo");

	}

	//	private int original(Random random, Comparison comparison, int trueMetadata, List<Integer> commonBlockIndices, int trueMetadata1, double nonMatchRatio){
	//		temp++;
	//		int match = NON_DUPLICATE; // false
	//		if (areMatching(comparison)) {
	//			if (random.nextDouble() < SAMPLE_SIZE) {
	//				trueMetadata1++;
	//				match = DUPLICATE; // true
	//			} else {
	//				return trueMetadata1;
	//			}
	//		} else if (nonMatchRatio <= random.nextDouble()) {
	//			return trueMetadata1;
	//		}
	//
	//		trainingSet.add(comparison);
	//		Instance newInstance = getFeatures(match, commonBlockIndices, comparison,0.0);
	//		trainingInstances.add(newInstance);
	//		return trueMetadata1;
	//	}


	private int getLevels(Comparison comparison, ExecuteBlockComparisons ebc, int i, PrintStream pstxt, PrintStream psarff, PrintStream[] pstxt_level, PrintStream[] psarff_level, double nonMatchRatio, double tamanho, int controle, double d) throws FileNotFoundException {
		String concatStringA;
		String concatStringB;
		Double sim=comparison.sim;


		Set<DataStructures.Attribute> setAtributtes = ebc.exportEntityA(comparison.getEntityId1());
		String sA[]=Converter.createVector(setAtributtes,comparison.getEntityId1(),Converter.atributos_valueA);
		concatStringA=sA[0]+"::";////title,

		if(sA.length==0)
			return 1;
		for (int j = 0; j < sA.length; j++) {
			try{
				//System.err.print(sA[j]+ "  ");
				if(!sA[j].isEmpty())
					sA[j]=sA[j].replace(",", " ").replace(":", " ").replace("\n","");
				concatStringA=concatStringA.concat(sA[j]+":");				

			}catch(Exception e){
				concatStringA=concatStringA.concat(":  :");	
			}
		}
		System.out.println(concatStringA);
		setAtributtes = ebc.exportEntityB(comparison.getEntityId2());
		String sB[]=Converter.createVector(setAtributtes,comparison.getEntityId2(),Converter.atributos_valueB);
		//    System.out.print( "  ---- ");
		concatStringB=sB[0]+"::";
		for (int j = 0; j < sB.length; j++) {
			try{
				//System.err.print(sB[j]+ "  ");
				if(!sB[j].isEmpty())
					sB[j]=sB[j].replace(",", " ").replace(":", " ").replace("\n","");
				concatStringB=concatStringB.concat(sB[j]+":");
			}catch (Exception e ){
				concatStringB=concatStringB.concat(": :");
			}
		}
		System.out.println(concatStringB);
		String label="false";
		IdDuplicates duplicatePair1 = new IdDuplicates(comparison.getEntityId1(), comparison.getEntityId2());
		if (duplicates.contains(duplicatePair1)) {
			label="true";
			//System.out.println("duplicate pair " + concatStringA + "   "+ concatStringB);
		}
		//////////////
		//System.out.println(comparison.sim);


		//
		//double similarity = ProfileComparison.getJaccardSimilarity(ebc.exportEntityA(comparison.getEntityId1()), ebc.exportEntityB(comparison.getEntityId2()));
		final List<Integer> commonBlockIndices = entityIndex.getCommonBlockIndices(i, comparison);
		Instance newInstanceTemp = getFeatures(label.contains("true")?1:0, commonBlockIndices, comparison,0.0);

//		if(controle>4)
//			return 0;

		DecimalFormat decimalFormatter = new DecimalFormat("############.#####");
		String temp;

		for (int j = 0; j < newInstanceTemp.numAttributes()-1; j++) {

			temp = decimalFormatter.format(newInstanceTemp.value(j));
			temp=temp.replace(",", ".");
			//System.out.println(temp);
			psarff.print(temp + ", ");
			//psarff_level[controle].print(temp + ", ");
		}
		//psarff.print(d+", ");
		//psarff_level[controle].print(d +", ");
		psarff.println(label.contains("true")?1:0);
		//psarff_level[controle].println(label.contains("true")?1:0);
		///////////    	
		//FileUtilities.save_data_db( String.valueOf(i), sB[0],concatStringA,concatStringB, sim,label,null,pstxt,pstxt_level, psarff_level,k  );
		pstxt.println(concatStringA +","+sim+ ", " +concatStringB+ ","+label+ " ,"+String.valueOf(i));
		//				pstxt_level[controle].println(concatStringA +","+sim+ ", " +concatStringB+ ","+label+ " ,"+String.valueOf(i));
		//				pstxt_level[controle].flush();
		pstxt.flush();
		//	if(ele)
		//	elements[controle]++;
		//System.out.println("controle " + controle + " --> element "+elements[controle]);
		return -1;
	}


	//	   public static double getJaccardSimilarity(int[] tokens1, int[] tokens2) {
	//	        double commonTokens = 0.0;
	//	        int noOfTokens1 = tokens1.length;
	//	        int noOfTokens2 = tokens2.length;
	//	        for (int i = 0; i < noOfTokens1; i++) {
	//	            for (int j = 0; j < noOfTokens2; j++) {
	//	                if (tokens2[j] < tokens1[i]) {
	//	                    continue;
	//	                }
	//
	//	                if (tokens1[i] < tokens2[j]) {
	//	                    break;
	//	                }
	//
	//	                if (tokens1[i] == tokens2[j]) {
	//	                    commonTokens++;
	//	                }
	//	            }
	//	        }
	//	        return commonTokens / (noOfTokens1 + noOfTokens2 - commonTokens);
	//	    }


	private void prepareStatistics() {
		sampleMatches = new ArrayList<Double>();
		sampleNonMatches = new ArrayList<Double>();
		sampleNonMatchesNotUsed= new ArrayList<Double>();
		overheadTimes = new ArrayList[noOfClassifiers];
		resolutionTimes = new ArrayList[noOfClassifiers];
		sampleComparisons = new ArrayList[noOfClassifiers];
		sampleDuplicates = new ArrayList[noOfClassifiers];
		for (int i = 0; i < noOfClassifiers; i++) {
			overheadTimes[i] = new ArrayList<Double>();
			resolutionTimes[i] = new ArrayList<Double>();
			sampleComparisons[i] = new ArrayList<Double>();
			sampleDuplicates[i] = new ArrayList<Double>();
		}
	}


	public void printStatistics() throws IOException {
		System.out.println("\n\n\n\n\n+++++++++++++++++++++++Printing overall statistics+++++++++++++++++++++++");

		double avSMatches = StatisticsUtilities.getMeanValue(sampleMatches);
		double avSNonMatches = StatisticsUtilities.getMeanValue(sampleNonMatches);
		System.out.println("Sample matches\t:\t" + avSMatches + "+-" + StatisticsUtilities.getStandardDeviation(avSMatches, sampleMatches));
		System.out.println("Sample non-matches\t:\t" + avSNonMatches + "+-" + StatisticsUtilities.getStandardDeviation(avSNonMatches, sampleNonMatches));

		for (int i = 0; i < overheadTimes.length; i++) {
			System.out.println("\n\n\n\n\nClassifier id\t:\t" + (i));

			double avOTime = StatisticsUtilities.getMeanValue(overheadTimes[i]);
			double avRTime = StatisticsUtilities.getMeanValue(resolutionTimes[i]);
			double avSEComparisons = StatisticsUtilities.getMeanValue(sampleComparisons[i]);
			double avSDuplicates = StatisticsUtilities.getMeanValue(sampleDuplicates[i]);



			final List<Double> pcs = new ArrayList<Double>();
			for (int j = 0; j < sampleMatches.size(); j++) {
				//pcs.add(sampleDuplicates[i].get(j)/(duplicates.size() - sampleMatches.get(j))*100.0);
				System.out.println(sampleDuplicates[i].get(j) + "   "+  (duplicates.size()  +"  "+ sampleMatches.get(j)));
				pcs.add((sampleDuplicates[i].get(j))/(duplicates.size())*100.0);
			}
			double avSPC = StatisticsUtilities.getMeanValue(pcs);

			System.out.println("Overhead time\t:\t" + avOTime + "+-" + StatisticsUtilities.getStandardDeviation(avOTime, overheadTimes[i]));
			System.out.println("Resolution time\t:\t" + avRTime + "+-" + StatisticsUtilities.getStandardDeviation(avRTime, resolutionTimes[i]));
			System.out.println("Sample duplicates\t:\t" + avSDuplicates + "+-" + StatisticsUtilities.getStandardDeviation(avSDuplicates, sampleDuplicates[i]));
			System.out.println("Sample PC\t:\t" + avSPC  + "+-  " + + StatisticsUtilities.getStandardDeviation(avSPC  , pcs));
			System.out.println("Sample comparisons\t:\t " + avSEComparisons + "+- " + StatisticsUtilities.getStandardDeviation(avSEComparisons, sampleComparisons[i]));

			//			try {
			//				writer.write(" " +avOTime+ " "+ StatisticsUtilities.getStandardDeviation(avOTime, overheadTimes[i]));
			//				writer.write(" " +avRTime + " " + StatisticsUtilities.getStandardDeviation(avRTime, resolutionTimes[i]));
			//				writer.write(" " +avSDuplicates + " " + StatisticsUtilities.getStandardDeviation(avSDuplicates, sampleDuplicates[i]));
			//				writer.write(" " +avSPC  + "  " + + StatisticsUtilities.getStandardDeviation(avSPC  , pcs));
			//				writer.write(" " +avSEComparisons + " " + StatisticsUtilities.getStandardDeviation(avSEComparisons, sampleComparisons[i]));
			//			} catch (IOException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}


		}
	}
}