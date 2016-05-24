/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import DataStructures.Attribute;
import DataStructures.EntityIndex;
import DataStructures.EntityProfile;
import DataStructures.IdDuplicates;
import DataStructures.UnilateralBlock;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * @author guilherme
 */
public class lixo {
    
    
    
     public static void main(String[] args) throws IOException, ClassNotFoundException {
        
         List<EntityProfile> profiles3 = (ArrayList<EntityProfile>) SerializationUtilities.loadSerializedObject("/home/vmguilherme/Downloads/dataset1");
         List<EntityProfile> profiles = (ArrayList<EntityProfile>) SerializationUtilities.loadSerializedObject("/home/vmguilherme/Downloads/dataset2");
        Set<IdDuplicates> duplicates = (Set<IdDuplicates>) SerializationUtilities.loadSerializedObject("/home/vmguilherme/Downloads//groundtruth");
     //    PrintWriter writer = new PrintWriter("/tmp/scholar", "UTF-8");
     // 
//         InputStream is = ...;
//         OutputStream os = ...;
//
//         byte buffer[] = new byte[1024];
//         int read;
//         while((read = is.read(buffer)) != -1){
//             os.write(buffer, 0, read);
//         }
         
         
        int count =0, x=0;
        for(IdDuplicates id: duplicates){
            
             System.err.println((count++) +" "+ id.getEntityId1() + " ************************  "+ id.getEntityId2());
               System.err.print(id.getEntityId1()+ " ");
              for (Attribute attribute :profiles3.get(id.getEntityId1()).getAttributes()){
               
            	  
            	  System.err.println(attribute.getName() + " "  + " " +attribute.getValue());
              }
              System.err.print(" -----");
               System.err.print(id.getEntityId2()+ " ");
            
               for (Attribute attribute :profiles.get(id.getEntityId2()).getAttributes()){
                  System.err.println(attribute.getName() + " "  + " "+ attribute.getValue());    
              }  
              if(x++>1)
              break;
              System.err.println("\n\n\n");
              System.err.flush();
        }
//        for (int i = 0; i < profiles3.size(); i++) {
//            String s[]={"","","","",""};
//            for (Attribute attribute :profiles3.get(i).getAttributes()){
//                  System.err.println(attribute.getName() +"  "+attribute.getValue());
//              //  name   starring writer actor genre director title year editor
//            	if(attribute.getName().equals("title"))
//                      s[0]=s[0].concat(attribute.getValue());
//                  if(attribute.getName().equals("writer"))
//                      s[1]=s[1].concat(attribute.getValue());
//                  if(attribute.getName().equals("starring"))
//                      s[2]=s[2].concat(attribute.getValue());
//                  if(attribute.getName().equals("year"))
//                      s[3]=s[3].concat(attribute.getValue());
//            }
//            if(i>5)
//            	break;
//     }
          // 
            
        
////             writer.print(":"+(i)+":");
////
//               System.err.print(i+";");
//                for (int j = 0; j < s.length-1; j++) {
//                    System.err.print(s[j] +" :");
//            //        writer.print(s[j] +" :");
//
//                }
//               // writer.println();
//                System.err.println(" ");
//                
           
//     }
//           writer.close();
//        HashMap<String, List<String>> attributeProfiles = new HashMap<>();
//        for (EntityProfile entity : profiles) {
//            for (Attribute attribute : entity.getAttributes()) {
//                List<String> values = attributeProfiles.get(attribute.getName());
//                if (values == null) {
//                    values = new ArrayList<>();
//                    attributeProfiles.put(attribute.getName(), values);
//                }
//                values.add(attribute.getValue());
//            }
//        }
        //     System.err.println(attributeProfiles.get("title") +"\n");
         
    
//         for (Attribute attribute :profiles3.get(id.getEntityId2()).getAttributes()){
//           //    System.err.println(attribute.getName() + " " +attribute.getValue() + " ");
//         }
        
//         for (int i = 0; i < profiles.size(); i++) {
//             for (Attribute attribute :profiles.get(i).getAttributes()){
//                 //System.err.println(attribute.getValue());
//                 String[] tokens = attribute.getValue().split("[\\W_]");
//                // System.err.println(attribute.getName() + " " +attribute.getValue() + " ");
////                 for (String t:tokens)
////                       System.err.print(t+ " ;");
//             }
//            // System.err.println();
//                     
//         }
//         
//         
//         List<EntityProfile> profiles3 = (ArrayList<EntityProfile>) SerializationUtilities.loadSerializedObject("/home/guilherme/Downloads/erframework-svn/bases/scholar");
//         for (int i = 0; i < profiles3.size(); i++) {
//             for (Attribute attribute :profiles3.get(i).getAttributes()){
//                 //System.err.println(attribute.getValue());
//                // String[] tokens = attribute.getValue().split("[\\W_]");
//                 System.err.println(attribute.getName() + " " +attribute.getValue() + " ");
////                 for (String t:tokens)
////                       System.err.print(t+ " ;");
//             }
//             System.err.println();
//                     
//         }
         
         
         
         
//         List<UnilateralBlock> blocksQueue;
//     EntityIndex entityIndex;
//     Set<IdDuplicates> groundTruth;
//     //HashSet<Integer>[] maxRecords;
//    
//     Set<HashSet<Integer>>[] previousCondition;
//    // HashSet<Integer>[] maxRecords;
////       List<EntityProfile> profiles2 = (ArrayList<EntityProfile>) SerializationUtilities.loadSerializedObject("/home/guilherme/Downloads/BlockingFramework/data/sets/dataset1_dblp");
//        //int noOfEntities = entityIndex.getNoOfEntities();
//       HashSet<Integer>[] maxRecords = new HashSet[100000];
//        Set<String>[] maxProfiles = new HashSet[100000];
//        for (int i = 0; i < 10000; i++) {
//            maxRecords[i] = new HashSet<Integer>();
//            maxRecords[i].add(i);
//
//            maxProfiles[i] = new HashSet<String>();
//            maxProfiles[i].addAll(ProfileComparison.getDistinctTokens(profiles2.get(i).getAttributes()));
//           // String s =profiles2.get(i).getAttributes()
//           // System.err.println(ProfileComparison.getDistinctTokens(profiles2.get(i).getAttributes()));
//        }
  
     }
     
        
}