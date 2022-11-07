package Servlets.UboatServlets;


import DTO.DTOAllyTeamDetails;
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

@WebServlet(name = "getUboatAllyTeam", urlPatterns = {"/uboatAllyTeamsDetails"})
public class GetAllyTeamsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String uboatName = request.getParameter("uboatName");


        List<DTOAllyTeamDetails> alliesTeamsDetailsList = new ArrayList<>();

        List<String> allies = ServletUtils.getAllUserManager(getServletContext()).getAllUboatsAllies().get(uboatName);

        if(!(allies == null) && !allies.isEmpty()) {
            allies.forEach(ally -> alliesTeamsDetailsList.add(ServletUtils.getAllyUserManager(getServletContext()).getAllAlliesDM().get(ally).getAllyTeam()));
        }
        String json = gson.toJson(alliesTeamsDetailsList);
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
    }
}

