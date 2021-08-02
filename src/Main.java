import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{

        //input excel file
        FileReader orderfile = new FileReader();
        orderfile.loadfile("test.xls");
        OrderTable.createOrderTable(orderfile.getRecords());

        //ouput 胚料需求計劃

        FileReader warehouse = new FileReader();
        warehouse.loadfile("warehouse.xls");
        WareHouse.createSteelWeightTable(warehouse.getRecords());
        OrderTable.createSteelWeightTable();
        DemandCalculator.calculateDemand();
        FileWriter.outputDemandTable(DemandCalculator.steelDemandTable);

        //scheduling

        //Step1.根据大组顺序排列
        Scheduling.sortOrderListbyBigGroup();
        //Step2.根据加热制度排序
        Scheduling.geneticAlgorithmWithHeatingSystem();
        OrderTable.printOrderListAfterHeatingSystemSort();
        System.out.println( "idle time:" + FitnessCalculator.calculateidleTime(OrderTable.scheduleAfterHeatingSystemSort));
        //Step3. 根据切割方式排序
        OrderTable.createkocksGroupHeatingSystemOrderList();
        Scheduling.geneticAlgorithmWithCuttingType();
        OrderTable.printscheduleAfterCuttingTypeSort();
        System.out.println( "idle time:" + FitnessCalculator.calculateidleTime(OrderTable.scheduleAfterCuttingTypeSort));
        //Step4. 根据成品规格排序
        OrderTable.createkocksGroupHeatingSystemCuttingTypeOrderList();
        Scheduling.geneticAlgorithmWithStandard();
        OrderTable.printscheduleAfterStandardSort();
        System.out.println( "idle time:" + FitnessCalculator.calculateidleTime(OrderTable.scheduleAfterStandardSort));
 
        
        //ouput schedule
        FileWriter.outputSchedule(OrderTable.scheduleAfterCuttingTypeSort);


    }
}