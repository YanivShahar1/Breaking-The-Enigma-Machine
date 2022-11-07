package Users;

import DTO.DTOAgentsProgressData;
import DecryptionManager.DM;
import SystemEngine.SystemEngine;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class AgentUserManager {

    private final Set<String> usersSet;
    private Map<String, Integer> agentToNumOfMissionsPerRequest;
    private Map<String, Integer> agentToNumOfThreads;
    private Map<String, DTOAgentsProgressData> agentToProgressData;
//    private Map<String, Integer> agentToNumOfmissionsDoneTillNow;
//    private Map<String, Integer> agentToNumOfCandidates;
    private Map<String, Boolean> agentToHisAllyApproval;
    public AgentUserManager() {
        usersSet = new HashSet<>();
        agentToNumOfMissionsPerRequest = new HashMap<>();
        agentToNumOfThreads = new HashMap<>();
//        agentToNumOfmissionsDoneTillNow = new HashMap<>();
//        agentToNumOfCandidates = new HashMap<>();
        agentToHisAllyApproval = new HashMap<>();
        agentToProgressData = new HashMap<>();
    }
    public synchronized void removeUser(String username) {
        usersSet.remove(username);
        agentToNumOfMissionsPerRequest.remove(username);
        agentToNumOfThreads.remove(username);
//        agentToNumOfmissionsDoneTillNow.remove(username);
//        agentToNumOfCandidates.remove(username);
        agentToHisAllyApproval.remove(username);
        agentToProgressData.remove(username);
    }

    public Map<String, DTOAgentsProgressData> getAgentToProgressData() {
        return agentToProgressData;
    }

    public Map<String, Integer> getAgentToNumOfThreads() {
        return agentToNumOfThreads;
    }

    public Map<String, Boolean> getAgentToHisAllyApproval() {
        return agentToHisAllyApproval;
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
    }



    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

//    public Map<String, Integer> getAgentToNumOfmissionsDoneTillNow() {
//        return agentToNumOfmissionsDoneTillNow;
//    }
//    public void increaseNumOfMissionsDoneTillNow(String agentName, int numOfMissions) {
//        agentToNumOfmissionsDoneTillNow.put(agentName, agentToNumOfmissionsDoneTillNow.get(agentName) + numOfMissions);
//    }
    public Map<String, Integer> getAgentToNumOfMissionsPerRequest() {
        return agentToNumOfMissionsPerRequest;
    }

//    public Map<String, Integer> getAgentToNumOfCandidates() {
//        return agentToNumOfCandidates;
//    }
//    public void increaseNumOfCandidates(String agentName, int numOfCandidates) {
//        agentToNumOfCandidates.put(agentName, agentToNumOfCandidates.get(agentName) + numOfCandidates);
//    }
    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }


}