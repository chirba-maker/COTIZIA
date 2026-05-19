package filters;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filter to protect pages and ensure authentication.
 * 
 * @author Major117
 */
@WebFilter(urlPatterns = { "/*" })
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String loginURI = httpRequest.getContextPath() + "/connexion";
        String resourcesURI = httpRequest.getContextPath() + "/resources"; // Static files
        String assetsURI = httpRequest.getContextPath() + "/assets"; // CSS/JS assets

        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        boolean loginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean resourceRequest = httpRequest.getRequestURI().startsWith(resourcesURI)
                || httpRequest.getRequestURI().startsWith(assetsURI);

        if (loginRequest || resourceRequest) {
            chain.doFilter(request, response);
            return;
        }

        if (loggedIn) {
            models.Utilisateur user = (models.Utilisateur) session.getAttribute("user");
            String uri = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

            // Role-based authorization
            boolean authorized = false;
            switch (user.getRole()) {
                case ADMIN:
                    authorized = true; // Admin sees everything
                    break;
                case COLLECTEUR:
                    authorized = uri.equals("/dashboard") || uri.startsWith("/cycles")
                            || uri.startsWith("/adherents") || uri.startsWith("/paiements")
                            || uri.startsWith("/echeances") || uri.startsWith("/participant")
                            || uri.startsWith("/notifications") || uri.equals("/")
                            || uri.equals("/logout") || uri.equals("/profile");
                    break;
                case CONSULTANT:
                    authorized = uri.equals("/dashboard") || uri.startsWith("/adherents")
                            || uri.startsWith("/cycles") || uri.startsWith("/participant")
                            || uri.startsWith("/echeances") || uri.startsWith("/paiements")
                            || uri.equals("/") || uri.equals("/logout") || uri.equals("/profile");
                    break;
            }

            if (authorized) {
                chain.doFilter(request, response);
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard");
            }
        } else {
            httpResponse.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
    }
}
