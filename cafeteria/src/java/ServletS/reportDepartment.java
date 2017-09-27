/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServletS;

import ClassPackage.DepartmentReport;
import com.itextpdf.text.DocumentException;
import ClassPackage.managerTokenReport;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sein 90
 */
@WebServlet(name = "reportDepartment", urlPatterns = {"/reportDepartment"})
public class reportDepartment extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, DocumentException, SQLException {
      
        try{
            String empDepartment=request.getParameter("empUnityOne");
              response.setContentType("text/html;charset=UTF-8");
          
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = new Date();
            String s=request.getParameter("fromDate");
            fromDate = df.parse(s);
            DateFormat df1 = new SimpleDateFormat("dd/MM/yy");
            Calendar start = Calendar.getInstance();
            start.setTime(fromDate);
            Date toDate = new Date();
            String ss=request.getParameter("toDate");
            toDate = df.parse(ss);
            DepartmentReport file = new DepartmentReport();
            file.empDepartmentReport(empDepartment, df1.format(fromDate), df1.format(toDate));
            String DEST = file.getDEST();
            String pdfName = file.getPdfName();
            file.pp(response);
        }
        catch (ParseException ex) {
            Logger.getLogger(reportDepartment.class.getName()).log(Level.SEVERE, null, ex);
        } 
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet reportDepartment</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet reportDepartment at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(reportDepartment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(reportDepartment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(reportDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(reportDepartment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(reportDepartment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(reportDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
