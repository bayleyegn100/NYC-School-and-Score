package com.yedebkid.jpmccodingchalleng.rest

import com.yedebkid.jpmccodingchalleng.model.domain.mapToDomainSchools
import com.yedebkid.jpmccodingchalleng.model.domain.mapToDomainScores
import com.yedebkid.jpmccodingchalleng.util.FailureResponseException
import com.yedebkid.jpmccodingchalleng.util.NullResponseException
import com.yedebkid.jpmccodingchalleng.util.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface SchoolsRepository {
    fun getSchools(): Flow<UIState>
    fun getScoresBySchoolDbn(): Flow<UIState>
}

class SchoolsRepositoryImpl @Inject constructor(
    private val schoolsApi: SchoolsApi
) : SchoolsRepository {

    override fun getSchools(): Flow<UIState> = flow {
        emit(UIState.LOADING)

        try {
            val response = schoolsApi.getAllSchools()
            if(response.isSuccessful) {
                response.body()?.let {
                    emit(UIState.SUCCESS(it.mapToDomainSchools()))
                } ?: throw NullResponseException(ERROR_NULL_MESSAGE)
            } else {
                throw FailureResponseException(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            emit(UIState.ERROR(e))
        }
    }

    override fun getScoresBySchoolDbn(): Flow<UIState> = flow {
        emit(UIState.LOADING)

        val response = schoolsApi.getSatScores()

        if(response.isSuccessful) {
            response.body()?.let { networkScores ->
                val schoolScore = networkScores.mapToDomainScores()
                emit(UIState.SUCCESS(schoolScore))
            } ?: throw NullResponseException("Scores response is null")
        } else {
            throw FailureResponseException(response.errorBody()?.string())
        }
    }

    companion object {
        private const val ERROR_NULL_MESSAGE = "School response is null"
    }
}