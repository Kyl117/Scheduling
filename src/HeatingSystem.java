
public class HeatingSystem {
    /**
     * Check whether the two subsequent orders can be continously produced or not by heating system
     * 按照加热制度检查两个订单能否连续生产
     * @param lastHeatingSystem The first order's heating system(第一张订单的加热制度) 
     * @param thisHeatingSystem The second order's heating system(第二张订单的加热制度) 
     * @return Whether the two orders can be continuously produced or not 
     */
    public static boolean IsHeatingSystemValid(int lastHeatingSystem, int thisHeatingSystem){
        if((lastHeatingSystem == 2 && thisHeatingSystem == 3)
        || (lastHeatingSystem == 3 && thisHeatingSystem == 2)
        || (lastHeatingSystem == 3 && thisHeatingSystem == 4)
        || (lastHeatingSystem == 4 && thisHeatingSystem == 3)){
            return false;
        }
        return true;
    }
}
