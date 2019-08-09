package core

interface IPlayerStrategy {

    fun onGameStart(yourCountry: Country, map: WorldMap)

    fun move(gameState: GameState, move: IPlayerMove)
}

data class GameState(
    val map: WorldMap,
    val unitPlacements: IUnitPlacements,
    val countryTurnOrder: List<Country>,
    val currentCountry: Country,
    val turnState: TurnState,
    val rounds: Int,
    val aliasPoints: Int,
    val axisPoints: Int,
    val playersHand: List<Card>
)