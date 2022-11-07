package Servlets.AlliesServlets;

import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "getWinner", urlPatterns = {"/getWinner"})
public class GetWinner extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userType = request.getParameter("userType");
        String uboatName;
        if (userType.equals("uboat")) {
            uboatName = request.getParameter("userName");
        }
        else {
            String allyName = request.getParameter("userName");
            uboatName = ServletUtils.getAllUserManager(getServletContext()).getUboatFromAllyMap().get(allyName);
        }
        String winner = ServletUtils.getUboatUserManager(getServletContext()).getUboatToAllyNameWinner().get(uboatName);
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.print(winner);
        out.flush();
    }

}
