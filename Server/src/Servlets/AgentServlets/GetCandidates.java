package Servlets.AgentServlets;

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

@WebServlet(name = "getCandidates", urlPatterns = {"/sendCandidates"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024)
public class GetCandidates extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String agentName = request.getParameter("agentName");
        String allyName = ServletUtils.getAllUserManager(getServletContext()).getAllyFromAgentMap().get(agentName);
        if (allyName == null) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else {
            String uboatName = ServletUtils.getAllUserManager(getServletContext()).getUboatFromAllyMap().get(allyName);
            Boolean isWinner;
            if (uboatName == null) {
                isWinner = true;
                Gson gson = new Gson();
                String json = gson.toJson(true, Boolean.class);
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.println(json);
                out.flush();
            }
            else {
                Gson gson = new Gson();

                StringBuffer jb = new StringBuffer();
                String line = null;
                try {
                    BufferedReader reader = request.getReader();
                    while ((line = reader.readLine()) != null)
                        jb.append(line);
                } catch (Exception e) { /*report an error*/ }

                String json = jb.toString();


                Type type = new TypeToken<DTODecryptionCandidate>() {}.getType();
                DTODecryptionCandidate decryptionsCandidate = gson.fromJson(json, type);

                ServletUtils.getAllyUserManager(getServletContext()).addDecryptionCandidateToAlly(allyName, decryptionsCandidate);
                String input = ServletUtils.getUboatUserManager(getServletContext()).getUboatToHisCorrectEncryption().get(uboatName);


                boolean[] foundWinner = new boolean[1];
                foundWinner[0] = false;
                response.setContentType("text/plain;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                if (decryptionsCandidate.getDecryption().equals(input)
                        && ServletUtils.getUboatUserManager(getServletContext()).getAllUboats()
                        .get(uboatName).getStatus() == ContestStatus.RUNNING) {
                    //winner!
                    foundWinner[0] = true;
                }

                if (foundWinner[0]) {

                    ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).setStatus(ContestStatus.FINISHED);
                    List<String> alliesList = ServletUtils.getAllUserManager(getServletContext()).getAllUboatsAllies().get(uboatName);
                    alliesList.forEach(ally -> {
                        ServletUtils.getAllyUserManager(getServletContext()).getAllAlliesDM().get(ally).setContestStatus(ContestStatus.FINISHED);
                    });
                    ServletUtils.getUboatUserManager(getServletContext()).getUboatToAllyNameWinner().put(uboatName, allyName);
                    ServletUtils.getUboatUserManager(getServletContext()).getReadyUboats().put(uboatName, false);
                    isWinner = true;
                }
                else {
                    isWinner = false;
                }
                if (ServletUtils.getUboatUserManager(getServletContext())
                        .getAllUboats().get(uboatName).getStatus() == ContestStatus.FINISHED) {
                    isWinner = true;
                }
                out.println(gson.toJson(isWinner));
                out.flush();
            }
        }

    }
}
