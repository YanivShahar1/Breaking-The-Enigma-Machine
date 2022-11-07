package Servlets.AlliesServlets;

import DTO.ContestStatus;
import DTO.DTOAgentDetails;
import Users.AllUsersManager;
import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "allyLogout", urlPatterns = {"/logoutAlly"})
public class AllyLogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AllUsersManager allUsersManager = ServletUtils.getAllUserManager(getServletContext());

        String allyName = request.getParameter("allyName");
        String uboatName = ServletUtils.getAllUserManager(getServletContext()).getUboatFromAllyMap().get(allyName);
//        List<DTOAgentDetails> agents = ServletUtils.getAllUserManager(getServletContext()).getAllyToAllHisAgents().get(allyName);
//        agents.forEach(agent -> {ServletUtils.getAgentUserManager(getServletContext())
//                .removeUser(agent.getAgentName());
//                        allUsersManager.getAllyFromAgentMap().remove(agent.getAgentName());});
//        allUsersManager.getAllyToAllHisAgents().remove(allyName);
        List<DTOAgentDetails> agents = ServletUtils.getAllUserManager(getServletContext()).getAllyToAllHisAgents().get(allyName);
        agents.forEach(agent -> ServletUtils.getAllUserManager(getServletContext()).getAllyFromAgentMap().put(agent.getAgentName(), null));
        ServletUtils.getAllUserManager(getServletContext()).getAllyToAllHisAgents().remove(allyName);

       // allUsersManager.setupAllyListener(allyName);
        ServletUtils.getAllyUserManager(getServletContext()).removeUser(allyName);
        ServletUtils.getAllUserManager(getServletContext()).removeUser(allyName);
        allUsersManager.getUboatFromAllyMap().remove(allyName);


        if (uboatName != null) {
            //ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).setStatus(ContestStatus.LOGOUT);
            List<String> allies = ServletUtils.getAllUserManager(getServletContext()).getAllUboatsAllies().get(uboatName);
            allies.remove(allyName);
            ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).setNumOfRegisteredAliies(allies.size());
            if (allies.isEmpty() && ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).getStatus() == ContestStatus.RUNNING) {
                ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).setStatus(ContestStatus.ALLY_LOGOUT);
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

}