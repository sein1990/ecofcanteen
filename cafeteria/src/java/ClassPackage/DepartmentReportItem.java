/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassPackage;

/**
 *
 * @author Sein 90
 */
public class DepartmentReportItem {
    
    private String FromeDate;
    private String ToDate;
    private String Empd;
    private int NormalLunch;
    private int MenualLunch;
    private int NormalDinner;
    private int MenualDinner;

    public DepartmentReportItem(String FromeDate, String ToDate, String Empd, int NormalLunch, int MenualLunch, int NormalDinner, int MenualDinner) {
        this.FromeDate = FromeDate;
        this.ToDate = ToDate;
        this.Empd = Empd;
        this.NormalLunch = NormalLunch;
        this.MenualLunch = MenualLunch;
        this.NormalDinner = NormalDinner;
        this.MenualDinner = MenualDinner;
    }

    public String getFromeDate() {
        return FromeDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public String getEmpd() {
        return Empd;
    }

    public int getNormalLunch() {
        return NormalLunch;
    }

    public int getMenualLunch() {
        return MenualLunch;
    }

    public int getNormalDinner() {
        return NormalDinner;
    }

    public int getMenualDinner() {
        return MenualDinner;
    }
   
    
    
}
