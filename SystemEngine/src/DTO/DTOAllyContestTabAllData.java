package DTO;

import java.util.List;

public class DTOAllyContestTabAllData {
    private List<DTOAgentsProgressData> dtoAgentsProgressDataList;
    private List<DTOAllyTeamDetails> dtoAllyTeamDetailsList;
    private DTOContestData dtoContestData;
    private List<DTODecryptionCandidate> dtoDecryptionCandidateList;

    //private ContestStatus contestStatus;

    public DTOAllyContestTabAllData(
                                    List<DTOAgentsProgressData> dtoAgentsProgressDataList,
                                    List<DTOAllyTeamDetails> dtoAllyTeamDetailsList,
                                    DTOContestData dtoContestData,
                                    List<DTODecryptionCandidate> dtoDecryptionCandidateList) {
       // this.contestStatus = contestStatus;
        this.dtoAgentsProgressDataList = dtoAgentsProgressDataList;
        this.dtoAllyTeamDetailsList = dtoAllyTeamDetailsList;
        this.dtoContestData = dtoContestData;
        this.dtoDecryptionCandidateList = dtoDecryptionCandidateList;
    }

//    public ContestStatus getContestStatus() {
//        return contestStatus;
//    }

    public List<DTOAgentsProgressData> getDtoAgentsProgressData() {
        return dtoAgentsProgressDataList;
    }

    public List<DTOAllyTeamDetails> getDTOAllyTeamDetails() {
        return dtoAllyTeamDetailsList;
    }

    public DTOContestData getDTOContestData() {
        return dtoContestData;
    }

    public List<DTODecryptionCandidate> getDTODecryptionCandidate() {
        return dtoDecryptionCandidateList;
    }
}
