package Servlets.UboatServlets;

import SystemEngine.SystemEngine;
import SystemExceptions.*;
import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getInputProcessing", urlPatterns = {"/inputProcessing"})
public class InputProcessingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uboatName = request.getParameter("uboatName");
        SystemEngine systemEngine = ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName);

        try {
            boolean isManual = true;
            if (request.getParameter("isManual").equals("false")) {
                isManual = false;
            }
            String input = request.getParameter("input");
            String output = systemEngine.inputProcessing(input, isManual);

            ServletUtils.getUboatUserManager(getServletContext()).addUboatInput(uboatName, output);

            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            out.println(output);
            out.flush();
        } catch (CloneNotSupportedException | invalidABCLetter e) {
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
            out.flush();
        }
    }
}
