package Servlets.AlliesServlets;

import DecryptionManager.DM;
import Users.AgentUserManager;
import Users.AllUsersManager;
import Users.AllyUserManager;
import Users.UboatUserManager;
import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "removeAllyFromContest", urlPatterns = {"/removeAllyFromContest"})
public class RemoveAllyFromContestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String allyName = request.getParameter("allyName");
        String uboatName = ServletUtils.getAllUserManager(getServletContext()).getUboatFromAllyMap().get(allyName);

        AllUsersManager allUsersManager = ServletUtils.getAllUserManager(getServletContext());
        AllyUserManager allyUserManager = ServletUtils.getAllyUserManager(getServletContext());
        UboatUserManager uboatUserManager = ServletUtils.getUboatUserManager(getServletContext());
        AgentUserManager agentUserManager = ServletUtils.getAgentUserManager(getServletContext());

        // clearing the decryption candidates from ally
        allyUserManager.getAllyToDecryptionsCandidatesList().get(allyName).clear();
        // decreasing the number of registered allies from uboat
        if (uboatName != null) {
            uboatUserManager.getAllUboats().get(uboatName).decreaseNumOfRegisteredAllies();
            List<String> allies = allUsersManager.getAllUboatsAllies().get(uboatName);
            allies.forEach(ally -> allyUserManager.getAllyToDecryptionsCandidatesList().get(ally).clear());
            allUsersManager.removeAllyFromUboatToAllHisAllies(allyName, uboatName);
        }

        // removing ally from uboat
        allUsersManager.getUboatFromAllyMap().remove(allyName);
        // clearing all decryption candidates from allies

        // resetting agent number of missions done till now and number of candidates
//        allies.forEach(ally -> allUsersManager.getAllyToAllHisAgents().get(ally)
//                .forEach(agent -> { agentUserManager.getAgentToNumOfmissionsDoneTillNow()
//                        .put(agent.getAgentName(), 0);
//                    agentUserManager.getAgentToNumOfCandidates()
//                            .put(agent.getAgentName(), 0); }));
        // removing ally from uboat to all his allies

        // generating an empty new DM for ally
        try {
            DM dm = new DM(allyName);
            dm.setNumOfCurrentAgents(allUsersManager.getAllyToAllHisAgents().get(allyName).size()); //agentsList.size
            allyUserManager.addAllyWithDM(allyName, dm);

        } catch (CloneNotSupportedException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

}