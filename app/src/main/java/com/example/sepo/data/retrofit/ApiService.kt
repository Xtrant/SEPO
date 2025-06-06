package com.example.sepo.data.retrofit

import com.example.sepo.data.response.ConnectionResponse
import com.example.sepo.data.response.DemographicTestResponseItem
import com.example.sepo.data.response.DoctorResponseItem
import com.example.sepo.data.response.ListUserResponseItem
import com.example.sepo.data.response.PostTestResponseItem
import com.example.sepo.data.response.PreTestResponseItem
import com.example.sepo.data.response.ProfileResponseItem
import com.example.sepo.data.response.VideoResponseItem
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("read.php")
    suspend fun listUser(): List<ListUserResponseItem>

    @FormUrlEncoded
    @POST("create_profile.php")
    suspend fun createProfile(
        @Field("user_id") uId: String,
        @Field("profile_name") name: String,
        @Field("age") age: String,
        @Field("gender") gender: String,
        @Field("berat") weight: String,
        @Field("tinggi") height: String,
    ): ConnectionResponse

    @FormUrlEncoded
    @POST("insert_condition.php")
    suspend fun saveCondition(
        @Field("user_id") uId: String,
        @Field("profile_id") profileId: Int,
        @Field("kondisi") condition: String,
    ): ConnectionResponse


    @GET("list_profiles.php")
    suspend fun getProfiles(
        @Query("user_id") uId: String
    ): List<ProfileResponseItem>

    @FormUrlEncoded
    @POST("save_user.php")
    suspend fun saveUser(
        @Field("user_id") uId: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("photo_url") photoUrl: String
    ): ConnectionResponse

    @GET("get_post_test.php")
    suspend fun getPostTest() : List<PostTestResponseItem>

    @GET("get_pre_test.php")
    suspend fun getPreTest() : List<PreTestResponseItem>

    @GET("get_demoghrapic_test.php")
    suspend fun getDemographicTest() : List<DemographicTestResponseItem>

    @FormUrlEncoded
    @POST("save_answer_post_test.php")
    suspend fun saveAnswerPostTest(
        @Field("user_id") userId: String,
        @Field("profile_id") profileId: Int,
        @Field("question_id") questionId: Int,
        @Field("answer_text") photoUrl: String
    ): ConnectionResponse

    @FormUrlEncoded
    @POST("save_answer_pre_test.php")
    suspend fun saveAnswerPreTest(
        @Field("user_id") userId: String,
        @Field("profile_id") profileId: Int,
        @Field("question_id") questionId: Int,
        @Field("answer_text") photoUrl: String
    ): ConnectionResponse

    @FormUrlEncoded
    @POST("save_answer_demography_test.php")
    suspend fun saveAnswerDemographyTest(
        @Field("user_id") userId: String,
        @Field("profile_id") profileId: Int,
        @Field("question_id") questionId: Int,
        @Field("answer_text") photoUrl: String
    ): ConnectionResponse

    @FormUrlEncoded
    @POST("insert_score.php")
    suspend fun saveScore(
        @Field("user_id") userId: String,
        @Field("profile_id") profileId: Int,
        @Field("knowledge_score") knowledgeScore: Int,
        @Field("behave_score") behaveScore: Int,
        @Field("hrq_score") hrqScore: Int,
        @Field("osteoporosis_score") osteoporosisScore: Int,
        @Field("osteoarthritis_score") osteoarthritisScore: Int,
    ): ConnectionResponse

    @GET("list_doctor.php")
    suspend fun getDoctor() : List<DoctorResponseItem>

    @GET("list_edukasi.php")
    suspend fun getEdukasi() : List<VideoResponseItem>

    @GET("list_exercise.php")
    suspend fun getExercise() : List<VideoResponseItem>



}