package cndjp.qicoo.infrastructure.logger

import mu.KotlinLogging

// qicooのロガー
open class QicooLogger {
    val logger by lazy { KotlinLogging.logger {} }
}
