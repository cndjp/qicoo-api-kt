package cndjp.qicoo.infrastructure.logger

import mu.KotlinLogging

// qicooのロガー
abstract class QicooLogger {
    val logger  by lazy { KotlinLogging.logger {} }
}