package com.example.sepo.ui.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sepo.data.Repository
import com.example.sepo.data.response.ConnectionResponse
import com.example.sepo.data.response.DemographicTestResponseItem
import com.example.sepo.data.response.PostTestResponseItem
import com.example.sepo.data.response.PreTestResponseItem
import com.example.sepo.data.response.ProfileStatusResponse
import com.example.sepo.data.response.ScoreResponse
import com.example.sepo.result.Result
import kotlinx.coroutines.launch

class TestViewModel(private val repository: Repository) : ViewModel() {
    private val _preTestResult = MutableLiveData<Result<List<PreTestResponseItem>>>()
    val preTestResult: LiveData<Result<List<PreTestResponseItem>>> = _preTestResult

    private val _postTestResult = MutableLiveData<Result<List<PostTestResponseItem>>>()
    val postTestResult: LiveData<Result<List<PostTestResponseItem>>> = _postTestResult

    private val _demographicTestResult = MutableLiveData<Result<List<DemographicTestResponseItem>>>()
    val demographicTestResult: LiveData<Result<List<DemographicTestResponseItem>>> = _demographicTestResult

    private val _saveCondition = MutableLiveData<Result<ConnectionResponse>>()
    val saveCondition : LiveData<Result<ConnectionResponse>> = _saveCondition

    private val _saveAnswerPostTest = MutableLiveData<Result<ConnectionResponse>>()
    val saveAnswerPostTest : LiveData<Result<ConnectionResponse>> = _saveAnswerPostTest

    private val _saveAnswerPreTest = MutableLiveData<Result<ConnectionResponse>>()
    val saveAnswerPreTest : LiveData<Result<ConnectionResponse>> = _saveAnswerPostTest

    private val _saveAnswerDemographyTest = MutableLiveData<Result<ConnectionResponse>>()
    val saveAnswerDemographyTest : LiveData<Result<ConnectionResponse>> = _saveAnswerDemographyTest

    private val _saveScore = MutableLiveData<Result<ConnectionResponse>>()
    val saveScore : LiveData<Result<ConnectionResponse>> = _saveScore

    private val _score = MutableLiveData<Result<ScoreResponse>>()
    val score : LiveData<Result<ScoreResponse>> = _score

    private val _saveProfileStatus = MutableLiveData<Result<ConnectionResponse>>()
    val saveProfileStatus : LiveData<Result<ConnectionResponse>> = _saveProfileStatus

    private val _profileStatus = MutableLiveData<Result<ProfileStatusResponse>>()
    val profileStatus : LiveData<Result<ProfileStatusResponse>> = _profileStatus


    fun getPreTest() {
        viewModelScope.launch {
            _preTestResult.value = Result.Loading
            _preTestResult.value = repository.getPreTest()
        }
    }

    fun getPostTest() {
        viewModelScope.launch {
            _postTestResult.value = Result.Loading
            _postTestResult.value = repository.getPostTest()
        }
    }

    fun getDemographicTest() {
        viewModelScope.launch {
            _demographicTestResult.value = Result.Loading
            _demographicTestResult.value = repository.getDemographicTest()
        }
    }

    fun getSaveAnswerPostTest(
        userId: String,
        profileId: Int,
        questionId: Int,
        answerText: String
    ) {
        viewModelScope.launch {
            _saveAnswerPostTest.value = Result.Loading
            _saveAnswerPostTest.value = repository.saveAnswerPostTest(userId, profileId, questionId, answerText)
        }
    }

    fun getSaveCondition(
        userId: String,
        profileId: Int,
        condition: String
    ) {
        viewModelScope.launch {
            _saveCondition.value = Result.Loading
            _saveCondition.value = repository.saveCondition(userId, profileId, condition)
        }
    }

    fun getSaveAnswerPreTest(
        userId: String,
        profileId: Int,
        questionId: Int,
        answerText: String
    ) {
        viewModelScope.launch {
            _saveAnswerPreTest.value = Result.Loading
            _saveAnswerPreTest.value = repository.saveAnswerPreTest(userId, profileId, questionId, answerText)
        }
    }

    fun getSaveAnswerDemographyTest(
        userId: String,
        profileId: Int,
        questionId: Int,
        answerText: String
    ) {
        viewModelScope.launch {
            _saveAnswerDemographyTest.value = Result.Loading
            _saveAnswerDemographyTest.value = repository.saveAnswerDemographyTest(userId, profileId, questionId, answerText)
        }
    }

    fun getSaveScore(
        userId: String,
        profileId: Int,
        knowledgeScore: Int?,
        behaveScore: Int?,
        hrqScore: Int?,
        osteoporosisScore: Int?,
        osteoarthritisScore: Int?,
    ) {
        viewModelScope.launch {
            _saveScore.value = Result.Loading
            _saveScore.value = repository.saveScore(userId, profileId, knowledgeScore, behaveScore, hrqScore, osteoporosisScore, osteoarthritisScore)
        }
    }

    fun getSaveProfileStatus(
        userId: String,
        profileId: Int,
        isPreTest: Int?,
        isPostTest: Int?,
        isEducation: Int?
    ) {
        viewModelScope.launch {
            _saveProfileStatus.value = Result.Loading
            _saveProfileStatus.value = repository.saveProfileStatus(userId, profileId, isPreTest, isPostTest, isEducation)
        }
    }

    fun getScore(
        userId: String,
        profileId: Int,
    ) {
        viewModelScope.launch {
            _score.value = Result.Loading
            _score.value = repository.getScore(userId, profileId)
        }
    }

    fun getProfileStatus(
        userId: String,
        profileId: Int,
    ) {
        viewModelScope.launch {
            _profileStatus.value = Result.Loading
            _profileStatus.value = repository.getProfileStatus(userId, profileId)
        }
    }



}