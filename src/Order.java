public class Order{
    private final int inputNumber;
    private final String orderNumber;
    private final float standard;
    private final int standardGroup;
    private final int group;
    private final int heatingSystem;
    private final String steelType;
    private final float weight;

    public static enum Cutting{
        HOLE, SAW;
    }
    private final Cutting cuttingType;

    private int getGroupFromStandardGroup(int standard){
        if(standard <= 6){
            return 1;
        }else if(standard <= 11){
            return 2;
        }else if(standard <= 15){
            return 3;
        }else if(standard <= 18){
            return 4;
        }else if(standard <= 21){
            return 5;
        }else if(standard <= 24){
            return 6;
        }
        return 7;
    }
    public Order(final int inputNumber, final String orderNumber,final float standard,final int standardGroup,
                    final int heatingSystem, final String steelType, final int cuttingType, final float weight){
        this.inputNumber = inputNumber;
        this.orderNumber = orderNumber;
        this.standard = standard;
        this.standardGroup = standardGroup;
        this.group = getGroupFromStandardGroup(standardGroup);
        this.steelType = steelType;

        if(steelType.equals("60Si2MnAZL")  || steelType.equals("45#") || steelType.equals("100Cr6-1")) {this.heatingSystem = 1;}
        else if(steelType.equals("S45C-ex")  || steelType.equals("40Cr-XLY") ||steelType.equals("S45SC-1497")){this.heatingSystem = 2;}
        else if(steelType.equals("A105")  || steelType.equals("42CrMo-bar3077")  || steelType.equals("50CrVA-1264")){this.heatingSystem = 3;}
        else{this.heatingSystem = 4;}


        //set cutting type
        if(steelType.equals("60Si2MnAZL") || steelType.equals("A105") || steelType.equals("GCr15") || 
            steelType.equals("S45C-ex") || steelType.equals("K50") || steelType.equals("15B36Cr") || 
            steelType.equals("11SMn30") || steelType.equals("30MnVS") || steelType.equals("S45SC-1497") ||
            steelType.equals("SUP9DZL") || steelType.equals("M3") || steelType.equals("25MnVK") || 
            steelType.equals("42CrMo-bar3077") || steelType.equals("40Cr-bar3077")){
            this.cuttingType = Cutting.SAW; 
        }
        else{
            this.cuttingType = Cutting.HOLE;
        }
        this.weight = weight;

    }
    
    public final String getorderNumber(){return orderNumber;}
    public final float getStandard(){return standard;}
    public final int getStandardGroup(){return standardGroup;}
    public final int getGroup(){return group;}
    public final int getHeatingSystem(){return heatingSystem;}
    public final String getSteelType(){return steelType;}
    public final Cutting getCuttingType (){return cuttingType;}
    public final float getWeight(){return weight;}


    public void print(){
        System.out.println("------------------------------------");
        System.out.println(inputNumber);
        System.out.println("orderNumber: "+orderNumber);
        System.out.println("standard: "+standard);
        System.out.println("standard Group: "+standardGroup);
        System.out.println("heating System: "+heatingSystem);
        System.out.println("steel Type: "+steelType);
        System.out.println("cutting Type: "+cuttingType);
        System.out.println("weight: "+weight);
        System.out.println("------------------------------------");
    }

    public int getInputNumber(){
        return inputNumber;
    }
}