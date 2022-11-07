package Servlets.AgentServlets;

import DTO.ContestStatus;
import DecryptionManager.DM;
import Machine.Enigma;
import SystemEngine.SystemEngine;
import Users.AllUsersManager;
import Users.UboatUserManager;
import Utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getIsEveryoneReady", urlPatterns = {"/isEveryoneReady"})
public class IsEveryoneReadyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String agentName = request.getParameter("agentName");

        AllUsersManager allUsersManager = ServletUtils.getAllUserManager(getServletContext());
        UboatUserManager uboatUserManager = ServletUtils.getUboatUserManager(getServletContext());

        String allyName = allUsersManager.getAllyFromAgentMap().get(agentName);
        if (allyName == null) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else {
            String uboatName = allUsersManager.getUboatFromAllyMap().get(allyName);
            if (uboatName==null){
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
                PrintWriter out = response.getWriter();
                out.println("No Uboat has been chosen for ally: " + allyName);
                out.flush();
            }
            else{

                Boolean isUboatReady = uboatUserManager.getReadyUboats().get(uboatName);

                response.setContentType("text/plain;charset=UTF-8");

                if(isUboatReady == null || isUboatReady == false){
                    response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
                    PrintWriter out = response.getWriter();
                    out.println("Uboat : " + uboatName + " is not ready yet");
                    out.flush();
                }

                //uboat is ready
                else {
                    int numOfAlliesNeeded = uboatUserManager.getAllUboats().get(uboatName).getNumOfAlliesNeeded();
                    int numOfRegisteredAllies = allUsersManager.getAllUboatsAllies().get(uboatName).size();

                    if (numOfRegisteredAllies == numOfAlliesNeeded){
                        response.setStatus(HttpServletResponse.SC_OK);
                        DM dm = ServletUtils.getAllyUserManager(getServletContext()).getAllAlliesDM().get(allyName);
                        try {
                            if (dm.getIsRunning()) {
                                return;// avoiding launching same DM multiple times for multiple agents from the same ally
                            }
                            dm.setIsRunning(true);
                            SystemEngine systemEngine = ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName);
                            Enigma enigma = systemEngine.getEnigma().clone();
                            dm.setEnigma(enigma);
                            dm.initEnigmaRotorsPositions(dm.getEnigma().getRotors());
                            dm.setRotorsCount(systemEngine.getRotorsCount());
                            dm.setLevel(systemEngine.getContestLevel());
                            dm.setAllPossibleReflectors(systemEngine.getReflectorsList());
                            dm.setAllPossibleRotors(systemEngine.getRotorsList());
                            dm.setDictionary(systemEngine.getDictionary());
                            dm.setMessage(ServletUtils.getUboatUserManager(getServletContext()).getUboatInputs().get(uboatName));
                            dm.setContestStatus(ContestStatus.RUNNING);

                            Thread dmThread = new Thread(dm);
                            dmThread.setDaemon(true);
                            dmThread.start();
                            ServletUtils.getUboatUserManager(getServletContext()).getAllUboats().get(uboatName).setStatus(ContestStatus.RUNNING);

                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }


                    }
                    else {
                        response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
                        PrintWriter out = response.getWriter();
                        out.println("Not all allies are ready");
                        out.flush();
                    }

                }

            }
        }

    }




}
