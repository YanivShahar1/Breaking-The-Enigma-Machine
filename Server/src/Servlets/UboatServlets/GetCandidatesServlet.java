package Servlets.UboatServlets;


import DTO.DTOAllyTeamDetails;
import DTO.DTODecryptionCandidate;
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

@WebServlet(name = "getUboatDecryptionCandidates", urlPatterns = {"/uboatDecryptionCandidates"})
public class GetCandidatesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String uboatName = request.getParameter("uboatName");

        List<DTODecryptionCandidate> decryptionCandidateList = new ArrayList<>();


        List<String> allies = ServletUtils.getAllUserManager(getServletContext()).getAllUboatsAllies().get(uboatName);
        allies.forEach(ally->decryptionCandidateList.addAll(ServletUtils.getAllyUserManager(getServletContext()).getAllyToDecryptionsCandidatesList().get(ally)));

        String json = gson.toJson(decryptionCandidateList);
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
    }
}

