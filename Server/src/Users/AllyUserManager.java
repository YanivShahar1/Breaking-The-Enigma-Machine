package Users;

import DTO.DTODecryptionCandidate;
import DecryptionManager.DM;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class AllyUserManager {

    private final Set<String> usersSet;
    private Map<String, DM> alliesDM;
    private Map<String, List<DTODecryptionCandidate>> allyToDecryptionsCandidatesList;

    public AllyUserManager() {
        usersSet = new HashSet<>();
        alliesDM = new HashMap<>();
        allyToDecryptionsCandidatesList = new HashMap<>();
    }
    public synchronized void removeUser(String username) {
        usersSet.remove(username);
        alliesDM.remove(username);
        allyToDecryptionsCandidatesList.remove(username);
    }
    public synchronized void addAllyWithDM(String allyName, DM dm) {
        alliesDM.put(allyName,dm);

    }
    public synchronized Map<String,DM> getAllAlliesDM() {
        return (alliesDM);
    }

    public Map<String, List<DTODecryptionCandidate>> getAllyToDecryptionsCandidatesList() {
        return allyToDecryptionsCandidatesList;
    }
    public void concatDecryptionsCandidatesListToAlly(String allyName, List<DTODecryptionCandidate> lst) {
        List<DTODecryptionCandidate> currentDTODecryptionCandidateList = allyToDecryptionsCandidatesList.get(allyName);
        currentDTODecryptionCandidateList.addAll(lst);
    }
    public void addDecryptionCandidateToAlly(String allyName, DTODecryptionCandidate dtoDecryptionCandidate) {
        List<DTODecryptionCandidate> currentDTODecryptionCandidateList = allyToDecryptionsCandidatesList.get(allyName);
        currentDTODecryptionCandidateList.add(dtoDecryptionCandidate);
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
    }


    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }
}