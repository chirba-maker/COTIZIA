package servlets;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Notification;
import models.Utilisateur;
import dao.NotificationDaoImplement;

/**
 * Controller for Notifications.
 * 
 * @author Major117
 */
public class NotificationController extends HttpServlet {

    private final NotificationDaoImplement notificationDAO = new NotificationDaoImplement();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        List<Notification> listNotif = notificationDAO.findByUser(user.getIdUtilisateur());
        request.setAttribute("listNotif", listNotif);
        request.getRequestDispatcher("/WEB-INF/views/listenotification.jsp").forward(request, response);
    }
}
