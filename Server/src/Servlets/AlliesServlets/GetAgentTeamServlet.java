package Servlets.AlliesServlets;


import DTO.DTOAgentDetails;
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

@WebServlet(name = "getAgentDetails", urlPatterns = {"/agentTeamDetails"})
public class GetAgentTeamServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {



        Gson gson = new Gson();
        String clientType = request.getParameter("clientType");
        String allyName, agentName;
        if (clientType.equals("agent")) {
            agentName = request.getParameter("clientName");
            allyName = ServletUtils.getAllUserManager(getServletContext()).getAllyFromAgentMap().get(agentName);
        }
        else {
            allyName = request.getParameter("clientName");
        }

        List<DTOAgentDetails> alliesTeamsDetailsList = ServletUtils.getAllUserManager(getServletContext()).getAllyToAllHisAgents().get(allyName);
        String json = "";
        if (alliesTeamsDetailsList != null && !alliesTeamsDetailsList.isEmpty()){
            json = gson.toJson(alliesTeamsDetailsList);
        }
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
    }
}

