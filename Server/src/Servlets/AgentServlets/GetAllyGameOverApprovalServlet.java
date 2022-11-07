package Servlets.AgentServlets;

import DTO.*;
import Users.AgentUserManager;
import Utils.ServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "getAllyGameOverApproval", urlPatterns = {"/allyApproveGameOverAgent"})
public class GetAllyGameOverApprovalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String agentName = request.getParameter("agentName");
        AgentUserManager agentUserManager = ServletUtils.getAgentUserManager(getServletContext());
        Boolean approval = agentUserManager.getAgentToHisAllyApproval().get(agentName);
        if (approval) {
            agentUserManager.getAgentToHisAllyApproval().put(agentName, false);
        }
        Gson gson = new Gson();
        String json = gson.toJson(approval, Boolean.class);

        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
