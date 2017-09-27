
package ClassPackage;

public class managerReportItem {

    private String todayDate;
   
    private int NormalLunch;
    private int ManualLunch;
    private int NormalDinner;
    private int ManualDinner;

    public managerReportItem(String todayDate, int NormalLunch, int ManualLunch, int NormalDinner, int ManualDinner) {
        this.todayDate = todayDate;
        this.NormalLunch = NormalLunch;
        this.ManualLunch = ManualLunch;
        this.NormalDinner = NormalDinner;
        this.ManualDinner = ManualDinner;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public int getNormalLunch() {
        return NormalLunch;
    }

    public int getManualLunch() {
        return ManualLunch;
    }

    public int getNormalDinner() {
        return NormalDinner;
    }

    public int getManualDinner() {
        return ManualDinner;
    }
    
  
    
     
    
}
