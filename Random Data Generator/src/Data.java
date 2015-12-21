/*
 * Data class will be used to store randomly generated data
 * Each object has an non-unique ID, so different sets of Data can correspond to the same person
 */
public class Data {
public int ID;
public double value;

public Data(int ID, double value)
{
	this.ID = ID;
	this.value = value;
}

public int compareTo(Data o) {
  return Double.compare(value, o.value);
}
}