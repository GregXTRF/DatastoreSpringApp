package eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.repository

import eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.model.BrepEntry
import eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.model.OperationSystem
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository
import org.springframework.stereotype.Repository

@Repository
interface DatabaseRepository extends DatastoreRepository<BrepEntry, Long> {
    List<BrepEntry> getByOperationSystem(OperationSystem operationSystem)

    List<BrepEntry> findByBambooGoalsToOperationsGreaterThan(Long higherThan)
}
