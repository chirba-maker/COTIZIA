package servlets;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Mouchard;
import dao.MouchardDaoImplement;

/**
 * Controller for Audit Logs (Mouchard).
 * 
 * @author Major117
 */
public class MouchardController extends HttpServlet {

    private final MouchardDaoImplement mouchardDAO = new MouchardDaoImplement();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        models.Utilisateur user = (models.Utilisateur) request.getSession().getAttribute("user");
        if (user == null || user.getRole() != models.Utilisateur.Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        List<Mouchard> listLogs = mouchardDAO.getAll();
        request.setAttribute("listLogs", listLogs);
        request.getRequestDispatcher("/WEB-INF/views/listemouchard.jsp").forward(request, response);
    }
}
