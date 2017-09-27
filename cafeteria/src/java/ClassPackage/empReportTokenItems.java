
package ClassPackage;

public class empReportTokenItems 
{
    
    private String empID;
    private String currentDate;
    private int lunch;
    private int dinner;

    public empReportTokenItems(String empID, String date, int lunch, int dinner) {
        this.empID = empID;
        this.currentDate = date;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public String getEmpID() {
        return empID;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public int getLunch() {
        return lunch;
    }

    public int getDinner() {
        return dinner;
    }
   
    
    
    
    
    
}
