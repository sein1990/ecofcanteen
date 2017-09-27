/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServletS;

import com.itextpdf.text.DocumentException;
import ClassPackage.managerTokenReport;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
@WebServlet(name = "mngrReport", urlPatterns = {"/mngrReport"})
public class mngrReport extends HttpServlet {

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
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");

            String sss="ff";
            String empID=request.getParameter("empID");
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
            managerTokenReport fi = new managerTokenReport();
            fi.managerReport(df1.format(fromDate), df1.format(toDate));
            String DEST = fi.getDEST();
            String pdfName = fi.getPdfName();
            fi.dd(response);
            
            
            
//            try (PrintWriter out = response.getWriter()) {
//                /* TODO output your page here. You may use following sample code. */
//                out.println("<!DOCTYPE html>");
//                out.println("<html>");
//                out.println("<head>");
//                out.println("<title>Servlet mngrReport</title>");
//                out.println("</head>");
//                out.println("<body>");
//                out.println("<h1>Servlet mngrReport at " + request.getContextPath() + "</h1>");
//                out.println(request.getParameter("fromDate"));
//                out.println(request.getParameter("toDate"));
//                
//                out.println("</body>");
//                out.println("</html>");
//            }
        }   catch (ParseException ex) {
            Logger.getLogger(mngrReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(mngrReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(mngrReport.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
