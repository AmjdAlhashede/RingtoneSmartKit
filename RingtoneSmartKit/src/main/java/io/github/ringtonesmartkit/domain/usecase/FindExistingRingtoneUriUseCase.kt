package io.github.ringtonesmartkit.domain.usecase

import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.repository.RingtoneDataSource
import io.github.ringtonesmartkit.domain.repository.RingtoneRepository

internal class FindExistingRingtoneUriUseCase (
    private val ringtoneDataSource: RingtoneDataSource
){
    suspend operator fun invoke(source: RingtoneSource): RingtoneData? {
        return ringtoneDataSource.findExistingRingtoneUri(source)
    }
}