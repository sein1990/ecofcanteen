package ClassPackage;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
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


public class EmpTokenReport {

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
    
     Vector<empReportTokenItems> TokenData;
      public void empReportToken(String empID, String fromDate, String toDate) throws IOException, DocumentException, SQLException, ParseException
    {

     //   Vector<String> notempVec = getNotEmployeeId(fromDate, toDate);
        getTokenData(empID, fromDate,toDate);
        writePDF(fromDate, toDate);
    }
 
     public void getTokenData(String empID, String fromDate, String toDate) throws ParseException{
     TokenData=new Vector<empReportTokenItems>();
        int lunch;
        int  dinner;
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
            getGuestLunchData(currentDate, empID);
            start.add(Calendar.DATE, 1);
        
        }       
        }
    
       
 private void getGuestLunchData(String currentDate, String empID){
       String query="SELECT `empID`,`lunch`, `dinner` FROM `tokendetails` WHERE `dateRegister`='"+currentDate+"' && `empID`='"+empID+"'";
       try {
            Connection con=dbconnection.getConnection();
            ResultSet resSet = null;
            PreparedStatement preSta=null;
            preSta=con.prepareStatement(query);
            resSet=preSta.executeQuery();
            while(resSet.next())
            {
                TokenData.add(new empReportTokenItems(currentDate, resSet.getString(1), resSet.getInt(2), resSet.getInt(3)));
            }
              con.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmpTokenReport.class.getName()).log(Level.SEVERE, null, ex);
        }  
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
       public void writePDF(String fromDate, String toDate){
    
        Font blueFont = FontFactory.getFont(FontFactory.HELVETICA,8, Font.BOLD);
        //Font redFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new CMYKColor(0, 255, 20, 20));
        //Font yellowFont = FontFactory.getFont(FontFactory.COURIER, 9, Font.BOLD, new CMYKColor(0, 0, 255, 0));
      
        try { 
            DEST =getMyDocPath()+"\\Canteen\\";
            pdfName="emp.pdf";
            File fN=new File(DEST);
            fN.mkdir();
            
            
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            PdfWriter.getInstance(document, new FileOutputStream(DEST+pdfName));
            document.open();
           
            Paragraph p = new Paragraph("EMPLOYEE MEALS REPORT", blueFont);
            p.setAlignment(Element.ALIGN_CENTER);         
            document.setMargins(1, 1, 1, 1);
            p.setSpacingBefore(1f);
            p.setSpacingAfter(1f);
            document.add(p);
            Paragraph p2 = new Paragraph();
            p2.add(new Phrase("Emp Name:", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            p2.add(new Phrase("  ", FontFactory.getFont(FontFactory.HELVETICA,9)));
            p2.add(new Phrase("From:", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            p2.add(new Phrase(fromDate+"    ", FontFactory.getFont(FontFactory.HELVETICA,9)));
            p2.add(new Phrase("To:", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            p2.add(new Phrase(toDate+"    ", FontFactory.getFont(FontFactory.HELVETICA,9)));
            p2.add(new Phrase("Department:", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            p2.add(new Phrase("IT"+"    ", FontFactory.getFont(FontFactory.HELVETICA,9)));           
            p2.add(new Phrase("Unity:", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            p2.add(new Phrase("ECOF"+"    ", FontFactory.getFont(FontFactory.HELVETICA,9)));
            p2.setAlignment(Element.ALIGN_CENTER); 
            document.add(p2);
         
          
            PdfPTable table = new PdfPTable(4);
            document.setMargins(1, 1, 1, 1);
            table.setWidthPercentage(100);
            table.setSpacingBefore(3f);
            table.setSpacingAfter(1f);
            table.addCell(new Phrase("emp ID", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            table.addCell(new Phrase("Date", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            table.addCell(new Phrase("Lunch", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));
            table.addCell(new Phrase("Dinner", FontFactory.getFont(FontFactory.HELVETICA,9, Font.BOLD)));

                 
            int totallunch=0;
            int totaldinner=0;
            for(int i=0;i<TokenData.size();i++)
                 {
                table.addCell(new Phrase(TokenData.get(i).getCurrentDate(), FontFactory.getFont(FontFactory.HELVETICA,7)));
                  table.addCell(new Phrase(TokenData.get(i).getEmpID(), FontFactory.getFont(FontFactory.HELVETICA,7)));  
     
       
              String lunchTaken=Integer.toString(TokenData.get(i).getLunch());
               String dinnerTaken=Integer.toString(TokenData.get(i).getDinner());
                 if(TokenData.get(i).getLunch()==1)
            {
              lunchTaken="Taken";
              totallunch=totallunch+1;
              
            }
            else 
            {
                 lunchTaken="Not Taken";
            }
               
              // table.addCell(Integer.toString(TokenData.get(i).getDinner()));
                 if(TokenData.get(i).getDinner()==1)
            {
                dinnerTaken="Taken";
                totaldinner=totaldinner+1;  
            }
            else
            {
                 dinnerTaken="Not Taken";
            }   
             
                table.addCell(new Phrase(lunchTaken, FontFactory.getFont(FontFactory.HELVETICA,7)));
                table.addCell(new Phrase(dinnerTaken, FontFactory.getFont(FontFactory.HELVETICA,7)));
         
               
                 }
                    PdfPCell cellTotal = new PdfPCell(new Phrase("Total :  ", FontFactory.getFont(FontFactory.HELVETICA,8, Font.BOLD)));
                    cellTotal.setColspan(2);
                    table.addCell(cellTotal);


                    cellTotal = new PdfPCell(new Phrase(""+totallunch, FontFactory.getFont(FontFactory.HELVETICA,8, Font.BOLD)));
                    table.addCell(cellTotal);  

                    cellTotal = new PdfPCell(new Phrase(""+totaldinner, FontFactory.getFont(FontFactory.HELVETICA,8, Font.BOLD)));
                    table.addCell(cellTotal);  

                    document.add(table);
                    document.close();
          //  Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+ DEST);
        } catch (DocumentException | FileNotFoundException ex) {
            Logger.getLogger(EmpTokenReport.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
    }  

}

    
