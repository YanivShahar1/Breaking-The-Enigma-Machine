package Servlets.UboatServlets;

import Machine.Reflector;
import Machine.Rotor;
import SystemEngine.SystemEngine;
import SystemExceptions.*;
import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "setCodeConfiguration", urlPatterns = {"/setCode"})
public class SetCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uboatName = request.getParameter("uboatName");
        SystemEngine systemEngine = ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName);
        try {
            List<Integer> tempRotorsIDList = systemEngine.setUsedRotors(request.getParameter("rotorsID"));
            List<Rotor> tempRotorsList = systemEngine.setStartingUsedRotorsPositions(
                    request.getParameter("rotorsPositions"), systemEngine.createUsedRotorsList(tempRotorsIDList));
            Reflector tempReflector = systemEngine.setUsedReflector(request.getParameter("reflectorID"));
            Map<String, String> plugin = new LinkedHashMap<>();
            systemEngine.setCodeConfiguration(tempRotorsList, tempReflector, plugin);
            systemEngine.setEnigmaMachine();


            response.setStatus(HttpServletResponse.SC_OK);
        } catch (keyIsNotInABC | invalidRotorsStartingPositions | CloneNotSupportedException | doubleRotorID |
                 rotorIDNotValid | invalidAmountOfRotors | invalidRotorIDType | invalidReflectorID | doubleReflectorID e) {
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
            out.flush();
        }
    }
}
