package SystemEngine;

import DecryptionManager.DM;
import SystemExceptions.*;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.Set;

interface Engine {
    void parseXML(InputStream XMLFilePath, Set<String> allBattleFieldNames) throws JAXBException, emptyABC, doubleLetterInABC, abcIsOdd,
            noXMLExtension, fileNotExist, tooFewRotors, tooFewRotorsCount, rotorsCountBiggerThanExist,
            rotorIDNotValid, doubleMappingRotor, doubleRotorID, keyIsNotInABC,
            noMatchBetweenMachineABCAndRotorABC, invalidNotchPosition, invalidNumOfReflectors,
            invalidReflectorID, doubleReflectorID, invalidReflectorInputOutput, doubleMappingReflector, InterruptedException, CloneNotSupportedException, invalidNumOfAgents, InvalidNumOfAllies, InvalidContestLevel, EmptyBattleFieldName, BattleFieldNameIsAlreadyExists;
}
