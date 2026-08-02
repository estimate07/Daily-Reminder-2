package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortDao {
    @Query("SELECT * FROM short_slots ORDER BY id ASC")
    fun getTodaySlots(): Flow<List<ShortSlot>>

    @Query("SELECT * FROM short_slots ORDER BY id ASC")
    suspend fun getTodaySlotsList(): List<ShortSlot>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSlot(slot: ShortSlot)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSlots(slots: List<ShortSlot>)

    @Query("SELECT * FROM archive_days ORDER BY dayNumber ASC")
    fun getArchiveDays(): Flow<List<ArchiveDay>>

    @Query("SELECT * FROM archive_days WHERE date = :date LIMIT 1")
    suspend fun getArchiveDayByDate(date: String): ArchiveDay?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArchiveDay(archiveDay: ArchiveDay)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArchiveDays(archiveDays: List<ArchiveDay>)

    @Query("SELECT * FROM rush_attempts ORDER BY time DESC")
    fun getRushAttempts(): Flow<List<RushAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRushAttempt(attempt: RushAttempt)

    @Query("SELECT * FROM stacked_attempts ORDER BY time DESC")
    fun getStackedAttempts(): Flow<List<StackedAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStackedAttempt(attempt: StackedAttempt)

    @Query("DELETE FROM rush_attempts")
    suspend fun clearRushAttempts()

    @Query("DELETE FROM stacked_attempts")
    suspend fun clearStackedAttempts()
}
