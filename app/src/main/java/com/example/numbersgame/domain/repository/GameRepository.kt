package com.example.numbersgame.domain.repository

import com.example.numbersgame.domain.entities.GameSettings
import com.example.numbersgame.domain.entities.Level
import com.example.numbersgame.domain.entities.Question

interface GameRepository {
    fun getGameSettings(level: Level): GameSettings
    fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question
}