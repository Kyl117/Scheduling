import java.util.HashMap;
import java.util.Map;

/**
 * This class is to calculate the demand of 坯料.  
 * 
 */
public class DemandCalculator {
    public static HashMap<String, Float> steelDemandTable = new HashMap<String, Float>();

    /**
     * Calculate the 
     * 计算每个订单的坯料需求（需求 = 生产叮当所需坯料/0.97 - 坯料库存）
     */
    public static void calculateDemand(){
        for(Map.Entry<String, Float> item:OrderTable.steelWeightTable.entrySet()){
            if(WareHouse.steelWeightTable.containsKey(item.getKey())){
                Float demand = OrderTable.steelWeightTable.get(item.getKey())/0.97f - WareHouse.steelWeightTable.get(item.getKey());
                steelDemandTable.put(item.getKey(), demand);
            }
            else{
                steelDemandTable.put(item.getKey(), item.getValue());
            }
        }
    }

    public static void printSteelDemandTable(){
        System.out.println("Demand:");
        System.out.println("Steeltype\tTotal weight");
        for(Map.Entry<String, Float> item:steelDemandTable.entrySet()){
            System.out.print(item.getKey()+":\t\t");
            System.out.println(item.getValue());
        }
        System.out.println("------------------------------------");
    }

    
}
