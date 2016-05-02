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

package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gap2
 */

public class FileUtilities {
    
    public static List<String> getFileLines(String filePath) throws Exception {
        final List<String> lines = new ArrayList<String>();

        final BufferedReader reader = new BufferedReader(new FileReader(filePath));
        for(String line; (line = reader.readLine()) != null; ) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }
    
//    public static void save_data_db(String idA, String idB, String recA, String recB, double d, String label, Connection con, PrintStream pstxt, PrintStream pstxt_level, PrintStream psarff_level) throws FileNotFoundException  {
//    	//System.out.println("salvando dados " +idA +" "+ idB);
////		PreparedStatement preparedStatement = null;
////		try	{
////			preparedStatement=con.prepareStatement("insert into   base_scholar_clear (idA,idB,recA,similarity,recB,label) values (?,?,?,?,?,?);");
////		}catch(SQLException ex){
////			ex.printStackTrace();
////		}
//		//while (buf.ready()){
//		//	String temp = buf.readLine();
//			
////			try{
////			//	String[] text_split=temp.split(";");
////				//System.out.println(text_split[0].split(":")[2]);
////				preparedStatement.setInt(1, Integer.parseInt(idA));
////				preparedStatement.setInt(2,Integer.parseInt(idB));
////				preparedStatement.setString(3,recA);
////				preparedStatement.setDouble(4, d);
////				preparedStatement.setString(5,recB);
////				preparedStatement.setString(6,label);
////				preparedStatement.execute();
////				
////			}catch(SQLException ex){
////				ex.printStackTrace();	
////			}
//	//	} 		
//		
//			
//			///save file
//			//System.out.println(recA +","+d+ ", " +recB+ ","+label);
//			pstxt.println(recA +","+d+ ", " +recB+ ","+label+ " ,"+idA);
//			pstxt_level.println(recA +","+d+ ", " +recB+ ","+label+ " ,"+idA);
//			pstxt_level.flush();
//			pstxt.flush();
//	}
}