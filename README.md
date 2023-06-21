# gcp-datastore-testtask-grakowski

### This is a Google Cloud Run project that generates test entries stored in the GCP Datastore.

There is a bambooGoalsToOperations field in the BrepEntry class - it is unnecessary from the point of view of the operation and appearance of the entity, it is 
only used to try to alternatively implement one of the points of the task to be performed.

### Automatic tests
For run automatic local tests, go to application.properties in test resources and provide new file path 
in spring.cloud.gcp.credentials.location property. The file should be a service account JSON file.
Service account used need to have permissions to brep-playground GCP project.

Attention!!! The test called "should delete all entries" is for removing all entities from the datastore, it should stay annotated Ignore until really needed.

