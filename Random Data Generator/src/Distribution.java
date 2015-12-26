/*
 * Distribution object to represent a set of data
 * Will contain information on which kind of distribution it is, and appropriate parameters as needed
 */
import java.util.ArrayList;
import java.util.Collections;

public class Distribution 
{
	private int size, ID;
	private double mean, SD, topPercent, corPercent, norPercent, norMean, norSD, topMean, topSD, min, max, falseChance, falseVal, trueChance, trueMean, trueSD;
	public double getFalseChance() {
		return falseChance;
	}

	public void setFalseChance(double falseChance) {
		this.falseChance = falseChance;
	}

	public double getFalseVal() {
		return falseVal;
	}

	public void setFalseVal(double falseVal) {
		this.falseVal = falseVal;
	}

	public double getTrueChance() {
		return trueChance;
	}

	public void setTrueChance(double trueChance) {
		this.trueChance = trueChance;
	}

	public double getTrueMean() {
		return trueMean;
	}

	public void setTrueMean(double trueMean) {
		this.trueMean = trueMean;
	}

	public double getTrueSD() {
		return trueSD;
	}

	public void setTrueSD(double trueSD) {
		this.trueSD = trueSD;
	}
	private String type, name;
	private ArrayList<Data> values = new ArrayList<Data>();
	public Distribution (int ID, String name, String type, int size, double mean, double SD)//Normal Distributions
	{
		this.ID = ID;
		this.name = name;
		this.type = type;
		this.size = size;
		this.mean = mean;
		this.SD = SD;
	}
	
	public Distribution (int ID, String name, String type, int size, double mean, double SD, double min, double max)//Bounded Distributions
	{
		this.ID = ID;
		this.name = name;
		this.type = type;
		this.size = size;
		this.mean = mean;
		this.SD = SD;
		this.min = min;
		this.max = max;
	}
	
	public Distribution (int ID, String name, String type, int size, double topPercent, double corPercent, double norPercent)//Binary Correlation
	{
		this.ID = ID;
		this.name = name;
		this.type = type;
		this.size = size;
		this.topPercent = topPercent;
		this.corPercent = corPercent;
		this.norPercent = norPercent;
	}
	
	public Distribution (int ID, String name, String type, int size, double topPercent, double norMean, double norSD, double topMean, double topSD)//Numerical Correlation
	{
		this.ID = ID;
		this.name = name;
		this.type = type;
		this.size = size;
		this.topPercent = topPercent;
		this.corPercent = 0;
		this.norPercent = 0;
		this.norMean = norMean;
		this.norSD = norSD;
		this.topMean = topMean;
		this.topSD = topSD;
	}
	
	public Distribution (int ID, int size, String name, String type, double falseChance, double falseVal, double trueChance, double trueMean, double trueSD)//Dynamic Binary Correlation
	{
		this.ID = ID;
		this.name = name;
		this.type = type;
		this.size = size;
		this.falseChance = falseChance;
		this.falseVal = falseVal;
		this.trueChance = trueChance;
		this.trueMean = trueMean;
		this.trueSD = trueSD;
	}
	
	/* *********************Getters and Setters ******************** */
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public double getNorMean() {
		return norMean;
	}

	public void setNorMean(double norMean) {
		this.norMean = norMean;
	}

	public double getNorSD() {
		return norSD;
	}

	public void setNorSD(double norSD) {
		this.norSD = norSD;
	}

	public double getTopMean() {
		return topMean;
	}

	public void setTopMean(double topMean) {
		this.topMean = topMean;
	}

	public double getTopSD() {
		return topSD;
	}

	public void setTopSD(double topSD) {
		this.topSD = topSD;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getTopPercent() {
		return topPercent;
	}

	public void setTopPercent(double topPercent) {
		this.topPercent = topPercent;
	}

	public double getCorPercent() {
		return corPercent;
	}

	public void setCorPercent(double corPercent) {
		this.corPercent = corPercent;
	}

	public double getNorPercent() {
		return norPercent;
	}

	public void setNorPercent(double norPercent) {
		this.norPercent = norPercent;
	}

	public void sort()
	{
		DataComparator compare = new DataComparator();
		Collections.sort(values, compare);
	}
	
	public ArrayList<Data> getValues() {
		return values;
	}

	public void setValues(ArrayList<Data> values) {
		this.values = values;
	}

	public void addData(int ID, double value)
	{
		values.add(new Data(ID, value));
	}
	
	public double getData(int ID)
	{
		for (Data d : values)
			if (d.ID == ID)
			{
				return d.value;
			}
		return Double.MAX_VALUE * -1; //If not found
	}
	
	public void deleteData(int ID)
	{
		for (Data d : values)
			if (d.ID == ID)
			{
				values.remove(d);
				break;
			}
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getSD() {
		return SD;
	}
	public void setSD(double sD) {
		SD = sD;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
