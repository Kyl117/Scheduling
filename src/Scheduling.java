import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * This class is to schedule production. 
 */
public class Scheduling {
    private static int geneticAlgorithmIteration = 20;
    public static double idleTime = 0;

    //大循环的方向(上升/下跌)
    public static enum Direction{
        Increasing, Decreasing ;
    }
    public static Direction cycleDirection = Direction.Decreasing;

    /**
     *  sort the order list by kocks(27 groups) according to cycle direction
     */
    public static void sortOrderListbyBigGroup() {
        ArrayList<Order> tempOrderList = new ArrayList<Order>();
        if(cycleDirection == Direction.Decreasing){
            for (Map.Entry<Integer, ArrayList<Order>> item : OrderTable.standardGroupHashMap.entrySet()) {
                for (Order order : item.getValue()) {
                    tempOrderList.add(order);
                }
            }
        }
        else{
            for (Map.Entry<Integer, ArrayList<Order>> item : OrderTable.standardGroupHashMap.entrySet()) {
                for (Order order : item.getValue()) {
                    tempOrderList.add(0,order);
                }
            }
        }
        OrderTable.orderList = tempOrderList;
        
    }

        //(helper function)
        private static boolean onlyHeatingSystem(ArrayList<Order> localGroupOrderList,int heatingSystem){
            int size = localGroupOrderList.size();
            for(int i=0; i<size;i++){
                if(localGroupOrderList.get(i).getHeatingSystem() != heatingSystem){return false;}
            }
            return true;
        }
    
        // (helper function) for Step 2 random heating system sort
        private static int getValidRandomIndex(ArrayList<Order> localGroupOrderList, int lastHeatingSystem) {
            int randomIndex = (int) (Math.random() * localGroupOrderList.size());
            int count = 0;
            if (OrderTable.scheduleAfterHeatingSystemSort.size() > 0) {
                while ((lastHeatingSystem == 2 && localGroupOrderList.get(randomIndex).getHeatingSystem() == 3 && !onlyHeatingSystem(localGroupOrderList, 3))
                        || (lastHeatingSystem == 3 && localGroupOrderList.get(randomIndex).getHeatingSystem() == 2 && !onlyHeatingSystem(localGroupOrderList, 2))
                        || (lastHeatingSystem == 3 && localGroupOrderList.get(randomIndex).getHeatingSystem() == 4 && !onlyHeatingSystem(localGroupOrderList, 4))
                        || (lastHeatingSystem == 4 && localGroupOrderList.get(randomIndex).getHeatingSystem() == 3 && !onlyHeatingSystem(localGroupOrderList, 3)))
                {
                    count++;
                    if(count>20){break;}
                    randomIndex = (int) (Math.random() * localGroupOrderList.size() );
                }
            }
            return randomIndex;
        }

    // random sort order list by heating system without changing big group
    private static void randomSortOrderListByHeatingSystem() {
        
        int lastHeatingSystem = 0;
        if(cycleDirection == Direction.Increasing){
            ArrayList<Integer> standardGroupReverseList = new ArrayList<Integer>();
            for (Map.Entry<Integer, ArrayList<Order>> item : OrderTable.standardGroupHashMap.entrySet()) {
                standardGroupReverseList.add(item.getKey());
            }
            Collections.reverse(standardGroupReverseList);
            for(Integer key :standardGroupReverseList){
                ArrayList<Order> localGroupOrderList = OrderTable.standardGroupHashMap.get(key);
                final int originalSize = localGroupOrderList.size();
                
                for (int i = 0; i < originalSize; i++) {
                    int randomIndex = getValidRandomIndex(localGroupOrderList, lastHeatingSystem);
  
                    OrderTable.scheduleAfterHeatingSystemSort.add(localGroupOrderList.get(randomIndex));
                    lastHeatingSystem = localGroupOrderList.get(randomIndex).getHeatingSystem();
    
                    //record current Steel Type
                    String currentSteelType = localGroupOrderList.get(randomIndex).getSteelType();
    
                    //remove from local list
                    localGroupOrderList.remove(randomIndex);
    
                    //combine all orders with same steelType
                    for(int j=0; j<localGroupOrderList.size(); j++){
                        if(localGroupOrderList.get(j).getSteelType().equals(currentSteelType)){
                            OrderTable.scheduleAfterHeatingSystemSort.add(localGroupOrderList.get(j));
                            localGroupOrderList.remove(j);
                            j--;
                            i++;
                        }
                    }
                    
                }                
            }    
        }
        else{
            for (Map.Entry<Integer, ArrayList<Order>> item : OrderTable.standardGroupHashMap.entrySet()) {
                ArrayList<Order> localGroupOrderList = (ArrayList<Order>)item.getValue().clone();
                final int originalSize = localGroupOrderList.size(); 
                
                for (int i = 0; i < originalSize; i++) {
                    int randomIndex = getValidRandomIndex(localGroupOrderList, lastHeatingSystem);
                     
                    OrderTable.scheduleAfterHeatingSystemSort.add(localGroupOrderList.get(randomIndex));
                    lastHeatingSystem = localGroupOrderList.get(randomIndex).getHeatingSystem();
    
                    //record current Steel Type
                    String currentSteelType = localGroupOrderList.get(randomIndex).getSteelType();
                   
                    //remove from local list
                    localGroupOrderList.remove(randomIndex);
                   
                    //combine all orders with same steelType
                    for(int j=0; j<localGroupOrderList.size(); j++){
                        if(localGroupOrderList.get(j).getSteelType().equals(currentSteelType)){
                            OrderTable.scheduleAfterHeatingSystemSort.add(localGroupOrderList.get(j));
                            localGroupOrderList.remove(j);
                            j--;
                            i++;
                        }
                    }
                }
            } 
        }  
    }

