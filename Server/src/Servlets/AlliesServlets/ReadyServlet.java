package Servlets.AlliesServlets;

import DecryptionManager.DM;
import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet(name = "setAllyReadyForContest", urlPatterns = {"/allyReadyForContest"})
public class ReadyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uboatName = request.getParameter("uboatName");
        String allyName = request.getParameter("allyName");
        String missionSize = request.getParameter("missionSize");
        int missionSizeInteger = Integer.parseInt(missionSize);
//        try {
//            ServletUtils.getAllyUserManager(getServletContext()).getAllAlliesDM().put(allyName, new DM(allyName));
//        } catch (CloneNotSupportedException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).increaseNumOfRegisteredAllies();
        ServletUtils.getAllUserManager(getServletContext()).addAllyToUboat(allyName, uboatName);
        ServletUtils.getAllyUserManager(getServletContext()).getAllAlliesDM().get(allyName).setMissionSize(missionSizeInteger);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
