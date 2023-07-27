//file:noinspection UnnecessaryBigDecimalInstantiation
package eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.service

import eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.model.BrepEntry
import eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.model.OperationSystem
import eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.repository.DatabaseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Ignore
import spock.lang.Specification

import java.math.RoundingMode

@SpringBootTest
class DatabaseServiceTest extends Specification {
    private List<BrepEntry> brepEntries
    @Autowired
    private DatabaseService databaseService
    @Autowired
    private DatabaseRepository databaseRepository

    def setup() {
        List<BrepEntry> preparedEntries = prepareEntitiesToTests()
        brepEntries = preparedEntries
        databaseRepository.saveAll { preparedEntries.iterator() }
    }

    def cleanup() {
        brepEntries.each {
            databaseRepository.deleteById(it.id)
        }
    }

    def "should push Entry to data base"() {
        given:
        List<BrepEntry> entries = prepareEntitiesToTests()
        when:
        databaseService.pushEntryToDatabase(entries)
        then:
        noExceptionThrown()
        cleanup:
        entries.each { databaseRepository.deleteById(it.id) }
    }

    def "should get BrepEntry with BambooGoals Done Percent Higher Than from String value"() {
        when:
        List<BrepEntry> entries =
                databaseService.getBrepEntryWithBambooGoalsDonePercentHigherThanFromString(new BigDecimal("10.30"))
        then:
        noExceptionThrown()
        !entries.empty
        entries.each { println(it.bambooGoalsDonePercent) }
    }

    def "should save the entities with two decimal places rounded half up"() {
        when:
        List<BrepEntry> entries = databaseRepository.findAllById { brepEntries.id.iterator() } as List
        then:
        noExceptionThrown()
        entries.each { println(it.bambooGoalsDonePercent) }
        entries.bambooGoalsDonePercent.containsAll([new BigDecimal("1.30"), new BigDecimal("10.31"), new BigDecimal("20.99")])
    }

    def "should get BrepEntry with BambooGoals Done Percent Higher Than"() {
        when:
        List<BrepEntry> entries = databaseService.getBrepEntryWithBambooGoalsDonePercentHigherThan(10)
        then:
        noExceptionThrown()
        !entries.empty
        entries.size() >= 2
        entries.bambooGoalsDonePercent.containsAll([new BigDecimal("10.31"), new BigDecimal("20.99")])
        entries.each { BrepEntry it -> println(it.bambooGoalsDonePercent) }
    }

    def "should get BrepEntry by Operation System"(OperationSystem operationSystem) {
        when:
        List<BrepEntry> entries = databaseService.getByOperationSystem(operationSystem)
        List<BrepEntry> entryList = entries.findAll { entry -> entry.operationSystem == operationSystem }
        then:
        noExceptionThrown()
        !entries.empty
        entryList.size() >= number
        where:
        operationSystem         | number
        OperationSystem.WINDOWS | 1
        OperationSystem.UBUNTU  | 1
        OperationSystem.DEBIAN  | 1
    }

    def "should get BrepEntry by Operation System and BambooGoals Done Percent Higher Than value"(
            OperationSystem operationSystem, BigDecimal higherThanValue
    ) {
        when:
        List<BrepEntry> entries = databaseService.getByOperationSystemAndBambooGoalsDonePercentHigherThan(
                operationSystem, higherThanValue
        )
        then:
        noExceptionThrown()
        !entries.empty
        entries.size() >= number
        where:
        operationSystem         | higherThanValue         | number
        OperationSystem.WINDOWS | new BigDecimal("20.00") | 1
        OperationSystem.UBUNTU  | new BigDecimal("10.00") | 1
        OperationSystem.DEBIAN  | new BigDecimal("1.20")  | 1
    }

    @Ignore
    def "should delete all entries"() {
        when:
        databaseRepository.deleteAll()
        then:
        noExceptionThrown()
    }

    private List<BrepEntry> prepareEntitiesToTests() {
        List<BrepEntry> preparedEntries = [
                new BrepEntry(
                        fullName: "nameOne", operationSystem: OperationSystem.DEBIAN, yearsInXtrf: 2,
                        bambooGoalsDonePercent: new BigDecimal("1.295")),
                new BrepEntry(
                        fullName: "nameTwo", operationSystem: OperationSystem.UBUNTU, yearsInXtrf: 3,
                        bambooGoalsDonePercent: new BigDecimal("10.312")),
                new BrepEntry(
                        fullName: "nameThree", operationSystem: OperationSystem.WINDOWS, yearsInXtrf: 4,
                        bambooGoalsDonePercent: new BigDecimal("20.985")),
        ]
        preparedEntries.each {
            Long value = (it.bambooGoalsDonePercent.setScale(2, RoundingMode.HALF_UP) * databaseService.valueToCalculateBambooGoalsToOperations).toLong()
            it.setBambooGoalsToOperations(value)
        }
        return preparedEntries
    }
}
