package core

class Game(
    gameStateFactory: IGameStateFactory,
    private val players: Map<Country, IPlayerStrategy>
) {

    private val gameState = gameStateFactory.create()

    fun start() {
        println("Starting the game!!!")
        var isEndOfGame = false
        while (gameState.rounds <= 100 && !isEndOfGame) {
            isEndOfGame = proceedRound()
        }

    }

    private fun proceedRound(): Boolean {
        println(">> New round #${gameState.rounds} <<")
        for (country in gameState.countryTurnOrder) {
            println("-->> Turn of $country")
            if (country.hasNoArmy(gameState)) {
                println("-->> COUNTRY $country has no army. LOOSER!!!")
                return true
            }
            gameState.currentCountry = country
            players[country]?.move(gameState.toPlayerGameStatus(), PlayerMove(gameState, country))

            println("!!! Game state -> ${gameState.unitPlacements}")
        }
        gameState.rounds++
        return false
    }

    private fun Country.hasNoArmy(gameState: GameStateInternal): Boolean {
        return this@Game.gameState.unitPlacements.getAllUnitOf(this).isEmpty()
    }
}


interface IGameStateFactory {

    fun create(): GameStateInternal
}

class GameStateInternal(
    val map: WorldMap,
    val unitPlacements: IMutableUnitPlacements,
    val countryTurnOrder: List<Country>,
    var currentCountry: Country,
    var turnState: TurnState,
    var rounds: Int,
    var aliasPoints: Int,
    var axisPoints: Int,
    var playersHand: MutableMap<Country, MutableList<Card>>
) {

    fun toPlayerGameStatus(): GameState {
        return GameState(
            map = map,
            unitPlacements = unitPlacements,
            countryTurnOrder = countryTurnOrder,
            currentCountry = currentCountry,
            turnState = turnState,
            rounds = rounds,
            aliasPoints = aliasPoints,
            axisPoints = axisPoints,
            playersHand = playersHand.getValue(currentCountry)
        )
    }
}


enum class TurnState {
    PLAY_CARDS,
    CHECK_SUPPLIES,
    DROP_CARDS,
    TAKE_CARDS
}

interface IPlayerMove {

    fun playCard(card: Card, cardPlayData: IPlayCardData?)
}

class PlayerMove(
    private val gameState: GameStateInternal,
    private val currentCountry: Country
) : IPlayerMove {

    override fun playCard(card: Card, cardPlayData: IPlayCardData?) {
        println("---->> $currentCountry played card $card with $cardPlayData")
        when (card.kind) {
            CardKind.ATTACK_ON_LAND -> handleAttack(card, cardPlayData)
            CardKind.ARMY_CREATION -> handleArmyCreation(card, cardPlayData)
            CardKind.FLEET_CREATION -> TODO("Not implemented")
            CardKind.ATTACK_ON_SEA -> TODO("Not implemented")
            CardKind.COUNTRY_SPECIFIC -> TODO("Not implemented")
        }
        gameState.playersHand[currentCountry]?.remove(card)
    }

    private fun handleAttack(card: Card, cardPlayData: IPlayCardData?) {
        if (cardPlayData !is TerritoryTargetPlayCardData) {
            throw IllegalStateException("Use TerritoryTargetPlayCardData to specify territory")
        }
        if (!cardPlayData.territory.hasNeighbourWithUnitsOf(gameState.unitPlacements, gameState.currentCountry)) {
            return
        }
        val placement = gameState.unitPlacements.getPlacementFor(cardPlayData.territory.id)
        val opponentUnit = placement.getOpponentUnit(currentCountry)
        if (opponentUnit != null) {
            gameState.unitPlacements.removeUnit(cardPlayData.territory.id, opponentUnit)
        }
    }

    private fun handleArmyCreation(card: Card, cardPlayData: IPlayCardData?) {
        if (cardPlayData !is TerritoryAndUnitTargetPlayCardData) {
            throw IllegalStateException("Use TerritoryAndUnitTargetPlayCardData to specify territory")
        }
        val placement = gameState.unitPlacements.getPlacementFor(cardPlayData.territory.id)
        if (!cardPlayData.territory.hasNeighbourWithUnitsOf(gameState.unitPlacements, currentCountry)) {
            return
        }
        if (!placement.isFull() &&
            !placement.isCorruptedBy(currentCountry) &&
            !placement.isCorruptedByOpponent(currentCountry)
        ) {
            gameState.unitPlacements.putUnit(
                territoryId = cardPlayData.territory.id,
                unit = cardPlayData.unit
            )
        }
    }
}

interface IPlayCardData

data class TerritoryTargetPlayCardData(val territory: Territory) : IPlayCardData

data class TerritoryAndUnitTargetPlayCardData(val territory: Territory, val unit: Unit) : IPlayCardData