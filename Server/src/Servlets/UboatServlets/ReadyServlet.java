package Servlets.UboatServlets;

import DTO.ContestStatus;
import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet(name = "setUboatReadyForContest", urlPatterns = {"/uboatReadyForContest"})
public class ReadyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uboatName = request.getParameter("uboatName");
        String output = request.getParameter("output");
        String correctEncryption = request.getParameter("input");
        ServletUtils.getUboatUserManager(getServletContext()).setUboatReady(uboatName);
        ServletUtils.getUboatUserManager((getServletContext())).addUboatInput(uboatName, output);
        ServletUtils.getUboatUserManager((getServletContext())).getUboatToHisCorrectEncryption().put(uboatName, correctEncryption.trim());
        ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).setStatus(ContestStatus.WAITING);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
