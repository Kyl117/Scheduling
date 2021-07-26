import java.util.ArrayList;

public class FitnessCalculator {
    private static double runningTime = 0; // in minutes
    private static double idleTime = 0; // in minutes
    private static double score = 0;

    public static double calculateRunningTime(ArrayList<Order> inputOrderTable) {
        // not finished
        return runningTime;
    }

    private static double getGroupSwitchTime(ArrayList<Order> inputOrderTable) {
        double totalSwitchTime = 0;
        int tableSize = inputOrderTable.size();
        for (int i = 1; i < tableSize; i++) {
            int standardGroupDifference = Math
                    .abs(inputOrderTable.get(i).getGroup() - inputOrderTable.get(i - 1).getGroup());
            if (standardGroupDifference > 0) {
                switch (standardGroupDifference) {
                    case 1:
                        totalSwitchTime += 30;
                        break;
                    case 2:
                        totalSwitchTime += 60;
                        break;
                    default:
                        totalSwitchTime += 200;
                }
            }
        }
        return totalSwitchTime;
    }

    private static double getCuttingTypeSwitchTime(ArrayList<Order> inputOrderTable) {
        double totalSwitchTime = 0;
        int tableSize = inputOrderTable.size();
        for (int i = 1; i < tableSize; i++) {
            if (inputOrderTable.get(i).getCuttingType() != inputOrderTable.get(i - 1).getCuttingType()) {
                totalSwitchTime += 15;
            }
        }
        return totalSwitchTime;
    }

    private static double getStandardSwitchTime(ArrayList<Order> inputOrderTable){
        double totalSwitchTime = 0;
        int tableSize = inputOrderTable.size();
        for(int i = 1; i < tableSize; i++){
            if(inputOrderTable.get(i).getStandard() != inputOrderTable.get(i - 1).getStandard()){
                totalSwitchTime += 5;
            }
        }
        return totalSwitchTime;
    }

    private static double getSteelTypeSwtichTime(ArrayList<Order> inputOrderTable){
        double totalSwitchTime = 0;
        int tableSize = inputOrderTable.size();
        for(int i = 1; i< tableSize; i++){
                if(!inputOrderTable.get(i).getSteelType().equals(inputOrderTable.get(i-1).getSteelType()))
                {
                    totalSwitchTime += 10;
                }
            }
        
        return totalSwitchTime;
    } 

    private static double getHeatingSystemSwitchTime(ArrayList<Order> inputOrderTable){
        double totalSwitchTime = 0;
        int tableSize = inputOrderTable.size();
        for(int i = 1; i< tableSize; i++){
            if(inputOrderTable.get(i).getHeatingSystem() != inputOrderTable.get(i - 1).getHeatingSystem()){
                totalSwitchTime += 500;
            }
        }
    
    return totalSwitchTime;
    }

    public static double calculateidleTime(ArrayList<Order> inputOrderTable) {
        // not implemented
        idleTime += getGroupSwitchTime(inputOrderTable);
        idleTime += getCuttingTypeSwitchTime(inputOrderTable);
        idleTime += getStandardSwitchTime(inputOrderTable);
        idleTime += getSteelTypeSwtichTime(inputOrderTable);
        // idleTime += getHeatingSystemSwitchTime(inputOrderTable);
        return idleTime;
    }

    public static double calculateScore(ArrayList<Order> inputOrderTable) {
        final double runningTimeRatio = 0.5;
        final double idleTimeRatio = 0.5;
        score = runningTimeRatio * calculateidleTime(inputOrderTable)
                + idleTimeRatio * 1 / calculateScore(inputOrderTable);
        return score;
    }

}
