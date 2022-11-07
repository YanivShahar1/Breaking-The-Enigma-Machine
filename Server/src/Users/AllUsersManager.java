package Users;

import DTO.DTOAgentDetails;

import java.util.*;

public class AllUsersManager {

    private Map<String,String> allyToUboat;
    private Map<String,String> agentToAlly;
    private Map<String, List<String>> uboatToAllHisAllies;
    private Map<String, List<DTOAgentDetails>> allyToAllHisAgents;
    private Set<String> allUsers;

    public AllUsersManager(){
        allyToUboat = new HashMap<>();
        agentToAlly = new HashMap<>();
        uboatToAllHisAllies = new HashMap<>();
        allyToAllHisAgents = new HashMap<>();
        allUsers = new HashSet<>();

    }
    public synchronized void removeUser(String username) {
        allUsers.remove(username);
    }
    public synchronized void addUser(String username) {
        allUsers.add(username);
    }
    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(allUsers);
    }
    public boolean isUserExists(String username) {
        return allUsers.contains(username);
    }

    public Map<String, List<DTOAgentDetails>> getAllyToAllHisAgents() {
        return allyToAllHisAgents;
    }
    public void addAgentToAlly(DTOAgentDetails agent, String allyName){
        agentToAlly.put(agent.getAgentName(), allyName);
        List<DTOAgentDetails> agentDetailsList = allyToAllHisAgents.get(allyName);
        agentDetailsList.add(agent);
    }
    public void addAllyToUboat(String allyName, String uboatName) {
        allyToUboat.put(allyName, uboatName);
        List<String> allyList = uboatToAllHisAllies.get(uboatName);
        allyList.add(allyName);
    }
    public Map<String, String> getAllyFromAgentMap() {
        return agentToAlly;
    }
    public Map<String, String> getUboatFromAllyMap() {
        return allyToUboat;
    }
    public Map<String, List<String>> getAllUboatsAllies() {
        return uboatToAllHisAllies;
    }
    public void removeAllyFromUboatToAllHisAllies(String allyName, String uboatName) {
        List<String> allies = uboatToAllHisAllies.get(uboatName);
        allies.removeIf(ally -> ally.equals(allyName));
    }
}