package io.github.ringtonesmartkit.data.repository

import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.repository.RingtoneDataSource
import io.github.ringtonesmartkit.domain.repository.RingtoneRepository

internal class RingtoneRepositoryImpl(
    private val sources: List<RingtoneDataSource>,
) : RingtoneRepository {

    override suspend fun getAvailableSources(): List<RingtoneSource> =
        sources.map { it.getSourceType() }

    override suspend fun getRingtone(source: RingtoneSource): RingtoneData? {
        return sources.firstOrNull { it.canHandle(source) }?.loadRingtone(source)
    }

    override suspend fun applyRingtones(
        source: RingtoneSource,
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
    ) {
        sources.firstOrNull { it.canHandle(source) }
            ?.applyRingtones(source = source, target = target, ringtone = ringtone)
    }

    override suspend fun applyContactRingtone(
        source: RingtoneSource,
        target: RingtoneTarget.ContactTarget,
        data: RingtoneData,
    ) {
        sources.firstOrNull { it.canHandle(source) }
            ?.applyRingtonesToContact(
                source = source,
                target = target,
                data = data
            )
    }
}