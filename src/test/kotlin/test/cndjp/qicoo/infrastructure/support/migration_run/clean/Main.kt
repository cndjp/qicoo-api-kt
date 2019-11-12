package test.cndjp.qicoo.infrastructure.support.migration_run.clean

import test.cndjp.qicoo.infrastructure.support.migration_run.RepositorySpecSupport
import test.cndjp.qicoo.infrastructure.support.migration_run.dropDummyData

fun main() {
    RepositorySpecSupport.dropDummyData()
}