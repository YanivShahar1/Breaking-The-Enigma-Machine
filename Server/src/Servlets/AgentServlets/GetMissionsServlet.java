package Servlets.AgentServlets;

import DTO.ContestStatus;
import DTO.DTOContestData;
import DTO.DTOMission;
import DecryptionManager.MissionsQueue;
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

@WebServlet(name = "getMissionsServlet", urlPatterns = {"/getMissions"})
public class GetMissionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Gson gson = new Gson();
        String agentName = request.getParameter("agentName");
        String allyName = ServletUtils.getAllUserManager(getServletContext()).getAllyFromAgentMap().get(agentName);

        List<DTOMission> dtoMissions = new ArrayList<>();

        if (allyName != null) {
            String uboatName = ServletUtils.getAllUserManager(getServletContext()).getUboatFromAllyMap().get(allyName);
            if (uboatName == null) {
                try {
                    dtoMissions.add(new DTOMission(null, null, 0, null, 0, null, ContestStatus.UBOAT_LOGOUT));
                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            }
            else {
                MissionsQueue Q = ServletUtils.getAllyUserManager(getServletContext()).getAllAlliesDM().get(allyName).getMissionsQueue();

                int missionsPerRequest = ServletUtils.getAgentUserManager(getServletContext()).getAgentToNumOfMissionsPerRequest().get(agentName);





                for (int i = 0; i < missionsPerRequest; i++) {
                    try {


                        DTOMission d = Q.dequeue();
                        if (d != null) {
                            dtoMissions.add(d);
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }





                //ServletUtils.getAgentUserManager(getServletContext()).increaseNumOfMissionsDoneTillNow(agentName, dtoMissions.size());
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }

        else {

            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        String json = gson.toJson(dtoMissions);
        response.setContentType("text/plain;charset=UTF-8");


        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
    }
}
