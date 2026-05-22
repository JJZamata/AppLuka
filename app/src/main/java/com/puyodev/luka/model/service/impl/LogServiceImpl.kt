/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.puyodev.luka.model.service.impl

import android.util.Log
import com.puyodev.luka.model.service.LogService
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.Firebase
import javax.inject.Inject

class LogServiceImpl @Inject constructor() : LogService {
  override fun logNonFatalCrash(throwable: Throwable) {
    Firebase.crashlytics.recordException(throwable)
    Log.e(TAG, "Non-fatal crash", throwable)
  }

  override fun logMessage(message: String) {
    // Registramos en Logcat para desarrollo
    Log.d(TAG, message)

    // También podemos enviar a Crashlytics como log personalizado
    Firebase.crashlytics.log(message)
  }

  override fun logError(message: String, error: Exception) {
    // Registramos en Logcat
    Log.e(TAG, message, error)

    // Enviamos a Crashlytics como log y excepción no fatal
    Firebase.crashlytics.log(message)
    Firebase.crashlytics.recordException(error)
  }

  companion object {
    private const val TAG = "LukaApp"
  }
}
