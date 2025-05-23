package com.example.sepo.data

import com.example.sepo.data.response.ConnectionResponse
import com.example.sepo.data.response.DoctorResponseItem
import com.example.sepo.data.response.ListUserResponseItem
import com.example.sepo.data.response.PostTestResponse
import com.example.sepo.data.response.PostTestResponseItem
import com.example.sepo.data.response.PreTestResponse
import com.example.sepo.data.response.PreTestResponseItem
import com.example.sepo.data.response.ProfileResponseItem
import com.example.sepo.data.retrofit.ApiService
import com.example.sepo.result.Result
import retrofit2.HttpException

class Repository(private val apiService: ApiService) {

    suspend fun connect(): Result<ConnectionResponse> {
        return try {
            val response = apiService.isConnect()
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }

    suspend fun listUser(): Result<List<ListUserResponseItem>> {
        return try {
            val response = apiService.listUser()
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }

    suspend fun createProfile(
        userId: String,
        profileName: String,
        age: Int,
        gender: String
    ): Result<ConnectionResponse> {
        return try {
            val response = apiService.createProfile(userId, profileName, age, gender)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }

    suspend fun getProfiles(userId: String): Result<List<ProfileResponseItem>> {
        return try {
            val response = apiService.getProfiles(userId)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }

    suspend fun saveUser(
        userId: String,
        name: String,
        email: String,
        photoUrl: String
    ): Result<ConnectionResponse> {
        return try {
            val response = apiService.saveUser(userId, name, email, photoUrl)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }

    suspend fun getPreTest(): Result<List<PreTestResponseItem>> {
        return try {
            val response = apiService.getPreTest()
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }

    suspend fun getPostTest(): Result<List<PostTestResponseItem>> {
        return try {
            val response = apiService.getPostTest()
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }

    suspend fun saveAnswerPostTest(
        userId: String,
        profileId: Int,
        questionId: Int,
        answerText: String
    ): Result<ConnectionResponse> {
        return try {
            val response = apiService.saveAnswerPostTest(userId, profileId , questionId, answerText)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }

    suspend fun saveAnswerPreTest(
        userId: String,
        profileId: Int,
        questionId: Int,
        answerText: String
    ): Result<ConnectionResponse> {
        return try {
            val response = apiService.saveAnswerPreTest(userId, profileId , questionId, answerText)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }

    suspend fun saveScore(
        userId: String,
        profileId: Int,
        knowledgeScore: Int,
        behaveScore: Int,
        hrqScore: Int,
        osteoporosisScore: Int,
        osteoarthritisScore: Int,
    ): Result<ConnectionResponse> {
        return try {
            val response = apiService.saveScore(userId, profileId, knowledgeScore, behaveScore, hrqScore, osteoporosisScore, osteoarthritisScore)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }

    suspend fun getDoctor(): Result<List<DoctorResponseItem>> {
        return try {
            val response = apiService.getDoctor()
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e.toString())
        }
    }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}
