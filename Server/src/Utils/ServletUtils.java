package Utils;

import SystemEngine.SystemEngine;
import Users.AgentUserManager;
import Users.AllUsersManager;
import Users.AllyUserManager;
import Users.UboatUserManager;
import jakarta.servlet.ServletContext;

public class ServletUtils {

    private static final String UBOAT_USER_MANAGER_ATTRIBUTE_NAME = "uboatUserManager";
    private static final String ALLY_USER_MANAGER_ATTRIBUTE_NAME = "allyUserManager";
    private static final String AGENT_USER_MANAGER_ATTRIBUTE_NAME = "agentUserManager";

    private static final String ALL_USERS_MANAGER_ATTRIBUTE_NAME = "allUsersManager";




    private static final String ALL_UBOATS_ATTRIBUTE_NAME = "allUboats";
    private static final String SYSTEM_ENGINE_ATTRIBUTE_NAME = "systemEngine";
    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object(); //check later

    public static UboatUserManager getUboatUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(UBOAT_USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(UBOAT_USER_MANAGER_ATTRIBUTE_NAME, new UboatUserManager());
            }
        }
        return (UboatUserManager) servletContext.getAttribute(UBOAT_USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static AgentUserManager getAgentUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(AGENT_USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(AGENT_USER_MANAGER_ATTRIBUTE_NAME, new AgentUserManager());
            }
        }
        return (AgentUserManager) servletContext.getAttribute(AGENT_USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static AllyUserManager getAllyUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(ALLY_USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ALLY_USER_MANAGER_ATTRIBUTE_NAME, new AllyUserManager());
            }
        }
        return (AllyUserManager) servletContext.getAttribute(ALLY_USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static AllUsersManager getAllUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(ALL_USERS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ALL_USERS_MANAGER_ATTRIBUTE_NAME, new AllUsersManager());
            }
        }
        return (AllUsersManager) servletContext.getAttribute(ALL_USERS_MANAGER_ATTRIBUTE_NAME);
    }

}