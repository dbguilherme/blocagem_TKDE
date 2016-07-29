package Utilities;

import java.io.*;

import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;

/**
 * Shows how to generate compatible train/test sets using the Discretize
 * filter.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DiscretizeTest {

  /**
   * loads the given ARFF file and sets the class attribute as the last
   * attribute.
   *
   * @param filename    the file to load
   * @throws Exception  if somethings goes wrong
   */
  static double values[];
  protected static Instances load(String filename) throws Exception {
    Instances       result;
    BufferedReader  reader;

    reader = new BufferedReader(new FileReader(filename));
    result = new Instances(reader);
           
    result.setClassIndex(result.numAttributes() - 1);
        
    for (int i = 0; i < result.size(); i++) {
    	//System.out.println(result.get(i).value(1));
    	for (int j = 0; j < result.get(i).numValues(); j++) {
    		if(result.get(i).value(j)>values[j])
    			values[j]=result.get(i).value(j);
		}
    }    
    
    for (int i = 0; i < values.length; i++) {
		System.out.println(values[i]);
	}
    
    for (int i = 0; i < result.size(); i++) {
    //	System.out.println(result.get(i).value(1));
    	for (int j = 0; j < result.get(i).numValues()-1; j++) {
    		
         // System.out.println(values[j] + " " + result.get(i).value(j) + "   " +result.get(i).value(j)/values[j]);
          result.get(i).setValue(j, result.get(i).value(j)/values[j]);
		}
    }
    reader.close();

    return result;
  }

  /**
   * saves the data to the specified file
   *
   * @param data        the data to save to a file
   * @param filename    the file to save the data to
   * @throws Exception  if something goes wrong
   */
  protected static void save(Instances data, String filename) throws Exception {
    BufferedWriter  writer;

    
    
    writer = new BufferedWriter(new FileWriter(filename));
    writer.write(data.toString().replace(",", ", "));
    writer.newLine();
    writer.flush();
    writer.close();
  }

  /**
   * Takes four arguments:
   * <ol>
   *   <li>input train file</li>
   *   <li>input test file</li>
   *   <li>output train file</li>
   *   <li>output test file</li>
   * </ol>
   *
   * @param args        the commandline arguments
   * @throws Exception  if something goes wrong
   */
  public static void run (String infile,String outFile) throws Exception {
    Instances     inputTrain;
    Instances     outputTrain;
    Discretize    filter;

    values=new double[10];
    for (int i = 0; i < values.length; i++) {
    	values[i]=0;
	}
    
    
    // load data (class attribute is assumed to be last attribute)
    inputTrain = load(infile);
   

    // setup filter
    filter = new Discretize();
    filter.setInputFormat(inputTrain);

    // apply filter
  //  outputTrain = Filter.useFilter(inputTrain, filter);
   

    // save output
     save(inputTrain, outFile);
  }
  public static void run_short (String infile,String outFile) throws Exception {
	    Instances     inputTrain;
	    Instances     outputTrain;
	   

	  
	    
	    
	    inputTrain = apllyNormalization(infile);
	    // setup filter
	   // filter = new Discretize();
	   // filter.setInputFormat(inputTrain);

	    // apply filter
	  //  outputTrain = Filter.useFilter(inputTrain, filter);
	   

	    // save output
	     save(inputTrain, outFile);
	  }
  
  protected static Instances apllyNormalization(String filename) throws Exception {
	    Instances       result;
	    BufferedReader  reader;

	    reader = new BufferedReader(new FileReader(filename));
	    result = new Instances(reader);
	           
	    result.setClassIndex(result.numAttributes() - 1);
	        
//	    for (int i = 0; i < result.size(); i++) {
//	    	//System.out.println(result.get(i).value(1));
//	    	for (int j = 0; j < result.get(i).numValues(); j++) {
//	    		if(result.get(i).value(j)>values[j])
//	    			values[j]=result.get(i).value(j);
//			}
//	    }    
//	    
//	    for (int i = 0; i < values.length; i++) {
//			System.out.println(values[i]);
//		}
	    
	    for (int i = 0; i < result.size(); i++) {
	    //	System.out.println(result.get(i).value(1));
	    	for (int j = 0; j < result.get(i).numValues()-1; j++) {
	    		
	         // System.out.println(values[j] + " " + result.get(i).value(j) + "   " +result.get(i).value(j)/values[j]);
	          result.get(i).setValue(j, result.get(i).value(j)/values[j]);
			}
	    }
	    reader.close();

	    return result;
	  }



}
