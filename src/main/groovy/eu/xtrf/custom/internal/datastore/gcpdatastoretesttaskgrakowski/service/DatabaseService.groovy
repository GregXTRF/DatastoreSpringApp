package eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.service

import eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.model.BrepEntry
import eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.model.OperationSystem
import eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.repository.DatabaseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.math.RoundingMode

@Service
class DatabaseService {

    final int valueToCalculateBambooGoalsToOperations = 100

    @Autowired
    DatabaseRepository databaseRepository

    void pushEntryToDatabase(List<BrepEntry> entriesToSave) {
        entriesToSave.each { entryToSave ->
            calculateBambooGoalsToOperationValue(entryToSave)
        }
        databaseRepository.saveAll(entriesToSave)
    }

    List<BrepEntry> getByOperationSystem(OperationSystem operationSystem) {
        return databaseRepository.getByOperationSystem(operationSystem)
    }

    List<BrepEntry> getBrepEntryWithBambooGoalsDonePercentHigherThanFromString(BigDecimal higherThanValue) {
        Iterable<BrepEntry> entries = databaseRepository.findAll()
        return entries.findAll { BrepEntry brepEntry ->
            brepEntry.bambooGoalsDonePercent > higherThanValue
        }
    }

    List<BrepEntry> getBrepEntryWithBambooGoalsDonePercentHigherThan(Long higherThanValue) {
        return databaseRepository.findByBambooGoalsToOperationsGreaterThan(higherThanValue * valueToCalculateBambooGoalsToOperations)
    }

    List<BrepEntry> getByOperationSystemAndBambooGoalsDonePercentHigherThan(OperationSystem operationSystem, BigDecimal higherThanValue) {
        return databaseRepository.getByOperationSystem(operationSystem).findAll {
            BrepEntry brepEntry -> brepEntry.bambooGoalsDonePercent > higherThanValue
        }
    }

    private void calculateBambooGoalsToOperationValue(BrepEntry entryToSave) {
        entryToSave.setBambooGoalsToOperations(
                (entryToSave.bambooGoalsDonePercent.setScale(2, RoundingMode.HALF_UP) * valueToCalculateBambooGoalsToOperations).toLong()
        )
    }
}