package com.puyodev.luka.model.service

interface ConfigurationService {
    suspend fun fetchConfiguration(): Boolean
    val isShowTaskEditButtonConfig: Boolean
}