    /**
     * sort the orders according to heating system.
     * 根据加热制度排序时间表
     */
    public static void geneticAlgorithmWithHeatingSystem(){
        int leastTime = 0;
        ArrayList<Order> leastidleTimeSchedule = new ArrayList<Order>();
        for(int i=0; i<geneticAlgorithmIteration; i++){
            Scheduling.randomSortOrderListByHeatingSystem();
            
            int currentIdleTime = (int)FitnessCalculator.calculateidleTime(OrderTable.scheduleAfterHeatingSystemSort);   

            if( i == 0 || currentIdleTime < leastTime){
                leastTime = currentIdleTime;
                leastidleTimeSchedule = (ArrayList<Order>)OrderTable.scheduleAfterHeatingSystemSort.clone();
                
            }
            OrderTable.scheduleAfterHeatingSystemSort.clear();
        }
        OrderTable.scheduleAfterHeatingSystemSort = (ArrayList<Order>)leastidleTimeSchedule.clone();
    }

    private static void randomSortOrderListByCuttingType(){
        for(ArrayList<Order> item: OrderTable.kocksGroupHeatingSystemOrderList){
            ArrayList<Order> localGroupOrderList = (ArrayList<Order>)item.clone();
            final int originalSize = localGroupOrderList.size(); 
                
            for (int i = 0; i < originalSize; i++) {
                int randomIndex = (int) (Math.random() * localGroupOrderList.size() );
                    
                OrderTable.scheduleAfterCuttingTypeSort.add(localGroupOrderList.get(randomIndex));

                //record current Steel Type
                String currentSteelType = localGroupOrderList.get(randomIndex).getSteelType();
                
                //remove from local list
                localGroupOrderList.remove(randomIndex);
                
                //combine all orders with same steelType
                for(int j=0; j<localGroupOrderList.size(); j++){
                    if(localGroupOrderList.get(j).getSteelType().equals(currentSteelType)){
                        OrderTable.scheduleAfterCuttingTypeSort.add(localGroupOrderList.get(j));
                        localGroupOrderList.remove(j);
                        j--;
                        i++;
                    }
                }
            }
        }
    }

    /**
     * Sort the orders according to Cutting Type
     * 根据切割制度排序时间表
     */
    public static void geneticAlgorithmWithCuttingType(){
        int leastTime = 0;
        ArrayList<Order> leastidleTimeSchedule = new ArrayList<Order>();
        for(int i=0; i<geneticAlgorithmIteration; i++){
            
            Scheduling.randomSortOrderListByCuttingType();
            
            int currentIdleTime = (int)FitnessCalculator.calculateidleTime(OrderTable.scheduleAfterCuttingTypeSort);   

            if( i == 0 || currentIdleTime < leastTime){
                leastTime = currentIdleTime;
                leastidleTimeSchedule = (ArrayList<Order>)OrderTable.scheduleAfterCuttingTypeSort.clone();
            }
            OrderTable.scheduleAfterCuttingTypeSort.clear();
        }
        OrderTable.scheduleAfterCuttingTypeSort = (ArrayList<Order>)leastidleTimeSchedule.clone();
    }

    public static void randomSortOrderListByStandard(){
        for(ArrayList<Order> item: OrderTable.kocksGroupHeatingSystemCuttingTypeOrderList){
            ArrayList<Order> localGroupOrderList = (ArrayList<Order>)item.clone();
            final int originalSize = localGroupOrderList.size(); 
                
            for (int i = 0; i < originalSize; i++) {
                int randomIndex = (int) (Math.random() * localGroupOrderList.size() );
                    
                OrderTable.scheduleAfterStandardSort.add(localGroupOrderList.get(randomIndex));

                //record current Steel Type
                String currentSteelType = localGroupOrderList.get(randomIndex).getSteelType();
                
                //remove from local list
                localGroupOrderList.remove(randomIndex);
                
                //combine all orders with same steelType
                for(int j=0; j<localGroupOrderList.size(); j++){
                    if(localGroupOrderList.get(j).getSteelType().equals(currentSteelType)){
                        OrderTable.scheduleAfterStandardSort.add(localGroupOrderList.get(j));
                        localGroupOrderList.remove(j);
                        j--;
                        i++;
                    }
                }
            }
        }
    }

    /**
     * Sort the orders according to Standard
     * 根据成品规格排序时间表
     */
    public static void geneticAlgorithmWithStandard(){
        int leastTime = 0;
        ArrayList<Order> leastidleTimeSchedule = new ArrayList<Order>();
        for(int i=0; i<geneticAlgorithmIteration; i++){
            
            Scheduling.randomSortOrderListByStandard();
            
            int currentIdleTime = (int)FitnessCalculator.calculateidleTime(OrderTable.scheduleAfterStandardSort);   

            if( i == 0 || currentIdleTime < leastTime){
                leastTime = currentIdleTime;
                leastidleTimeSchedule = (ArrayList<Order>)OrderTable.scheduleAfterStandardSort.clone();
            }
            OrderTable.scheduleAfterStandardSort.clear();
        }
        OrderTable.scheduleAfterStandardSort = (ArrayList<Order>)leastidleTimeSchedule.clone();
    }
}
