package Utilities;

//import java.util.Comparator;
//
//public class ArrayComparator implements Comparator<Comparable[]> {
//    private final int columnToSort;
//    private final boolean ascending;
//
//    public ArrayComparator(int columnToSort, boolean ascending) {
//        this.columnToSort = columnToSort;
//        this.ascending = ascending;
//    }
//
//    public int compare(Comparable[] c1, Comparable[] c2) {
//       
//    	
//    	Double a=Double.parseDouble((String) c1[columnToSort]);
//    	Double b=Double.parseDouble((String) c2[columnToSort]);
//    	int cmp=0;
//    	if(a<b)
//    		cmp=1;
//    	else
//    		cmp=0;
//    	//int cmp = c1[columnToSort].compareTo(c2[columnToSort]);
//        return ascending ? cmp : -cmp;
//    }
//}

import java.util.Arrays;
import java.util.Comparator;

//public class ArrayComparator implements Comparable<Double[]> {

	
	
	public class ArrayComparator <T extends Comparable<T>> implements Comparator<T[]> {
		
		int column;
		public ArrayComparator (int n){
			column=n;
		}
		
	    @Override	    
	    public int compare(T[] arrayA, T[] arrayB) {
	        // get the second element of each array, andtransform it into a Double
	        Double d1 = Double.valueOf(arrayA[column].toString());
	        Double d2 = Double.valueOf(arrayB[column].toString());
	      //  System.out.println(arrayB[1].toString());
	        // since you want a descending order, you need to negate the 
	        // comparison of the double
	        return -d1.compareTo(d2);
	        // or : return d2.compareTo(d1);
	    }
	}	
//	
	
//	public class ArrayComparator<T extends Comparable<T>> implements Comparator<T[]> {
//	    @Override public int compare(T[] arrayA, T[] arrayB) {
//	        if(arrayA==arrayB) return 0; int compare;
//	        for(int index=0;index<arrayA.length;index++)
//	            if(index<arrayB.length) {
//	                if((compare=arrayA[index].compareTo(arrayB[index]))!=0)
//	                    return compare;
//	            } else return 1; //first array is longer
//	        if(arrayA.length==arrayB.length)
//	             return 0; //arrays are equal
//	        else return -1; //first array is shorter 
//	    }
//	}
