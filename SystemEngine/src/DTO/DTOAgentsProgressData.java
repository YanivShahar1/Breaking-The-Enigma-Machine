package DTO;

public class DTOAgentsProgressData {
    private String agentName;
    private long numOfDoneMissions;
    private long totalMissions;
    private int numOfCandidates;

    public DTOAgentsProgressData(String agentName, long numOfDoneMissions, long totalMissions, int numOfCandidates) {
        this.agentName = agentName;
        this.numOfDoneMissions = numOfDoneMissions;
        this.totalMissions = totalMissions;
        this.numOfCandidates = numOfCandidates;
    }
    public void reset() {
        numOfDoneMissions = 0;
        totalMissions = 0;
        numOfCandidates = 0;
    }
    public String getAgentName() {
        return agentName;
    }
    public long getNumOfDoneMissions() {
        return numOfDoneMissions;
    }
    public long getTotalMissions() {
        return totalMissions;
    }
    public int getNumOfCandidates() {
        return numOfCandidates;
    }
}
