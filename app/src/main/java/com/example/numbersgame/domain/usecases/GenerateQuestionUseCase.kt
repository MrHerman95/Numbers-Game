package com.example.numbersgame.domain.usecases

import com.example.numbersgame.domain.entities.Question
import com.example.numbersgame.domain.repository.GameRepository

class GenerateQuestionUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(maxSumValue: Int): Question {
        return gameRepository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}