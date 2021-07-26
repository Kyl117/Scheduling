import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static int geneticAlgorithmIteration = 2500;
    public static void main(String[] args) throws IOException{

        FileReader orderfile = new FileReader();
        orderfile.loadfile("test.xls");
        OrderTable.createOrderTable(orderfile.getRecords());

        double idleTime = 0;
        OrderTable.sortOrderListbyBigGroup();
        ArrayList<Order> leastidleTimeSchedule = new ArrayList<Order>();
        for(int i=0; i<geneticAlgorithmIteration; i++){
            OrderTable.randomSortOrderListByHeatingSystem();
            double currentIdleTime = FitnessCalculator.calculateidleTime(OrderTable.orderListAfterHeatingSystemSort);

            if( i == 0 || currentIdleTime < idleTime){
                idleTime = currentIdleTime;
                leastidleTimeSchedule = OrderTable.orderListAfterHeatingSystemSort;
            }
        }
        OrderTable.orderListAfterHeatingSystemSort = leastidleTimeSchedule;
        System.out.println( "idle time:" + FitnessCalculator.calculateidleTime(OrderTable.orderListAfterHeatingSystemSort));
        
        
        OrderTable.printOrderListAfterHeatingSystemSort();


        //ouput 胚料需求計劃
        FileReader warehouse = new FileReader();
        warehouse.loadfile("warehouse.xls");
        WareHouse.createSteelWeightTable(warehouse.getRecords());
        OrderTable.createSteelWeightTable();
        DemandCalculator.calculateDemand();
        FileWriter.outputDemandTable(DemandCalculator.steelDemandTable);
    }
} 
