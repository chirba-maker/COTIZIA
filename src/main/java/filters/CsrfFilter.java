package filters;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filter for CSRF Protection.
 * Generates a token and validates it for all non-safe methods (POST, PUT,
 * DELETE).
 * 
 * @author Antigravity
 */
@WebFilter("/*")
public class CsrfFilter implements Filter {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        // 1. Ensure token exists in session
        String token = (String) session.getAttribute("csrfToken");
        if (token == null) {
            byte[] buffer = new byte[32];
            secureRandom.nextBytes(buffer);
            token = Base64.getEncoder().encodeToString(buffer);
            session.setAttribute("csrfToken", token);
        }

        // 2. Validate token on POST requests
        String method = httpRequest.getMethod();
        if ("POST".equalsIgnoreCase(method)) {
            String requestToken = httpRequest.getParameter("csrfToken");
            if (requestToken == null || !requestToken.equals(token)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF Token Validation Failed");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
