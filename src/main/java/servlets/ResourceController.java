package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Servlet to serve static resources from WEB-INF/css.
 * Since WEB-INF is protected, this servlet acts as a bridge.
 */
@WebServlet(name = "ResourceController", urlPatterns = { "/assets/css/*" })
public class ResourceController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Only allow CSS files for now as per requirement
        if (!pathInfo.endsWith(".css")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String resourcePath = "/WEB-INF/css" + pathInfo;
        try (InputStream is = getServletContext().getResourceAsStream(resourcePath)) {
            if (is == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.setContentType("text/css");

            byte[] buffer = new byte[4096];
            int bytesRead;
            OutputStream os = response.getOutputStream();
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}
