package Servlets.UboatServlets;

import SystemEngine.SystemEngine;
import SystemExceptions.*;
import Utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;

@WebServlet(name = "setXMLFile", urlPatterns = {"/xmlFile"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024)
public class XMLFileUploadServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uboatName = request.getParameter("uboatName");
        SystemEngine systemEngine = ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName);
        try {
            Collection<Part> parts = request.getParts();

            for (Part part : parts) {
                systemEngine.parseXML(part.getInputStream(), ServletUtils.getUboatUserManager(getServletContext()).getAllBattleFieldNames());
                systemEngine.setUboatName(uboatName);
            }
            ServletUtils.getUboatUserManager(getServletContext()).getAllBattleFieldNames().add(systemEngine.getBattleFieldName());

            ServletUtils.getUboatUserManager(getServletContext()).addUboat(uboatName, systemEngine);


            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            out.println("XML Loaded");
            out.flush();
        } catch (emptyABC | JAXBException | doubleLetterInABC | abcIsOdd | noXMLExtension | fileNotExist |
                 tooFewRotors | tooFewRotorsCount | rotorsCountBiggerThanExist | rotorIDNotValid | doubleMappingRotor |
                 keyIsNotInABC | noMatchBetweenMachineABCAndRotorABC | invalidNotchPosition | invalidNumOfReflectors |
                 doubleRotorID | invalidReflectorID | doubleReflectorID | invalidReflectorInputOutput |
                 doubleMappingReflector | InterruptedException | CloneNotSupportedException | invalidNumOfAgents |
                 InvalidNumOfAllies | InvalidContestLevel | EmptyBattleFieldName | ServletException |
                 BattleFieldNameIsAlreadyExists e) {
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
            out.flush();
        }
    }
}
