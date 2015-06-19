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