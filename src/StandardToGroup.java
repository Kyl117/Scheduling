
/**
 * This class is a helper class, only for converting standard to corresponding kocks group
 * 转化订单的规格到适当的kocks 组别（27组）
 */
public class StandardToGroup{
    private static final int totalGroup = 27;
    private static Float[] groupLimit = {
        15.90f,17.31f,18.61f,19.91f,21.62f,23.31f,25.0f, //0-6
        27.21f,29.41f,31.61f,34.45f,37.28f,40.11f,43.04f,//7-13
        45.96f,48.89f,51.81f,54.47f,57.12f,59.77f,62.42f,//14-20
        65.08f,67.58f,70.06f,72.55f,75.03f,77.53f,80.1f  //21-27
    };

    public static int convertTokocksGroup(float value) {
        int group = 0;
        for(int i = 0; i<totalGroup+1; i++){
            group = i;
            if(value < groupLimit[i]){
                break;
            }
        }
        if(group <= 0 || group >= totalGroup + 1){
            group = -1;
        }
        group = totalGroup + 1 - group;
        return group;
    }
}