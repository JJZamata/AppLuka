
package com.puyodev.luka.model.service

interface LogService {
  fun logNonFatalCrash(throwable: Throwable)
}
