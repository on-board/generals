package cli

import ai.AttackAi
import ai.SimpleAi
import core.*

fun main() {
    Game(
        gameStateFactory = GameFactory(),
        players = mapOf(
            Country.USSR to AttackAi(),
            Country.GERMANY to SimpleAi(),
            Country.ITALY to SimpleAi()
        )
    ).start()
}

private object Map {

    val moscow = Territory(TerritoryId.MOSCOW, isLand = true, neighbours = mutableListOf())
    val southUssr = Territory(TerritoryId.SOUTH_USSR, isLand = true, neighbours = mutableListOf(moscow))
    val easternEurope = Territory(TerritoryId.EASTERN_EUROPE, isLand = true, neighbours = mutableListOf(southUssr))
    val germany = Territory(TerritoryId.GERMANY, isLand = true, neighbours = mutableListOf(easternEurope))
    val italy = Territory(TerritoryId.ITALY, isLand = true, neighbours = mutableListOf(easternEurope, germany))
    val westernEurope = Territory(TerritoryId.WESTERN_EUROPE, isLand = true, neighbours = mutableListOf(italy, germany))

    val worldMap = WorldMap(territories = listOf(moscow, southUssr, easternEurope, germany, italy, westernEurope))

    init {
        moscow.neighbours.add(southUssr)
        southUssr.neighbours.add(easternEurope)
        easternEurope.neighbours.addAll(listOf(germany, italy))
        germany.neighbours.addAll(listOf(italy, westernEurope))
        italy.neighbours.addAll(listOf(westernEurope))
    }
}

private class GameFactory : IGameStateFactory {

    override fun create(): GameStateInternal {
        return GameStateInternal(
            map = Map.worldMap,
            unitPlacements = createPlacements(),
            countryTurnOrder = listOf(Country.USSR, Country.GERMANY, Country.ITALY),
            currentCountry = Country.USSR,
            turnState = TurnState.CHECK_SUPPLIES,
            rounds = 0,
            axisPoints = 0,
            aliasPoints = 0,
            playersHand = createPlayerHands()
        )
    }

    private fun createPlayerHands(): MutableMap<Country, MutableList<Card>> {
        return mutableMapOf(
            Country.USSR to mutableListOf<Card>().apply {
                (0..120).map { Card(0, Country.USSR, CardKind.ARMY_CREATION) }.also { addAll(it) }
                (0..80).map { Card(0, Country.USSR, CardKind.ATTACK_ON_LAND) }.also { addAll(it) }
                shuffle()
            },
            Country.GERMANY to mutableListOf<Card>().apply {
                (0..80).map { Card(0, Country.GERMANY, CardKind.ARMY_CREATION) }.also { addAll(it) }
                (0..120).map { Card(0, Country.GERMANY, CardKind.ATTACK_ON_LAND) }.also { addAll(it) }
                shuffle()
            },
            Country.ITALY to mutableListOf<Card>().apply {
                (0..60).map { Card(0, Country.ITALY, CardKind.ARMY_CREATION) }.also { addAll(it) }
                (0..140).map { Card(0, Country.ITALY, CardKind.ATTACK_ON_LAND) }.also { addAll(it) }
                shuffle()
            }
        )
    }

    private fun createPlacements(): IMutableUnitPlacements {
        return UnitPlacements().apply {
            putUnit(TerritoryId.MOSCOW, Unit(0, Country.USSR, UnitType.ARMY))
            putUnit(TerritoryId.GERMANY, Unit(0, Country.GERMANY, UnitType.ARMY))
            putUnit(TerritoryId.ITALY, Unit(0, Country.ITALY, UnitType.ARMY))
        }
    }
}