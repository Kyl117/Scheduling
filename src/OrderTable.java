import java.util.ArrayList;
import java.util.Map;


import java.util.HashMap;


/**
 * This class is to group order objects.
 * After constructing all the order objects, put them all into a list and divide them into serveral groups for scheduling.
 * 为每一个订单建设了订单Object后，将所有订单写进一个列表里，并用Hashmap将订单Object按照特征（包括组别，规格）区分开不同组别 
 * 
 */
public class OrderTable {

    private static final int inputColOrderNmuber = 1;
    private static final int inputColSteelType = 8;
    private static final int inputColStandard = 12;
    private static final int inputColLength = 14;
    private static final int inputColWeight = 29;

    //-----------------For 坯料需求计划----------------------
    public static ArrayList<Order> orderList = new ArrayList<Order>();
    public static HashMap<String, Float> steelWeightTable = new HashMap<String, Float>();
    //------------------------------------------------------

    public static ArrayList<Order> scheduleAfterHeatingSystemSort = new ArrayList<Order>();
    public static ArrayList<Order> scheduleAfterCuttingTypeSort = new ArrayList<Order>();
    public static ArrayList<Order> scheduleAfterStandardSort = new ArrayList<Order>();

    //每个key为同一个kocks组
    public static HashMap<Integer, ArrayList<Order>> standardGroupHashMap = new HashMap<Integer, ArrayList<Order>>();
    
    //每个key为同一个规格
    public static HashMap<Float, ArrayList<Order>> standardHashMap = new HashMap<Float, ArrayList<Order>>();

    //每个key为同一个kocks组或加热制度
    public static ArrayList<ArrayList<Order>> kocksGroupHeatingSystemOrderList = new ArrayList<ArrayList<Order>>();

    //每个key为同一个kocks组或加热制度或切割方式
    public static ArrayList<ArrayList<Order>> kocksGroupHeatingSystemCuttingTypeOrderList = new ArrayList<ArrayList<Order>>();

    private static void createOrderList(int numberOfOrder, ArrayList<ArrayList<String>> records) {
        for (int i = 1; i < numberOfOrder + 1; i++) {
            OrderTable.orderList.add(new Order(OrderTable.orderList.size() + 1, records.get(i).get(inputColOrderNmuber),
                    Float.valueOf(records.get(i).get(inputColStandard)),
                    StandardToGroup.convertTokocksGroup(Float.valueOf(records.get(i).get(inputColStandard))), 99,
                    records.get(i).get(inputColSteelType), 101, Float.valueOf(records.get(i).get(inputColWeight)), 
                    Float.valueOf(records.get(i).get(inputColLength))));
        }
    }

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

    public static void createOrderTable(ArrayList<ArrayList<String>> records) throws NumberFormatException {
        int numberOfOrder = records.size() - 1;
        // int numberOfOrder = 4;
        createOrderList(numberOfOrder, records);
        createStandardGroupHashMap(numberOfOrder);
        createStandardHashMap(numberOfOrder);
    }

    public static void createkocksGroupHeatingSystemOrderList(){
        int lastHeatingSystem = scheduleAfterHeatingSystemSort.get(0).getHeatingSystem();
        int lastKocksGroup = scheduleAfterHeatingSystemSort.get(0).getStandardGroup();
        ArrayList<Order> currentOrderList= new ArrayList<Order>();
        for(Order item:scheduleAfterHeatingSystemSort){
            if(!(HeatingSystem.IsHeatingSystemValid(lastHeatingSystem, item.getHeatingSystem())) || lastKocksGroup != item.getStandardGroup()){   
                ArrayList<Order> copyOrderList= new ArrayList<Order>(currentOrderList);
                kocksGroupHeatingSystemOrderList.add(copyOrderList);
                currentOrderList.clear();
            }
            currentOrderList.add(item);  
            lastKocksGroup = item.getStandardGroup();
            lastHeatingSystem = item.getHeatingSystem();
            
        }
        kocksGroupHeatingSystemOrderList.add(currentOrderList);
    }

