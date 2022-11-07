package Servlets.AlliesServlets;


import DTO.DTOAgentDetails;
import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "allyApprovedGameOverAlly", urlPatterns = {"/allyApprovedGameOverAlly"})
public class GetAllyGameOverApproval extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String allyName = request.getParameter("allyName");
        List<DTOAgentDetails> allAgents = ServletUtils.getAllUserManager(getServletContext()).getAllyToAllHisAgents().get(allyName);
        allAgents.forEach(agent -> {ServletUtils.getAgentUserManager(getServletContext())
                .getAgentToHisAllyApproval().put(agent.getAgentName(), true);
        ServletUtils.getAgentUserManager(getServletContext()).getAgentToProgressData().get(agent.getAgentName()).reset();});

        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}