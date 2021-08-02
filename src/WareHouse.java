import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * The class to record the data of steel blank in the warehouse.
 * 记录仓库里已经有的坯料。
 */
public class WareHouse {
    private static int inputColWeight = 2; 
    private static int inputColSteelType = 13;

    public static HashMap<String, Float> steelWeightTable = new HashMap<String, Float>();

    public static void createSteelWeightTable(ArrayList<ArrayList<String>> records){    
        // for(int i=0; i<records.get(0).size(); i++){
        //     System.out.println(records.get(0).get(i));
        //     // if(records.get(0).get(i) == "钢种"){inputColSteelType = i;}
        //     // if(records.get(0).get(i) == "重量"){inputColWeight = i;}
        // }

        int numberOfRow = records.size();

        for(int i=1; i<numberOfRow;i++){
            String key = records.get(i).get(inputColSteelType);
            Float value = Float.valueOf(records.get(i).get(inputColWeight));
            if(steelWeightTable.containsKey(key)){
                steelWeightTable.replace(key,steelWeightTable.get(key) + value);
            }
            else{
                steelWeightTable.put(key, value);
            }
        }
    }

    public static void printSteelWeightTable(){

        System.out.println("Warehouse:");
        System.out.println("Steeltype\tTotal weight");
        for(Map.Entry<String, Float> item:steelWeightTable.entrySet()){
            System.out.print(item.getKey()+":\t\t");
            System.out.println(item.getValue());
        }
        System.out.println("------------------------------------");
    }
}
