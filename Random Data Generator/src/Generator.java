//TESTING GIT
//Jorge Test

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

public class Generator {

	public static void main(String[] args) throws BiffException, IOException, RowsExceededException, WriteException {
		
		int selection = 99;
		Scanner userInput = new Scanner(System.in);
		while (selection != 0)
		{
			System.out.println("Enter an Input:\n1. Single Binary Distribution\n2. Simple Correlation (Binary values)\n0. Exit");
			selection = userInput.nextInt();
			switch (selection){
				case 1:
				{
					System.out.println("Enter the number of values, the mean, and the Standard deviation.\n");
					int Size = userInput.nextInt();
					double Mean = userInput.nextDouble();
					double StandardDev = userInput.nextDouble();
					ArrayList<Double> bin = new ArrayList<Double>(); 
					bin = binaryDistribution(Size, Mean, StandardDev);
					WritableWorkbook workbook = Workbook.createWorkbook(new File("BinaryDistribution.xls"));
					WritableSheet sheet = workbook.createSheet("Binary Distribution", 0);
					int j = 0;
					for(double i : bin)
					{
						Number number = new Number(0, j, i); 
						sheet.addCell(number);
						j++;
					}
					workbook.write(); 
					workbook.close();
					break;
				}
				
				case 2:
				{
					System.out.println("Fist populate Binary Distribution. Enter number of values, mean, and Standard Deviation");
					int Size = userInput.nextInt();
					double Mean = userInput.nextDouble();
					double StandardDev = userInput.nextDouble();
					ArrayList<Double> bin = new ArrayList<Double>(); 
					bin = binaryDistribution(Size, Mean, StandardDev);
					Collections.sort(bin);
					
					System.out.println("Now enter the top percentage, the correlation percentage, and the normal percentage");
					double topPercent = userInput.nextDouble();
					double corPercent = userInput.nextDouble();
					double norPercent = userInput.nextDouble();
					
					ArrayList<Integer> corList = TopPercentBinaryCorrelation(topPercent, corPercent, norPercent, bin);
					
					WritableWorkbook workbook = Workbook.createWorkbook(new File("SimpleBinaryCorrelation.xls"));
					WritableSheet sheet = workbook.createSheet("Correlation", 0);
					int j = 0;
					for(double i : bin)
					{
						Number number = new Number(0, j, i); 
						Number number2 = new Number(1, j, corList.get(j));
						sheet.addCell(number);
						sheet.addCell(number2);
						j++;
					}
					workbook.write(); 
					workbook.close();
					break;
				}
			}
		}
		
	}
	
	
	 private static ArrayList<Double> binaryDistribution(int size, double mean, double sd)
	{
		ArrayList<Double> retList = new ArrayList<Double>();
		Random generator = new Random();
		for(int i = 0; i < size; i++)
		{
			retList.add(generator.nextGaussian()*sd+mean);
		}
		
		return retList;
	}

	 private static ArrayList<Integer> TopPercentBinaryCorrelation(double topPercent, double corPercent, double normPercent, ArrayList<Double> inData)
	 {
		 ArrayList<Integer> retList = new ArrayList<Integer>();
		 Collections.sort(inData);
		 int topN = (int) (inData.size() * topPercent);
		 
		 int i = 0;
		 Random generator = new Random();
		 for (double d : inData)
		 {
			 if (i < inData.size() - topN)
			 {
				 if (generator.nextDouble()*1 <= normPercent)
					 retList.add(1);
				 else retList.add(0);
			 }
			 else
			 {
				 if (generator.nextDouble()*1 <= corPercent)
					 retList.add(1);
				 else retList.add(0);
			 }
			 i++;
		 }
		 
		 
		 return retList;
	 }
}
