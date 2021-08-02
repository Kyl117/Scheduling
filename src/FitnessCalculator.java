import java.util.ArrayList;

/**
 * The class to calculate the fitness score for genetic algorithm
 */
public class FitnessCalculator {

    public static double calculateRunningTime(ArrayList<Order> inputOrderTable) {
        // not finished
        double runningTime = 0;
        return runningTime;
    }

    /**
     * Calculate The total switching time of a schedule caused by steel rolling machine switching
     * 计算轧机切换的总时间
     * @param schedule The schedule of orders (轧制排程表)
     * @return The total switching time of a schedule caused by steel rolling machine switching(时间表内切换轧机的总时间)
     */
    private static double getGroupSwitchTime(ArrayList<Order> schedule) {
        double totalSwitchTime = 0;
        int tableSize = schedule.size();
        for (int i = 1; i < tableSize; i++) {
            int standardGroupDifference = Math
                    .abs(schedule.get(i).getGroup() - schedule.get(i - 1).getGroup());
            if (standardGroupDifference > 0) {
                switch (standardGroupDifference) {
                    case 1:
                        totalSwitchTime += 30;
                        break;
                    case 2:
                        totalSwitchTime += 60;
                        break;
                    default:
                        break;
                }
            }
        }
        return totalSwitchTime;
    }

    /**
     * Calculate The total switching time of a schedule caused by cutting type switching
     * 计算排程表中切割方式切换的总时间
     * @param inputschedule The schedule of production (轧制排程表)
     * @return The total switching time of a schedule caused by cutting type switching(时间表内切换切割方式的总时间)
     */
    private static double getCuttingTypeSwitchTime(ArrayList<Order> inputschedule) {
        double totalSwitchTime = 0;
        int tableSize = inputschedule.size();
        for (int i = 1; i < tableSize; i++) {
            if (inputschedule.get(i).getCuttingType() != inputschedule.get(i - 1).getCuttingType()) {
                totalSwitchTime += 15;
            }
        }
        return totalSwitchTime;
    }

    /**
     * Calculate The total switching time of a schedule caused by kocks switching
     * 计算排程表中kocks切换的总时间
     * @param inputschedule The schedule of production for calculating the total idle time
     * @return The total switching time of a schedule caused by kocks switching(排程表中切换kocks的总时间)
     */
    private static double getKocksSwitchTime(ArrayList<Order> inputschedule){
        double totalSwitchTime = 0;
        int tableSize = inputschedule.size();
        for(int i = 1; i < tableSize; i++){
            //kocks 切换时间
            if(inputschedule.get(i).getStandardGroup() != inputschedule.get(i - 1).getStandardGroup()){
                totalSwitchTime += 15;
            }

            //换冷床时间
            if(inputschedule.get(i).getStandardGroup() == 18 && inputschedule.get(i).getStandardGroup() == 19 ||
               inputschedule.get(i).getStandardGroup() == 19 && inputschedule.get(i).getStandardGroup() == 18 ){
                totalSwitchTime += 50;
            }
               
            
        }
        return totalSwitchTime;
    }

    /**
     * Calculate The total switching time of a schedule caused by standard switching
     * 计算排程表中规格切换的总时间
     * @param inputschedule The schedule of production for calculating the total idle time
     * @return The total switching time of a schedule caused by standard switching(排程表中切换规格的总时间)
     */
    private static double getStandardSwitchTime(ArrayList<Order> inputschedule){
        double totalSwitchTime = 0;
        int tableSize = inputschedule.size();
        for(int i = 1; i < tableSize; i++){
            if(inputschedule.get(i).getStandard() != inputschedule.get(i - 1).getStandard()){
                totalSwitchTime += 5;
            }
            
        }
        return totalSwitchTime;
    }

    /**
     * Calculate The total switching time of a schedule caused by steeltype switching
     * 计算排程表中钢种切换的总时间
     * @param inputschedule The schedule of production for calculating the total idle time
     * @return The total switching time of a schedule caused by steel type switching(排程表中切换钢种的总时间)
     */
    private static double getSteelTypeSwtichTime(ArrayList<Order> inputschedule){
        double totalSwitchTime = 0;
        int tableSize = inputschedule.size();
        for(int i = 1; i< tableSize; i++){
                if(!inputschedule.get(i).getSteelType().equals(inputschedule.get(i-1).getSteelType()))
                {
                    totalSwitchTime += 10;
                }
            }
        
        return totalSwitchTime;
    } 

    /**
     * Calculate The total idle time of the production schedule(including kocks switching time, standard switching time, cutting type switching time)
     * 计算排程表的总闲置时间
     * @param inputschedule The schedule of production for calculating the total idle time
     * @return the total idle time of the schedule
     */
    public static double calculateidleTime(ArrayList<Order> inputschedule) {
        double idleTime = 0;
        idleTime += getGroupSwitchTime(inputschedule);
        idleTime += getCuttingTypeSwitchTime(inputschedule);
        idleTime += getStandardSwitchTime(inputschedule);
        idleTime += getSteelTypeSwtichTime(inputschedule);
        idleTime += getKocksSwitchTime(inputschedule);
        // idleTime += getHeatingSystemSwitchTime(inputschedule);
        return idleTime;
    }

    /**
     * Calculate the fitness score of the input schedule.
     * @param inputschedule The schedule of production for calculating the fitness score
     * @return The fitness score
     */
    public static double calculateScore(ArrayList<Order> inputschedule) {
        double score = 0;
        final double runningTimeRatio = 0.5;
        final double idleTimeRatio = 0.5;
        score = runningTimeRatio * calculateidleTime(inputschedule)
                + idleTimeRatio * 1 / calculateScore(inputschedule);
        return score;
    }

}
