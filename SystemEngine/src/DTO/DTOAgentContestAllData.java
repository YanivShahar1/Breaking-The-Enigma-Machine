package DTO;

import java.util.List;

public class DTOAgentContestAllData {
    DTOContestData dtoContestData;
    List<DTOAgentDetails> dtoAgentDetailsList;
    ContestStatus contestStatus;
//    DTOAgentsProgressData dtoAgentsProgressData;
//    List<DTODecryptionCandidate> dtoDecryptionCandidateList;

    public DTOAgentContestAllData(DTOContestData dtoContestData, List<DTOAgentDetails> dtoAgentDetails,
    ContestStatus contestStatus) {
        this.dtoContestData = dtoContestData;
        this.dtoAgentDetailsList = dtoAgentDetails;
        this.contestStatus = contestStatus;
//        this.dtoAgentsProgressData = dtoAgentsProgressData;
//        this.dtoDecryptionCandidateList = dtoDecryptionCandidate;
    }

    public ContestStatus getContestStatus() {
        return contestStatus;
    }

    public DTOContestData getDtoContestData() {
        return dtoContestData;
    }

    public List<DTOAgentDetails> getDtoAgentDetailsList() {
        return dtoAgentDetailsList;
    }

//    public DTOAgentsProgressData getDtoAgentsProgressData() {
//        return dtoAgentsProgressData;
//    }
//
//    public List<DTODecryptionCandidate> getDtoDecryptionCandidateList() {
//        return dtoDecryptionCandidateList;
//    }
}
