package Servlets.UboatServlets;

import DTO.ContestStatus;
import DecryptionManager.DM;
import Session.SessionUtils;
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


@WebServlet(name = "logoutUboat", urlPatterns = {"/logoutUboat"})
public class UboatLogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
       // String usernameFromSession = SessionUtils.getUsername(request);
        UboatUserManager uboatUserManager = ServletUtils.getUboatUserManager(getServletContext());

       // if (usernameFromSession != null) {

            String uboatName = request.getParameter("uboatName");
            String battleFieldName = uboatUserManager.getAllUboats().get(uboatName).getBattleFieldName();
            uboatUserManager.getAllBattleFieldNames().remove(battleFieldName);
            AllUsersManager allUsersManager = ServletUtils.getAllUserManager(getServletContext());
            AllyUserManager allyUserManager = ServletUtils.getAllyUserManager(getServletContext());
            //AgentUserManager agentUserManager = ServletUtils.getAgentUserManager(getServletContext());

            uboatUserManager.getAllUboats().get(uboatName).setStatus(ContestStatus.UBOAT_LOGOUT);
            List<String> allies = allUsersManager.getAllUboatsAllies().get(uboatName);
            allies.forEach(ally -> {allyUserManager.getAllAlliesDM()
                    .get(ally).setContestStatus(ContestStatus.UBOAT_LOGOUT);
            ServletUtils.getAllUserManager(getServletContext()).getUboatFromAllyMap().put(ally, null);});

            SessionUtils.clearSession(request);
            uboatUserManager.removeUser(uboatName);
            allUsersManager.removeUser(uboatName);
            response.setStatus(HttpServletResponse.SC_OK);
      //  }

//        allies.forEach(ally -> allUsersManager.getAllyToAllHisAgents().get(ally)
//                .forEach(agent -> { agentUserManager.getAgentToNumOfmissionsDoneTillNow()
//                        .put(agent.getAgentName(), 0);
//                    agentUserManager.getAgentToNumOfCandidates()
//                            .put(agent.getAgentName(), 0); }));
//        allies.forEach(ally -> {allyUserManager.getAllyToDecryptionsCandidatesList().get(ally).clear();
//            allUsersManager.getUboatFromAllyMap().remove(ally);
//            allUsersManager.removeAllyFromUboatToAllHisAllies(ally, uboatName);
//            {
//            try {
//                DM dm = new DM(ally);
//                dm.setNumOfCurrentAgents(allUsersManager.getAllyToAllHisAgents().get(ally).size()); //agentsList.size
//                allyUserManager.addAllyWithDM(ally, dm);
//            } catch (CloneNotSupportedException | InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            }
//        });

    }

}
