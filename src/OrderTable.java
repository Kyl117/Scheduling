import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class OrderTable {
    private static final int inputColOrderNmuber = 1;
    private static final int inputColSteelType = 8;
    private static final int inputColStandard = 12;
    private static final int inputColWeight = 29;

    public static ArrayList<Order> orderList = new ArrayList<Order>();
    public static ArrayList<Order> orderListAfterHeatingSystemSort = new ArrayList<Order>();
    public static ArrayList<Order> orderListAfterCuttingTypeSort = new ArrayList<Order>();
    public static HashMap<Integer, ArrayList<Order>> standardGroupHashMap = new HashMap<Integer, ArrayList<Order>>();
    public static HashMap<Integer, ArrayList<Order>> standardGroupHeatingSystemHashMap = new HashMap<Integer, ArrayList<Order>>();
    public static HashMap<Float, ArrayList<Order>> standardHashMap = new HashMap<Float, ArrayList<Order>>();
    public static HashMap<String, Float> steelWeightTable = new HashMap<String, Float>();

    private static void createOrderList(int numberOfOrder, ArrayList<ArrayList<String>> records) {
        for (int i = 1; i < numberOfOrder + 1; i++) {
            OrderTable.orderList.add(new Order(OrderTable.orderList.size() + 1, records.get(i).get(inputColOrderNmuber),
                    Float.valueOf(records.get(i).get(inputColStandard)),
                    StandardToGroup.convertToStandardGroup(Float.valueOf(records.get(i).get(inputColStandard))), 99,
                    records.get(i).get(inputColSteelType), 101, Float.valueOf(records.get(i).get(inputColWeight))));
        }
    }

    // 大組 big group
    private static void createStandardGroupHashMap(int numberOfOrder) {
        for (int i = 0; i < numberOfOrder; i++) {
            int key = OrderTable.orderList.get(i).getStandardGroup();
            if (OrderTable.standardGroupHashMap.containsKey(key)) {
                OrderTable.standardGroupHashMap.get(key).add(OrderTable.orderList.get(i));
            } else {
                ArrayList<Order> newArrayList = new ArrayList<Order>();
                newArrayList.add(OrderTable.orderList.get(i));
                OrderTable.standardGroupHashMap.put(key, newArrayList);
            }
        }
    }

    // 中組 middle group
    private static void createStandardHashMap(int numberOfOrder) {
        for (int i = 0; i < numberOfOrder - 1; i++) {
            Float key = OrderTable.orderList.get(i).getStandard();
            if (OrderTable.standardHashMap.containsKey(key)) {
                OrderTable.standardHashMap.get(key).add(OrderTable.orderList.get(i));
            } else {
                ArrayList<Order> newArrayList = new ArrayList<Order>();
                newArrayList.add(OrderTable.orderList.get(i));
                OrderTable.standardHashMap.put(key, newArrayList);
            }
        }
    }

    public static void createSteelWeightTable() {
        for (Order item : orderList) {
            String key = item.getSteelType();

            Float value = Float.valueOf(item.getWeight());
            if (steelWeightTable.containsKey(key)) {
                steelWeightTable.replace(key, steelWeightTable.get(key) + value);
            } else {
                steelWeightTable.put(key, value);
            }
            if (steelWeightTable.containsKey(item.getSteelType())) {

            }
        }
    }

    // create all
    public static void createOrderTable(ArrayList<ArrayList<String>> records) throws NumberFormatException {
        int numberOfOrder = records.size() - 1;
        // int numberOfOrder = 10;
        createOrderList(numberOfOrder, records);
        createStandardGroupHashMap(numberOfOrder);
        createStandardHashMap(numberOfOrder);
    }

    // Step 1. sort order by big group
    public static void sortOrderListbyBigGroup() {
        ArrayList<Order> tempOrderList = new ArrayList<Order>();
        for (Map.Entry<Integer, ArrayList<Order>> item : standardGroupHashMap.entrySet()) {
            for (Order order : item.getValue()) {
                tempOrderList.add(order);
            }
        }
        orderList = tempOrderList;
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
        if (orderListAfterHeatingSystemSort.size() > 0) {
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

    // Step 2. random sort order list by heating system without changing big group
    // order
    public static void randomSortOrderListByHeatingSystem() {
        int lastHeatingSystem = 0;
        String SteelTypeInLastBigGroup = "";
        for (Map.Entry<Integer, ArrayList<Order>> item : standardGroupHashMap.entrySet()) {
            ArrayList<Order> localGroupOrderList = item.getValue();
            final int originalSize = localGroupOrderList.size();
            
            for (int i = 0; i < originalSize; i++) {
                int randomIndex = getValidRandomIndex(localGroupOrderList, lastHeatingSystem);

                //make sure the first order's steeltype is the same as that in last big group
                if(i == 0 && item.getKey()!=1){
                    for(int k=0; k<localGroupOrderList.size(); k++){
                        if(localGroupOrderList.get(k).getSteelType().equals(SteelTypeInLastBigGroup) ){
                            randomIndex = k;
                        }
                    }
                }

                orderListAfterHeatingSystemSort.add(localGroupOrderList.get(randomIndex));
                lastHeatingSystem = localGroupOrderList.get(randomIndex).getHeatingSystem();

                //record current Steel Type
                String currentSteelType = localGroupOrderList.get(randomIndex).getSteelType();

                //remove from local list
                localGroupOrderList.remove(randomIndex);

                //combine all orders with same steelType
                for(int j=0; j<localGroupOrderList.size(); j++){
                    if(localGroupOrderList.get(j).getSteelType().equals(currentSteelType)){
                        orderListAfterHeatingSystemSort.add(localGroupOrderList.get(j));
                        localGroupOrderList.remove(j);
                        j--;
                        i++;
                    }
                }
                SteelTypeInLastBigGroup = orderListAfterHeatingSystemSort.get(orderListAfterHeatingSystemSort.size()-1).getSteelType();
            }
        }
    }

    public static void printOrderList() {
        System.out.println("Order List:");
        for (Order order : orderList) {
            System.out.println(order.getInputNumber() + "\t "+ order.getGroup() + order.getStandardGroup() + "\t " + order.getStandard()
                    + "\t " + order.getSteelType() + "\t " + order.getHeatingSystem() + "\t " + order.getCuttingType());
        }
    }

    public static void printOrderListAfterHeatingSystemSort() {
        System.out.println("Order List After Heating System Sort:");
        for (Order order : orderListAfterHeatingSystemSort) {
            System.out.println(order.getInputNumber() + "\t "+ order.getGroup()+ "\t" + order.getStandardGroup() + "\t " + order.getStandard()
                    + "\t " + order.getSteelType() + "\t\t " + order.getHeatingSystem() + "\t " + order.getCuttingType());
        }
    }

    public static void printStandGroupHashMap() {
        for (Map.Entry<Integer, ArrayList<Order>> item : standardGroupHashMap.entrySet()) {
            System.out.print(item.getKey() + ":\t");
            for (int i = 0; i < item.getValue().size(); i++) {
                System.out.print(item.getValue().get(i).getInputNumber() + " ");
            }
            System.out.println(" ");
        }
    }

    public static void printStandHashMap() {
        for (Map.Entry<Float, ArrayList<Order>> item : standardHashMap.entrySet()) {
            System.out.print(item.getKey() + ":\t");
            for (int i = 0; i < item.getValue().size(); i++) {
                System.out.print(item.getValue().get(i).getInputNumber() + " ");
            }
            System.out.println(" ");
        }
    }

    public static void printSteelWeightTable() {
        System.out.println("OrderTable:");
        System.out.println("Steeltype\tTotal weight");
        for (Map.Entry<String, Float> item : steelWeightTable.entrySet()) {
            System.out.print(item.getKey() + ":\t\t");
            System.out.println(item.getValue());
        }
        System.out.println("------------------------------------");
    }

}