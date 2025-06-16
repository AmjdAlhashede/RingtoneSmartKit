package io.github.ringtonesmartkit.domain.usecase

import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.repository.RingtoneDataSource
import io.github.ringtonesmartkit.domain.repository.RingtoneRepository
import javax.inject.Inject

internal class FindExistingRingtoneUriUseCase @Inject constructor(
    private val ringtoneDataSource: RingtoneDataSource
){
    suspend operator fun invoke(source: RingtoneSource): RingtoneData? {
        return ringtoneDataSource.findExistingRingtoneUri(source)
    }
}