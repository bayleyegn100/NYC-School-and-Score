package com.yedebkid.jpmccodingchalleng.model.domain

import com.yedebkid.jpmccodingchalleng.model.SatScoresItem

data class Score(
    val dbn: String,
    val mathScore: String,
    val readScore: String,
    val writeScore: String,
    val schoolName: String,
)

fun List<SatScoresItem>.mapToDomainScores() =
    this.map {
        Score(
            dbn = it.dbn ?: "",
            mathScore = it.satMathAvgScore ?: "",
            readScore = it.satCriticalReadingAvgScore ?: "",
            writeScore = it.satWritingAvgScore ?: "",
            schoolName = it.schoolName ?: "",
        )
    }
