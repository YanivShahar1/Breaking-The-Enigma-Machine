package Servlets.AgentServlets;

import DTO.DTOAgentDetails;
import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@WebServlet(name = "logoutAgent", urlPatterns = {"/logoutAgent"})
public class AgentLogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String agentName = request.getParameter("agentName");
        String allyName = ServletUtils.getAllUserManager(getServletContext()).getAllyFromAgentMap().get(agentName);
        if (allyName != null) {
            List<DTOAgentDetails> agents = ServletUtils.getAllUserManager(getServletContext()).getAllyToAllHisAgents().get(allyName);
            DTOAgentDetails temp = null;
            for (DTOAgentDetails dtoAgentDetails : agents) {
                if (dtoAgentDetails.getAgentName().equals(agentName)) {
                    temp = dtoAgentDetails;
                    break;
                }
            }
            if (temp != null) {
                agents.remove(temp);
                ServletUtils.getAllyUserManager(getServletContext()).getAllAlliesDM().get(allyName).decreaseNumOfCurrentAgents();
            }
        }
        ServletUtils.getAgentUserManager(getServletContext()).removeUser(agentName);
        ServletUtils.getAllUserManager(getServletContext()).removeUser(agentName);
        ServletUtils.getAllUserManager(getServletContext()).getAllyFromAgentMap().remove(agentName);


        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }
}