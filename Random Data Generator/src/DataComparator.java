//Simple comparator that dictates the Data object is to be compared by it's value, not it's ID
import java.util.Comparator;

public class DataComparator implements Comparator<Data>{

    @Override
    public int compare(Data x, Data y){
        if(x.value<y.value){
            return -1;
        }
        if(x.value>y.value){
            return 1;
        }
        return 0;
    }
}