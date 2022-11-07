package Servlets.AlliesServlets;

import DTO.*;
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

@WebServlet(name = "getAllDTOsToAlly", urlPatterns = {"/allDTOsToAlly"})
public class GetAllDTOsToAlly extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String allyName = request.getParameter("allyName");
        String uboatName = ServletUtils.getAllUserManager(getServletContext()).getUboatFromAllyMap().get(allyName);

        List<DTODecryptionCandidate> dtoDecryptionCandidateList = ServletUtils.getAllyUserManager(getServletContext()).getAllyToDecryptionsCandidatesList().get(allyName);


        List<DTOAgentsProgressData> dtoAgentsProgressDataList = new ArrayList<>();
        List<DTOAgentDetails> allAllyAgents = ServletUtils.getAllUserManager(getServletContext()).getAllyToAllHisAgents().get(allyName);
        for (DTOAgentDetails dtoAgentDetails : allAllyAgents) {
            String agentName = dtoAgentDetails.getAgentName();
//            int numOfCandidates = ServletUtils.getAgentUserManager(getServletContext()).getAgentToNumOfCandidates().get(agentName);
//            long numOfMissionsDoneTillNow = ServletUtils.getAgentUserManager(getServletContext()).getAgentToNumOfmissionsDoneTillNow().get(agentName);
//            long totalMissions = ServletUtils.getAllyUserManager(getServletContext()).getAllAlliesDM().get(allyName).getTrueNumOfMissions();
            dtoAgentsProgressDataList.add(ServletUtils.getAgentUserManager(getServletContext()).getAgentToProgressData().get(agentName));
        }

        DTOContestData dtoContestData = null;
        if (uboatName != null) {
            dtoContestData = ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).getContestData();
        }


        List<DTOAllyTeamDetails> dtoAllyTeamDetailsList = new ArrayList<>();
        if (uboatName != null) {
            List<String> allUboatAllies = ServletUtils.getAllUserManager(getServletContext()).getAllUboatsAllies().get(uboatName);
            for (String name : allUboatAllies) {
                if (!name.equals(allyName)) {
                    dtoAllyTeamDetailsList.add(ServletUtils.getAllyUserManager(getServletContext()).getAllAlliesDM().get(name).getAllyTeam());
                }
            }
        }


        DTOAllyContestTabAllData dtoAllyContestTabAllData = new DTOAllyContestTabAllData(

                dtoAgentsProgressDataList, dtoAllyTeamDetailsList, dtoContestData, dtoDecryptionCandidateList);



        Gson gson = new Gson();
        String json = gson.toJson(dtoAllyContestTabAllData);
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();

    }
}
