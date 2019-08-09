package ai

import core.*
import kotlin.random.Random

class SimpleAi : IPlayerStrategy {

    private val random = Random(1)

    override fun onGameStart(yourCountry: Country, map: WorldMap) {
    }

    override fun move(gameState: GameState, move: IPlayerMove) {
        if (gameState.playersHand.isEmpty()) return
        val randomCard = gameState.playersHand.random(random)
        when (randomCard.kind) {
            CardKind.ATTACK_ON_LAND -> {
                move.playCard(
                    card = randomCard,
                    cardPlayData = TerritoryTargetPlayCardData(
                        territory = gameState.map.territories.random(random)
                    )
                )
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
}