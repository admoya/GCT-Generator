import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.io.File; 
import java.io.IOException;
import java.util.Date; 

import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import jxl.*;
import jxl.read.biff.BiffException;

import java.sql.*;
public class Generator {

	public static void main(String[] args) throws BiffException, IOException, RowsExceededException, WriteException, SQLException {
		ArrayList<Distribution> distributions = new ArrayList<Distribution>();
		//Reading Header Table and saving parameters
		 // JDBC driver name and database URL
		   String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		   String DB_URL = "jdbc:mysql://localhost/gct";
		//  Database credentials
		   String USER = "root";
		   String PASS = "gerby1";
		   
		   Connection conn = null;
		   Statement stmt = null;
		   try{
			      //STEP 2: Register JDBC driver
			      Class.forName("com.mysql.jdbc.Driver");

			      //STEP 3: Open a connection
			      System.out.println("Connecting to database...");
			      conn = DriverManager.getConnection(DB_URL,USER,PASS);
			      
			      //STEP 4: Execute a query
			      System.out.println("Creating statement...");
			      stmt = conn.createStatement();
			      String sql;
			      sql = "SELECT * FROM import_data_header";
			      ResultSet rs = stmt.executeQuery(sql);

			      //STEP 5: Extract data from result set
			      while(rs.next()){
			         //Retrieve by column name
			         int id  = rs.getInt("Dist_ID");
			         String name = rs.getString("Name");
			         String distType = rs.getString("Dist_Type");
			         int size = rs.getInt("Size");
			         int corID = rs.getInt("Cor_ID");
			         double mean = rs.getDouble("Mean");
			         double sd = rs.getDouble("Std_Dev");
			         double min = rs.getDouble("Min");
			         double max = rs.getDouble("Max");
			         double topPercent = rs.getDouble("Top_Percent");
			         double corPercent = rs.getDouble("Cor_Percent");
			         double normPercent = rs.getDouble("Norm_Percent");
			         double topMean = rs.getDouble("Top_Mean");
			         double topSD = rs.getDouble("Top_Std_Dev");
			         double normMean = rs.getDouble("Norm_Mean");
			         double normSD = rs.getDouble("Norm_Std_Dev");

			         //Display values
			         System.out.print("ID: " + id);
			         System.out.print(", Name: " + name);
			         System.out.print(", Type: " + distType);
			         System.out.print(", Size: " + size);
			         System.out.print(", CorID: " + corID);
			         System.out.print(", Mean: " + mean);
			         System.out.print(", SD: " + sd);
			         System.out.print(", Min: " + min);
			         System.out.print(", Max: " + max);
			         System.out.print(", TopPercent: " + topPercent);
			         System.out.print(", CorPercent: " + corPercent);
			         System.out.print(", NormPercent: " + normPercent);
			         System.out.print(", TopMean: " + topMean);
			         System.out.print(", TopSD: " + topSD);
			         System.out.print(", normMean: " + normMean);
			         System.out.println(", normSD: " + normSD);
			         
			         switch(distType)
			         {
			         case ("Normal"):
			        	 distributions.add(newBinaryDistribution(id, size, name, mean, sd));
			         	break;
			         case ("SBinCor"):
			        	 distributions.add(TopPercentBinaryCorrelation(id, name, topPercent, corPercent, normPercent, getDistFromID(corID, distributions)));
			         	break;
			         case ("SNumCor"):
			        	 distributions.add(TopPercentNumericalCorrelation(id, name, topPercent, topMean, topSD, normMean, normSD, getDistFromID(corID, distributions)));
			         	break;
			         case ("Bounded"):
			        	 distributions.add(newBoundedDistribution(id, size, name, normMean, normSD, min, max));
			         	break;
			         }
			      }
			      //Populate detail table
			      for (Distribution d : distributions)
					{
						for(Data data : d.getValues())
						{
						String query = "Insert into import_data_detail (Dist_ID, Subset_ID, Value)" + " values (?, ?, ?)";
					      PreparedStatement preparedStmt = conn.prepareStatement(query);
					      preparedStmt.setInt(1, d.getID());
					      preparedStmt.setInt(2, data.ID);
					      preparedStmt.setDouble(3, data.value);
					      preparedStmt.execute();
						}
					      
					}
			      //STEP 6: Clean-up environment
			      rs.close();
			      stmt.close();
			      conn.close();
			   }catch(SQLException se){
			      //Handle errors for JDBC
			      se.printStackTrace();
			   }catch(Exception e){
			      //Handle errors for Class.forName
			      e.printStackTrace();
			   }finally{
			      //finally block used to close resources
			      try{
			         if(stmt!=null)
			            stmt.close();
			      }catch(SQLException se2){
			      }// nothing we can do
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }//end finally try
			   }//end try
		   System.out.println("Done.");
		
	}
	
	private static Distribution getDistFromID(int ID, ArrayList<Distribution> dists)
	{
		for (Distribution d : dists)
			if (d.getID() == ID)
				return d;
		return null;
	}
	 private static Distribution newBinaryDistribution(int ID, int size, String name, double mean, double sd)
	{
		Distribution retVal = new Distribution(ID, name, "Normal", size, mean, sd);
		//ArrayList<Double> retList = new ArrayList<Double>();
		Random generator = new Random();
		for(int i = 0; i < size; i++)
		{
			retVal.addData(i, (generator.nextGaussian()*sd+mean));
		}
		
		return retVal;
	}

	 private static Distribution TopPercentBinaryCorrelation(int ID, String name, double topPercent, double corPercent, double normPercent, Distribution inData)
	 {
		 Distribution retVal = new Distribution(ID, name, "BCor", inData.getSize(), topPercent, corPercent, normPercent);
		 //ArrayList<Integer> retList = new ArrayList<Integer>();
		 inData.sort();
		 int topN = (int) (inData.getSize() * topPercent);
		 
		 int i = 0;
		 Random generator = new Random();
		 for (Data d : inData.getValues())
		 {
			 if (i < inData.getSize() - topN)
			 {
				 if (generator.nextDouble()*1 <= normPercent)
					 retVal.addData(d.ID, 1);
				 else retVal.addData(d.ID, 0);
			 }
			 else
			 {
				 if (generator.nextDouble()*1 <= corPercent)
					 retVal.addData(d.ID, 1);
				 else retVal.addData(d.ID, 0);
			 }
			 i++;
		 }
		 
		 
		 return retVal;
	 }
	 
	 private static Distribution TopPercentNumericalCorrelation (int ID, String name, double topPercent, double topMean, double topSD, double norMean, double norSD, Distribution inData)
				{
		 			Distribution retVal = new Distribution(ID, name, "NumCor", inData.getSize(), topPercent, norMean, norSD, topMean, topSD);
		 			inData.sort();
		 			int topN = (int) (inData.getSize() * topPercent);
		 			int i = 0;
		 			Random generatorLo = new Random();
		 			Random generatorHi = new Random();
		 			for (Data d : inData.getValues())
		 			{
		 				if (i < inData.getSize() - topN)
		 				{
		 					retVal.addData(d.ID, generatorLo.nextGaussian()*norSD+norMean);
		 				}
		 				else 
		 				{
		 					retVal.addData(d.ID, generatorHi.nextGaussian()*topSD+topMean);
		 				}
		 				i++;
		 			}
		 			
		 			return retVal;
				}

	 private static Distribution newBoundedDistribution(int ID, int size, String name, double mean, double sd, double min, double max)
		{
			Distribution retVal = new Distribution(ID, name, "Bounded", size, mean, sd);
			//ArrayList<Double> retList = new ArrayList<Double>();
			Random generator = new Random();
			for(int i = 0; i < size; i++)
			{
				double val = (generator.nextGaussian()*sd+mean);
				while (val < min || val > max)
				{
					val = (generator.nextGaussian()*sd+mean);
				}
				retVal.addData(i,  val);
			}
			
			return retVal;
		}
}

	