
package ClassPackage;


public class empAndGuestItem {

    
    private String todayDate;
    private int empLunch;
    private int empDinner;
    private int notEmpLunch;
    private int notEmpDinner;

    public empAndGuestItem(String todayDate, int empLunch, int empDinner, int notEmpLunch, int notEmpDinner) {
        this.todayDate = todayDate;
        this.empLunch = empLunch;
        this.empDinner = empDinner;
        this.notEmpLunch = notEmpLunch;
        this.notEmpDinner = notEmpDinner;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public int getEmpLunch() {
        return empLunch;
    }

    public int getEmpDinner() {
        return empDinner;
    }

    public int getNotEmpLunch() {
        return notEmpLunch;
    }

    public int getNotEmpDinner() {
        return notEmpDinner;
    }
  


   
}
