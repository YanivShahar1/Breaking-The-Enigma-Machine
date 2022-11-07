package Servlets.AgentServlets;

import DTO.DTOAgentsProgressData;
import DTO.DTODecryptionCandidate;
import DTO.ContestStatus;
import Utils.ServletUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@WebServlet(name = "updateAgentProgressData", urlPatterns = {"/updateAgentProgressData"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024)
public class UpdateAgentProgressData extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String agentName = request.getParameter("agentName");
        Gson gson = new Gson();
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        String json = jb.toString();


        Type type = new TypeToken<DTOAgentsProgressData>() {}.getType();
        DTOAgentsProgressData dtoAgentsProgressData = gson.fromJson(json, type);

        ServletUtils.getAgentUserManager(getServletContext()).getAgentToProgressData().put(agentName, dtoAgentsProgressData);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
