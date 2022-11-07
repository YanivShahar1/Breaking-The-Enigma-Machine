package Servlets.UboatServlets;

import Utils.ServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getAllUboatsServlets", urlPatterns = {"/allUboats"})
public class GetAllUboatsServlets extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String json;
        try {

            Gson gson = new Gson();
            json = gson.toJson(ServletUtils.getUboatUserManager(getServletContext()).getAllUboats());
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            out.println(json);
            out.flush();
        } catch (/*CloneNotSupportedException | e*/ Exception e) {
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
            out.flush();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
