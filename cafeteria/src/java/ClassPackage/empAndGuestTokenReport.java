
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


public class empAndGuestTokenReport {
    
     public void ee(HttpServletResponse response){
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
                                Logger.getLogger(EmpTokenReport.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (buf != null) {
                            try {
                                buf.close();
                            } catch (IOException ex) {
                                Logger.getLogger(EmpTokenReport.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                      }
    
     }
     
    
    
    
     public void empGuestReport(String fromDate, String toDate) throws IOException, DocumentException, SQLException, ParseException
    {
        Vector<empAndGuestItem> tokenData = getTokenData(fromDate, toDate);
        writePDF(tokenData, fromDate, toDate);
    }
  
   public Vector<empAndGuestItem> getTokenData(String fromDate, String toDate) throws ParseException{
        Vector<empAndGuestItem> TokenData=new Vector<empAndGuestItem>();
        int empLunch=0;
        int notEmpLunch=0;
        int empDinner=0;
        int notEmpDinner=0;
 
  
       DateFormat df=new SimpleDateFormat("dd/MM/yy");
        
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
            empLunch = getNumberFromDB("SELECT count(`lunch`)FROM `tokendetails` WHERE `dateRegister`='"+currentDate+"' && `lunch`='1' && `reason`='Employee'");
         //   notEmpLunch = getNumberFromDB("SELECT count(`lunch`)FROM `tokendetails` WHERE `dateRegister`='"+currentDate+"' && `lunch`='1' && `reason`='Guest'");
         //   empDinner = getNumberFromDB("SELECT count(`dinner`) FROM `tokendetails`WHERE `dateRegister`='"+currentDate+"'  && `dinner`='1' && `reason`='Guest'");
            notEmpDinner = getNumberFromDB("SELECT count(`dinner`) FROM `tokendetails` WHERE `dateRegister`='"+currentDate+"'  && `dinner`='1' && `reason`='Employee'");
          
            TokenData.add(new empAndGuestItem(currentDate,empLunch, empDinner, notEmpLunch, notEmpDinner)); 
            start.add(Calendar.DATE, 1);
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
 
    private String DEST, pdfName;

    public String getDEST() {
        return DEST;
    }

    public String getPdfName() {
        return pdfName;
    }
    
    
    
        
      public void writePDF(Vector<empAndGuestItem> TokenData, String fromDate, String toDate){
    Font blueFont = FontFactory.getFont(FontFactory.HELVETICA,8, Font.BOLD);
        
        try { 
            DEST =getMyDocPath()+"\\Canteen\\";
            pdfName="dpt.pdf";
            File fN=new File(DEST);
            fN.mkdir();
             Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            PdfWriter.getInstance(document, new FileOutputStream(DEST+pdfName));
            document.open();
           
            Paragraph p = new Paragraph("EMPLOYEE AND NOT EMPLOYEE MEALS REPORT", blueFont);
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
            PdfPTable table = new PdfPTable(3);
            document.setMargins(1, 1, 1, 1);
            table.setWidthPercentage(100);
            table.setSpacingBefore(3f);
            table.setSpacingAfter(1f);
      
         
            table.addCell(new Phrase("Date", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase("Employee Total Lunch", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase("Employee Total Dinne", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
          //  table.addCell(new Phrase("Not Employee Total Lunch", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
          //  table.addCell(new Phrase("Not Employee Total Dinner", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
           
            
            
            
            int totalEmpLunch=0;
            int totalEmpDinner=0;
            int totalNotEmpLunch=0;
            int totalNotEmpDinner=0;
            for(int i=0;i<TokenData.size();i++){
                table.addCell(new Phrase(TokenData.get(i).getTodayDate(), FontFactory.getFont(FontFactory.HELVETICA,9)));
             
                table.addCell(new Phrase(Integer.toString(TokenData.get(i).getEmpLunch()), FontFactory.getFont(FontFactory.HELVETICA,9)));
                table.addCell(new Phrase(Integer.toString(TokenData.get(i).getEmpDinner()), FontFactory.getFont(FontFactory.HELVETICA,9)));
               // table.addCell(new Phrase(Integer.toString(TokenData.get(i).getNotEmpLunch()), FontFactory.getFont(FontFactory.HELVETICA,9)));
              //  table.addCell(new Phrase(Integer.toString(TokenData.get(i).getNotEmpDinner()), FontFactory.getFont(FontFactory.HELVETICA,9)));
            
                totalEmpLunch=totalEmpLunch+TokenData.get(i).getEmpLunch();
                totalEmpDinner=totalEmpDinner+TokenData.get(i).getEmpDinner();
              //  totalNotEmpLunch=totalNotEmpLunch+TokenData.get(i).getNotEmpLunch();
              //  totalNotEmpDinner=totalNotEmpDinner+TokenData.get(i).getNotEmpDinner();
                
            }
                table.addCell(new Phrase("TOTAL", FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
                table.addCell(new Phrase(""+totalEmpLunch, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
            table.addCell(new Phrase(""+totalEmpDinner, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
          //  table.addCell(new Phrase(""+totalNotEmpLunch, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
        //    table.addCell(new Phrase(""+totalNotEmpDinner, FontFactory.getFont(FontFactory.HELVETICA,7, Font.BOLD)));
         
            document.add(table);
            document.close();
           // Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+ DEST);
        } catch (DocumentException ex) {
            Logger.getLogger(empAndGuestTokenReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(empAndGuestTokenReport.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
    }  
     
     
}
