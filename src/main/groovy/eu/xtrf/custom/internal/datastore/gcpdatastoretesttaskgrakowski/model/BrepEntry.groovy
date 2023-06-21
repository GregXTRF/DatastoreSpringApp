package eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.model

import groovy.transform.ToString
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity
import org.springframework.cloud.gcp.data.datastore.core.mapping.Field
import org.springframework.cloud.gcp.data.datastore.core.mapping.Unindexed
import org.springframework.data.annotation.Id

@Entity(name = "gRakowskiBrepEntry")
@ToString
class BrepEntry {
    @Id
    @Unindexed
    Long id
    String fullName
    OperationSystem operationSystem
    @Field(name = "YearsOfWorkInXTRF")
    Integer yearsInXtrf
    @Unindexed
    BigDecimal bambooGoalsDonePercent
    Long bambooGoalsToOperations
}
