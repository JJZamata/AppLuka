
package com.puyodev.luka.model.service

interface LogService {
  fun logNonFatalCrash(throwable: Throwable)
  fun logMessage(message: String)
  fun logError(message: String, error: Exception)
}

