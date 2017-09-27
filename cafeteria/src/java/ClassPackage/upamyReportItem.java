/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassPackage;

/**
 *
 * @author USER
 */
public class upamyReportItem {
    
    private String todayDate;
    private int upamyLunch;
    private int upamyDinner;
    //private int notEmpLunch;
    //private int notEmpDinner;
    

    public upamyReportItem(String todayDate, int upamyLunch, int upamyDinner) {
        this.todayDate = todayDate;
        this.upamyLunch = upamyLunch;
        this.upamyDinner = upamyDinner;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public int getUpamyLunch() {
        return upamyLunch;
    }

    public int getUpamyDinner() {
        return upamyDinner;
    }

    
    
}
