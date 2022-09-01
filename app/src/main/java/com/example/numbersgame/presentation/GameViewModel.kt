package com.example.numbersgame.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.numbersgame.R
import com.example.numbersgame.data.GameRepositoryImpl
import com.example.numbersgame.domain.entities.GameResult
import com.example.numbersgame.domain.entities.GameSettings
import com.example.numbersgame.domain.entities.Level
import com.example.numbersgame.domain.entities.Question
import com.example.numbersgame.domain.usecases.GenerateQuestionUseCase
import com.example.numbersgame.domain.usecases.GetGameSettingsUseCase
import java.util.concurrent.TimeUnit

class GameViewModel(private val application: Application,
                    private val level: Level) : ViewModel() {

    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private lateinit var gameSettings: GameSettings
    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _rightAnswersPercent = MutableLiveData<Int>()
    val rightAnswersPercent: LiveData<Int>
        get() = _rightAnswersPercent

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughRightAnswersCount = MutableLiveData<Boolean>()
    val enoughRightAnswersCount: LiveData<Boolean>
        get() = _enoughRightAnswersCount

    private val _enoughRightAnswersPercent = MutableLiveData<Boolean>()
    val enoughRightAnswersPercent: LiveData<Boolean>
        get() = _enoughRightAnswersPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent


    private var rightAnswersCount = 0
    private var questionsCount = 0

    init {
        startGame()
    }

    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        setProgressAnswer()
    }

    fun chooseAnswer(answer: Int) {
        checkAnswer(answer)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculateRightAnswersPercent()
        _rightAnswersPercent.value = percent
        setProgressAnswer()
        _enoughRightAnswersCount.value = rightAnswersCount >= gameSettings.minCountOfRightAnswers
        _enoughRightAnswersPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun setProgressAnswer() {
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.progress_answers),
            rightAnswersCount,
            gameSettings.minCountOfRightAnswers
        )
    }

    private fun calculateRightAnswersPercent(): Int {
        return ((rightAnswersCount / questionsCount.toDouble()) * 100).toInt()
    }

    private fun getGameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun checkAnswer(answer: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (answer == rightAnswer) {
            rightAnswersCount++
        }
        questionsCount++
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun startTimer() {

        timer = object :
            CountDownTimer(gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS) {

            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = getFormattedTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }

        timer?.start()
    }

    private fun getFormattedTime(millis: Long): String {
        var seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
        val minutes = TimeUnit.SECONDS.toMinutes(seconds)
        seconds -= TimeUnit.MINUTES.toSeconds(minutes)

        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun finishGame() {
        val isWinner =
            enoughRightAnswersCount.value == true && enoughRightAnswersPercent.value == true
        _gameResult.value = GameResult(isWinner, rightAnswersCount, questionsCount, gameSettings)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
    }
}