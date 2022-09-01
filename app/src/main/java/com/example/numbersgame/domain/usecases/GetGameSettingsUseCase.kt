package com.example.numbersgame.domain.usecases

import com.example.numbersgame.domain.entities.GameSettings
import com.example.numbersgame.domain.entities.Level
import com.example.numbersgame.domain.repository.GameRepository

class GetGameSettingsUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return gameRepository.getGameSettings(level)
    }
}