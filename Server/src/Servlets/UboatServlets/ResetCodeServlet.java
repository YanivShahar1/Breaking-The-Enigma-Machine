package Servlets.UboatServlets;

import SystemEngine.SystemEngine;
import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "resetCodeConfiguration", urlPatterns = {"/resetCode"})
public class ResetCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uboatName = request.getParameter("uboatName");
        SystemEngine systemEngine = ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName);
        try {
            systemEngine.resetCodeConfiguration();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (CloneNotSupportedException e) {
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
            out.flush();
        }
    }
}
