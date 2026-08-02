package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "short_slots")
data class ShortSlot(
    @PrimaryKey val id: Int, // 1, 2, 3
    val title: String = "",
    val status: String = "TODO", // "TODO", "DOING", "DONE"
    val startTime: Long? = null,
    val doneTime: Long? = null,
    val label: String // "MORNING", "AFTERNOON", "EVENING"
)

@Entity(tableName = "archive_days")
data class ArchiveDay(
    @PrimaryKey val date: String, // e.g. "JUL 12" or "2026-08-01"
    val dayNumber: Int, // 1-30
    val count: Int, // 0-3
    val titles: String, // comma separated or pipe separated titles
    val realDurations: String // comma separated durations in ms
)

@Entity(tableName = "rush_attempts")
data class RushAttempt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val slot: Int,
    val time: Long,
    val earlyBy: Long
)

@Entity(tableName = "stacked_attempts")
data class StackedAttempt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val slot: Int,
    val fromSlot: Int,
    val time: Long,
    val waitLeft: Long
)
