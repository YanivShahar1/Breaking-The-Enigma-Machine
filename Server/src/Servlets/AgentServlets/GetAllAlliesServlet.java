package Servlets.AgentServlets;

import DTO.DTOContestData;
import SystemEngine.SystemEngine;
import Utils.ServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "getAllAllies", urlPatterns = {"/getAllAllies"})
public class GetAllAlliesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        Set<String> alliesSet = ServletUtils.getAllyUserManager(getServletContext()).getUsers();

        String json = gson.toJson(alliesSet);
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
    }
}