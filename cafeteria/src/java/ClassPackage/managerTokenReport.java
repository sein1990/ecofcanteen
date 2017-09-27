
package ClassPackage;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


public class managerTokenReport {
    
    
    
    public void dd(HttpServletResponse response){
          BufferedInputStream buf = null;
                        ServletOutputStream myOut = null;
                    try {
                        myOut = response.getOutputStream();             
                        File myfile = new File(DEST + pdfName);

                        //set response headers
                        response.setContentType("application/pdf");         //I want to download a PDF file

                        response.addHeader(
                                "Content-Disposition", "inline; filename=" + pdfName);

                        response.setContentLength((int) myfile.length());

                        FileInputStream input = new FileInputStream(myfile);
                        buf = new BufferedInputStream(input);
                        int readBytes = 0;
                        //read from the file; write to the ServletOutputStream
                        while ((readBytes = buf.read()) != -1) {
                            myOut.write(readBytes);
                        }

                    } catch (IOException ioe) {
                        String s=ioe.getMessage();
                    } finally {

                        //close the input/output streams
                        if (myOut != null) {
                            try {
                                myOut.close();
                            } catch (IOException ex) {
                                Logger.getLogger(managerTokenReport.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (buf != null) {
                            try {
                                buf.close();
                            } catch (IOException ex) {
                                Logger.getLogger(managerTokenReport.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                      }

    }
    
    
     public void managerReport(String fromDate, String toDate) throws IOException, DocumentException, SQLException, ParseException
    {
        
       // Vector<String> empVec = getEmployeeIds();
        Vector<managerReportItem> tokenData = getTokenData(fromDate,toDate);
        writeToPDF(tokenData, fromDate,toDate);
      
    }
    
    
       public Vector<managerReportItem> getTokenData(String fromDate, String toDate) throws ParseException{
        Vector<managerReportItem> TokenData=new Vector<managerReportItem>();
        int NormalLunch=0;
        int ManualLunch=0;
        int NormalDinner=0;
        int ManualDinner=0;
        
        int sum=0;
       String todaydate="";
        DateFormat df=new SimpleDateFormat("dd/mm/yy");
        
        Date startDate=df.parse(fromDate);
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
             
        Date endDate=df.parse(toDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        
        
        while(!start.after(end)){
            
            int year=start.get(Calendar.YEAR);
            int month=start.get(Calendar.MONTH)+1;
            int day=start.get(Calendar.DAY_OF_MONTH);
            
            String currentDate = df.format(start.getTime());       
            // SELECT count(`lunch`),count(`dinner`),count(`manual`) FROM `tokendetails` WHERE `dateRegister`>="12/03/17" && `dateRegister`<="14/04/17" 
            NormalLunch = getNumberFromDB("SELECT count(`lunch`) FROM `tokendetails` WHERE `dateRegister`='"+currentDate+"'  &&`manual`='0' && `lunch`='1'");
            ManualLunch = getNumberFromDB("SELECT count(`lunch`) FROM `tokendetails` WHERE `dateRegister`='"+currentDate+"'  d&&`manual`='1' && `lunch`='1'");
            NormalDinner = getNumberFromDB("SELECT count(`dinner`) FROM `tokendetails` WHERE `dateRegister`='"+currentDate+"'  &&`manual`='0' && `dinner`='1'");
            ManualDinner = getNumberFromDB("SELECT count(`dinner`) FROM `tokendetails` WHERE `dateRegister`='"+currentDate+"'  &&`manual`='1' && `dinner`='1'");
          sum=getNumberFromDB("SELECT SUM(`lunch`)FROM `tokendetails`");
            start.add(Calendar.DATE, 1);
            TokenData.add(new managerReportItem(currentDate, NormalLunch, ManualLunch, NormalDinner, ManualDinner)); 
        }
       return TokenData;
    }

 private int getNumberFromDB(String query){
        int res = 0;
        try {
            
            Connection con=dbconnection.getConnection();
            ResultSet resSet = null;
            PreparedStatement preSta=null;
            preSta=con.prepareStatement(query);
            resSet=preSta.executeQuery();
            while(resSet.next())
            {
                res= resSet.getInt(1);            
            }
              con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentReport.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      return res;  
        
    } 
 
 
 
  static final public String getMyDocPath(){
       
        String myDocuments = null;

            try {
                Process p =  Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
                p.waitFor();
                InputStream in = p.getInputStream();
                byte[] b = new byte[in.available()];
                in.read(b);
                in.close();
                myDocuments = new String(b);
                myDocuments = myDocuments.split("\\s\\s+")[4];
            } catch(Throwable t) {
                t.printStackTrace();
            }
       return myDocuments;      
   }
 
  
  private String DEST,pdfName;

    public String getDEST() {
        return DEST;
    }

    public String getPdfName() {
        return pdfName;
    }
  
    
    
  
 
 public void writeToPDF(Vector<managerReportItem> TokenData, String fromDate, String toDate){
    
         Font blueFont = FontFactory.getFont(FontFactory.HELVETICA,8, Font.BOLD);
        try {
            DEST =getMyDocPath()+"\\Canteen\\";
            pdfName="EMP.pdf";
            File fN=new File(DEST);
            fN.mkdir();
            
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            PdfWriter.getInstance(document, new FileOutputStream(DEST+pdfName));
            document.open();
           
            Paragraph p = new Paragraph("MANAGER MEALS REPORT", blueFont);
            p.setAlignment(Element.ALIGN_CENTER);         
            document.setMargins(1, 1, 1, 1);
            p.setSpacingBefore(1f);
            p.setSpacingAfter(1f);
            document.add(p);
            // first row
        
            Paragraph p2 = new Paragraph();
      
            p2.add(new Phrase("From:", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            p2.add(new Phrase(fromDate+"    ", FontFactory.getFont(FontFactory.HELVETICA,9)));
            p2.add(new Phrase("To:", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            p2.add(new Phrase(toDate+"    ", FontFactory.getFont(FontFactory.HELVETICA,9)));
           
           
            p2.add(new Phrase("Unit:", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            p2.add(new Phrase("ECOF"+"    ", FontFactory.getFont(FontFactory.HELVETICA,9)));
            p2.setAlignment(Element.ALIGN_CENTER); 
            document.add(p2);
            PdfPTable table = new PdfPTable(8);
            document.setMargins(1, 1, 1, 1);
            table.setWidthPercentage(100);
            table.setSpacingBefore(3f);
            table.setSpacingAfter(1f);
      
         
            table.addCell(new Phrase("Date", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase("Normal Lunch", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase("Manual Lunch", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase("Total Lunch", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase("Normal Dinner", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase("Manual Dinner", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase("Total Dinner", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));     
            table.addCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));     
            
         
            int totalnormalLunch=0;
            int totalmanualLunch=0;
            int totalLunch=0;
            int totalnormalDinner=0;
            int totalmanualDinner=0;
            int totalDinner=0;
            
            
            for(int i=0;i<TokenData.size();i++){
            table.addCell(new Phrase(TokenData.get(i).getTodayDate(), FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase(Integer.toString(TokenData.get(i).getNormalLunch()), FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase(Integer.toString(TokenData.get(i).getManualLunch()), FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase(Integer.toString(TokenData.get(i).getNormalLunch() + TokenData.get(i).getManualLunch()), FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase(Integer.toString(TokenData.get(i).getNormalDinner()), FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase(Integer.toString(TokenData.get(i).getManualDinner()), FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase(Integer.toString(TokenData.get(i).getNormalDinner() + TokenData.get(i).getManualDinner()), FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase(Integer.toString(TokenData.get(i).getNormalLunch()+
            TokenData.get(i).getNormalDinner() +  TokenData.get(i).getManualLunch()+TokenData.get(i).getManualDinner()), FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            totalnormalLunch=totalnormalLunch+TokenData.get(i).getNormalLunch();
            totalmanualLunch=totalmanualLunch+TokenData.get(i).getManualLunch();
            totalnormalDinner=totalnormalDinner+TokenData.get(i).getNormalDinner();
            totalmanualDinner=totalmanualDinner+TokenData.get(i).getManualDinner();   
            }
                totalLunch=totalLunch+TokenData.get(1).getNormalLunch() + TokenData.get(1).getManualLunch();
                totalDinner=totalDinner+TokenData.get(1).getManualLunch() + TokenData.get(1).getManualDinner();
                
                table.addCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
                table.addCell(new Phrase(""+totalnormalLunch, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
                table.addCell(new Phrase(""+totalmanualLunch, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
                int tl=totalnormalLunch+totalmanualLunch;
                table.addCell(new Phrase(""+tl, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
                table.addCell(new Phrase(""+totalnormalDinner, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
                table.addCell(new Phrase(""+totalmanualDinner, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
               
                int td=totalnormalDinner+totalmanualDinner;
                table.addCell(new Phrase(""+td, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD))); 
                int tdtlTotal=td+tl;
               
                table.addCell(new Phrase(""+tdtlTotal, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
                  
           
            document.add(table);
            document.close();
            //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+ pdfName);
        } catch (DocumentException | FileNotFoundException ex) {
            Logger.getLogger(managerTokenReport.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
    }
 }

