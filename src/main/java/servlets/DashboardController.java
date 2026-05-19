package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Utilisateur;

/**
 * Controller for the Dashboard.
 * 
 * @author Major117
 */

public class DashboardController extends HttpServlet {

    private final services.DashboardService dashboardService = new services.DashboardService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        java.util.Map<String, Object> stats = dashboardService.getStats(user);
        request.setAttribute("stats", stats);

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
