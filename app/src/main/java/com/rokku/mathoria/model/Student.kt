package com.rokku.mathoria.model

// Main Student Data Model
data class Student(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val birthday: String = "",
    val schoolGrade: String = "",
    val linkedTeacherId: String = "",
    val playerProfile: PlayerProfile = PlayerProfile(),
    val gameProgress: Map<String, GameProgress> = emptyMap(),
    val achievements: Achievements = Achievements()
)

// Player Profile with Game Stats
data class PlayerProfile(
    val playerName: String = "",
    val gameLevel: Int = 0,
    val mathLevel: Int = 0,
    val coins: Int = 0,
    val questionsSolved: Int = 0,
    val skillsToImprove: List<String> = emptyList(),
    val rewardProfile: RewardProfile = RewardProfile()
)

// Reward Profile for Player Achievements
data class RewardProfile(
    val score: Int = 0,
    val rank: Int = 0,
    val iScore: Int = 0,
    val rewardCount: Int = 0,
    val positives: Int = 0,
    val negatives: Int = 0
)

// Game Progress Data for Each Mini-Game
data class GameProgress(
    val lastScore: Int = 0,
    val bestScore: Int = 0,
    val completedAt: String = ""
)

// Achievements Data
data class Achievements(
    val badges: List<String> = emptyList()
)
