package Servlets.AgentServlets;

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

@WebServlet(name = "getAllDTOsToAgent", urlPatterns = {"/allDTOsToAgent"})
public class GetAllDTOsToAgent extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String agentName = request.getParameter("agentName");
        String allyName = ServletUtils.getAllUserManager(getServletContext()).getAllyFromAgentMap().get(agentName);
        if (allyName == null) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else {
            String uboatName = ServletUtils.getAllUserManager(getServletContext()).getUboatFromAllyMap().get(allyName);
            if (uboatName==null){
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            }
            else {
                ContestStatus contestStatus = ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).getStatus();
                List<DTOAgentDetails> allAllyAgents = ServletUtils.getAllUserManager(getServletContext()).getAllyToAllHisAgents().get(allyName);

                DTOContestData dtoContestData = ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).getContestData();

                DTOAgentContestAllData dtoAgentContestAllData = new DTOAgentContestAllData(
                        dtoContestData, allAllyAgents, contestStatus);
                Gson gson = new Gson();
                String json = gson.toJson(dtoAgentContestAllData);
                response.setContentType("text/plain;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.println(json);
                out.flush();
            }
        }
    }

}
