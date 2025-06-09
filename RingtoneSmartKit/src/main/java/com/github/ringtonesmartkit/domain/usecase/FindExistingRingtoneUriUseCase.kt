package com.github.ringtonesmartkit.domain.usecase

import com.github.ringtonesmartkit.domain.model.RingtoneData
import com.github.ringtonesmartkit.domain.model.RingtoneSource
import com.github.ringtonesmartkit.domain.repository.RingtoneDataSource
import com.github.ringtonesmartkit.domain.repository.RingtoneRepository

internal class FindExistingRingtoneUriUseCase (
    private val ringtoneDataSource: RingtoneDataSource
){
    suspend operator fun invoke(source: RingtoneSource): RingtoneData? {
        return ringtoneDataSource.findExistingRingtoneUri(source)
    }
}