    public static void createkocksGroupHeatingSystemCuttingTypeOrderList(){
        int lastHeatingSystem = scheduleAfterCuttingTypeSort.get(0).getHeatingSystem();
        int lastKocksGroup = scheduleAfterCuttingTypeSort.get(0).getStandardGroup();
        Order.Cutting lastCuttingType = scheduleAfterCuttingTypeSort.get(0).getCuttingType();

        ArrayList<Order> currentOrderList= new ArrayList<Order>();
        for(Order item:scheduleAfterCuttingTypeSort){
            if(!(HeatingSystem.IsHeatingSystemValid(lastHeatingSystem, item.getHeatingSystem())) || lastKocksGroup != item.getStandardGroup() || lastCuttingType != item.getCuttingType()){   
                ArrayList<Order> copyOrderList= new ArrayList<Order>(currentOrderList);
                kocksGroupHeatingSystemCuttingTypeOrderList.add(copyOrderList);
                currentOrderList.clear();
            }
            currentOrderList.add(item);  
            lastKocksGroup = item.getStandardGroup();
            lastHeatingSystem = item.getHeatingSystem();
            lastCuttingType = item.getCuttingType();
            
        }
        kocksGroupHeatingSystemCuttingTypeOrderList.add(currentOrderList);
    }

    public static void printOrderList() {
        System.out.println("Order List:");
        for (Order order : orderList) {
            System.out.println(order.getInputNumber() + "\t "+ order.getGroup() +"\t" +order.getStandardGroup() + "\t " + order.getStandard()
                    + "\t " + order.getSteelType() + "\t " + order.getHeatingSystem() + "\t " + order.getCuttingType());
        }
    }

    public static void printOrderList(ArrayList<Order> orderList){
        for(Order order:orderList){
            System.out.print(order.getInputNumber()+" ");
        }
    }

    public static void printkocksGroupHeatingSystemorderlist(){
        for(ArrayList<Order> list:kocksGroupHeatingSystemOrderList){
            System.out.print(kocksGroupHeatingSystemOrderList.indexOf(list)+": ");
            for(Order order:list){
                System.out.print(order.getInputNumber()+" ");
            }
            System.out.println(" ");
        }
    }

    public static void printkocksGroupHeatingSystemCuttingTypeOrderList(){
        for(ArrayList<Order> list:kocksGroupHeatingSystemCuttingTypeOrderList){
            System.out.print(kocksGroupHeatingSystemCuttingTypeOrderList.indexOf(list)+": ");
            for(Order order:list){
                System.out.print(order.getInputNumber()+" ");
            }
            System.out.println(" ");
        }
    }

    public static void printOrderListAfterHeatingSystemSort() {
        System.out.println("Order List After Heating System Sort:");
        for (Order order : scheduleAfterHeatingSystemSort) {
            System.out.println(order.getInputNumber() + "\t "+ order.getGroup()+ "\t" + order.getStandardGroup() + "\t " + order.getStandard()
                    + "\t " + order.getSteelType() + "\t\t " + order.getHeatingSystem() + "\t " + order.getCuttingType());
        }
    }

    public static void printscheduleAfterCuttingTypeSort() {
        System.out.println("Schedule After Cutting Type Sort:");
        for (Order order : scheduleAfterCuttingTypeSort) {
            System.out.println(order.getInputNumber() + "\t "+ order.getGroup()+ "\t" + order.getStandardGroup() + "\t " + order.getStandard()
                    + "\t " + order.getSteelType() + "\t\t " + order.getHeatingSystem() + "\t " + order.getCuttingType());
        }
    }

    public static void printscheduleAfterStandardSort() {
        System.out.println("Schedule After Standard Sort:");
        for (Order order : scheduleAfterHeatingSystemSort) {
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