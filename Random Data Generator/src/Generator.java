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
		int size;
		ArrayList<Distribution> distributions = new ArrayList<Distribution>();
		Scanner userInput = new Scanner(System.in);
		System.out.println("Enter size: ");
		size = userInput.nextInt();
		int[][] index = new int[100][100];
		while (selection != 0)
		{
			System.out.println("Enter an Input:\n1. New Normal Distribution\n2. New Bounded Distribution\n3. Simple Correlation (Binary values)\n4. Simple Correlation (Numerical Values)\n9. Print to Excel\n0. Exit");
			selection = userInput.nextInt();
			switch (selection){
				case 1:
				{
					System.out.println("Enter the name, mean, and the Standard deviation.\n");
					String Name = userInput.next();
					double Mean = userInput.nextDouble();
					double StandardDev = userInput.nextDouble();
					int id = -1;
					for (int i = 0; i < 100; i++)
					{
						if (index[0][i] == 0)
						{
							id = i+1;
							index[0][i] = i+1;
							break;
						}
					}
					Distribution bin = newBinaryDistribution(id, size, Name, Mean, StandardDev);
					//bin = binaryDistribution(Size, Mean, StandardDev);
					WritableWorkbook workbook = Workbook.createWorkbook(new File("BinaryDistribution.xls"));
					WritableSheet sheet = workbook.createSheet("Binary Distribution", 0);
					//int j = 0;
					for (int i = 0; i < size; i ++)
					{
						double value = bin.getData(i);
						Number number = new Number(0,i,value);
						sheet.addCell(number);
					}
					/*
					for(Data d : bin.getValues())
					{
						Number number = new Number(0, j, d.value); 
						sheet.addCell(number);
						j++;
					}
					*/
					workbook.write(); 
					workbook.close();
					distributions.add(bin);
					break;
				}
				
				case 2:
				{
					System.out.println("Enter the name, mean, Standard deviation, Minimum, and Maximum.\n");
					String Name = userInput.next();
					double Mean = userInput.nextDouble();
					double StandardDev = userInput.nextDouble();
					double min = userInput.nextDouble();
					double max = userInput.nextDouble();
					int id=-1;
					for (int i = 0; i < 100; i++)
					{
						if (index[0][i] == 0)
						{
							id = i+1;
							index[0][i] = i+1;
							break;
						}
					}
					Distribution bin = newBoundedDistribution(id, size, Name, Mean, StandardDev, min, max);
					//bin = binaryDistribution(Size, Mean, StandardDev);
					WritableWorkbook workbook = Workbook.createWorkbook(new File("BoundedDistribution.xls"));
					WritableSheet sheet = workbook.createSheet("Bounded Distribution", 0);
					//int j = 0;
					for (int i = 0; i < size; i ++)
					{
						double value = bin.getData(i);
						Number number = new Number(0,i,value);
						sheet.addCell(number);
					}
					/*
					for(Data d : bin.getValues())
					{
						Number number = new Number(0, j, d.value); 
						sheet.addCell(number);
						j++;
					}
					*/
					workbook.write(); 
					workbook.close();
					distributions.add(bin);
					break;
				}
				
				
				case 3:
				{
					System.out.println("Choose a distribution");
					int i = 0;
					for (Distribution d : distributions)
					{
						System.out.println(i+1 + ". " + d.getName());
						i++;
					}
					int sel = userInput.nextInt();
					Distribution base = distributions.get(sel-1);
					/*
					System.out.println("First populate Binary Distribution. Enter number of values, mean, and Standard Deviation");
					int Size = userInput.nextInt();
					double Mean = userInput.nextDouble();
					double StandardDev = userInput.nextDouble();
					ArrayList<Double> bin = new ArrayList<Double>(); 
					bin = binaryDistribution(Size, Mean, StandardDev);
					Collections.sort(bin);
					*/
					System.out.println("Now enter the name, top percentage, the correlation percentage, and the normal percentage");
					String name = userInput.next();
					double topPercent = userInput.nextDouble();
					double corPercent = userInput.nextDouble();
					double norPercent = userInput.nextDouble();
					int corId = base.getID();
					int id=-1;
					for (int j = 0; j < 100; j++)
					{
						if (index[0][j] == 0)
						{
							id = j+1;
							index[0][j] = j+1;
							break;
						}
					}
					Distribution corList = TopPercentBinaryCorrelation(id, name, topPercent, corPercent, norPercent, base);
					
					for(int j = 0; j <100; j++)
					{
						if(index[j][corId-1] == 0)
						{
							index[j][corId-1] = id;
							break;
						}
					}
					
					WritableWorkbook workbook = Workbook.createWorkbook(new File("SimpleBinaryCorrelation.xls"));
					WritableSheet sheet = workbook.createSheet("Correlation", 0);
					for (int j = 0; j < size; j++)
					{
						double baseVal = base.getData(j);
						double corVal = corList.getData(j);
						Number number1 = new Number(0, j, baseVal);
						Number number2 = new Number(1, j, corVal);
						sheet.addCell(number1);
						sheet.addCell(number2);
					}
					/*
					for(double i : bin)
					{
						Number number = new Number(0, j, i); 
						Number number2 = new Number(1, j, corList.get(j));
						sheet.addCell(number);
						sheet.addCell(number2);
						j++;
					}
					*/
					workbook.write(); 
					workbook.close();
					distributions.add(corList);
					break;
				}
				
				case 4:
				{
					System.out.println("Choose a distribution");
					int i = 0;
					for (Distribution d : distributions)
					{
						System.out.println(i+1 + ". " + d.getName());
						i++;
					}
					int sel = userInput.nextInt();
					Distribution base = distributions.get(sel-1);
					System.out.println("Now enter the name, top percentage, top mean, top SD, normal mean, normal SD");
					String name = userInput.next();
					double topPercent = userInput.nextDouble();
					double topMean = userInput.nextDouble();
					double topSD = userInput.nextDouble();
					double norMean = userInput.nextDouble();
					double norSD = userInput.nextDouble();
					int corId = base.getID();
					int id=-1;
					for (int j = 0; j < 100; j++)
					{
						if (index[0][j] == 0)
						{
							id = j+1;
							index[0][j] = j+1;
							break;
						}
					}
					
					Distribution corList = TopPercentNumericalCorrelation(id, name, topPercent, topMean, topSD, norMean, norSD, base);
					for(int j = 0; j <100; j++)
					{
						if(index[j][corId-1] == 0)
						{
							index[j][corId-1] = id;
							break;
						}
					}
					
					WritableWorkbook workbook = Workbook.createWorkbook(new File("SimpleNumericalCorrelation.xls"));
					WritableSheet sheet = workbook.createSheet("Correlation", 0);
					for (int j = 0; j < size; j++)
					{
						double baseVal = base.getData(j);
						double corVal = corList.getData(j);
						Number number1 = new Number(0, j, baseVal);
						Number number2 = new Number(1, j, corVal);
						sheet.addCell(number1);
						sheet.addCell(number2);
					}
					/*
					for(double i : bin)
					{
						Number number = new Number(0, j, i); 
						Number number2 = new Number(1, j, corList.get(j));
						sheet.addCell(number);
						sheet.addCell(number2);
						j++;
					}
					*/
					workbook.write(); 
					workbook.close();
					distributions.add(corList);
					break;
				}
				case 9:
				{
					int i = 0;
					WritableWorkbook workbook = Workbook.createWorkbook(new File("RandomData.xls"));
					WritableSheet sheet = workbook.createSheet("Sheet1", 0);
					for (Distribution d : distributions)
					{
						for (int j = 0; j<size; j++)
						{
							double val = d.getData(j);
							Number number = new Number(i, j, val);
							sheet.addCell(number);
						}
						i++;
					}
					workbook.write();
					workbook.close();
					break;
				}
			}
		}
		userInput.close();
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

	