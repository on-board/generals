package ai

import core.*
import kotlin.random.Random

class AttackAi : IPlayerStrategy {

    private val random = Random(2)

    override fun onGameStart(yourCountry: Country, map: WorldMap) {
    }

    override fun move(gameState: GameState, move: IPlayerMove) {
        if (gameState.playersHand.isEmpty()) return
        val randomCard = gameState.playersHand.random(random)
        when (randomCard.kind) {
            CardKind.ATTACK_ON_LAND -> {
                handleAttack(gameState, move, randomCard)
            }
            CardKind.ARMY_CREATION -> {
                move.playCard(
                    card = randomCard,
                    cardPlayData = TerritoryAndUnitTargetPlayCardData(
                        territory = gameState.map.territories.random(random),
                        unit = Unit(0, gameState.currentCountry, UnitType.ARMY)
                    )
                )
            }
            else -> throw IllegalStateException("Unknown card")
        }
    }

    private fun handleAttack(gameState: GameState, move: IPlayerMove, card: Card) {
        val myTerritories = getCountryTerritoriesWithUnit(gameState, gameState.currentCountry)
        val enemyTerritories = getCountryTerritoriesWithUnit(
            gameState = gameState,
            countries = Country.values().filter { it.side.isOpponentOf(gameState.currentCountry.side) }
        )
        val targetTerritory = enemyTerritories.firstOrNull { enemyTerritory ->
            myTerritories.any { it.neighbours.contains(enemyTerritory) }
        }
        move.playCard(
            card = card,
            cardPlayData = TerritoryTargetPlayCardData(
                territory = targetTerritory ?: gameState.map.territories.random(random)
            )
        )
    }

    private fun getCountryTerritoriesWithUnit(gameState: GameState, country: Country): List<Territory> {
        return gameState.map.territories
            .filter { territory -> territory.isCorruptedBy(gameState.unitPlacements, country) }
    }

    private fun getCountryTerritoriesWithUnit(gameState: GameState, countries: List<Country>): List<Territory> {
        return gameState.map.territories
            .filter { territory -> countries.any { territory.isCorruptedBy(gameState.unitPlacements, it) } }
    }
}