import java.util.ArrayList;
import java.util.Collections;

public class Distribution 
{
	private int size;
	private double mean, SD, topPercent, corPercent, norPercent;
	private String type, name;
	private ArrayList<Data> values = new ArrayList<Data>();
	public Distribution (String name, String type, int size, double mean, double SD)//Normal Distributions
	{
		this.name = name;
		this.type = type;
		this.size = size;
		this.mean = mean;
		this.SD = SD;
		topPercent = 0;
		corPercent = 0;
		norPercent = 0;
	}
	
	public Distribution (String name, String type, int size, double topPercent, double corPercent, double norPercent)//Binary Correlation
	{
		this.name = name;
		this.type = type;
		this.size = size;
		this.mean = 0;
		this.SD = 0;
		this.topPercent = topPercent;
		this.corPercent = corPercent;
		this.norPercent = norPercent;
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
