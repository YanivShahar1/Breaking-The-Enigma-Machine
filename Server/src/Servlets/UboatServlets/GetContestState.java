package Servlets.UboatServlets;

import DTO.ContestStatus;
import Utils.ServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getContestState", urlPatterns = {"/getContestState"})
public class GetContestState extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uboatName = request.getParameter("uboatName");
        Gson gson = new Gson();
        ContestStatus contestStatus = ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).getStatus();
        String json = gson.toJson(contestStatus, ContestStatus.class);
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
    }
}