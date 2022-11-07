package Servlets.UboatServlets;

import Utils.ServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getDictionary", urlPatterns = {"/dictionary"})
public class DictionaryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String uboatName = request.getParameter("uboatName");
        String json = gson.toJson(ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).getDictionary());
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
    }
}
