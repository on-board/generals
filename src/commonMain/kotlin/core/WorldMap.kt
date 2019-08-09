package core

data class WorldMap(
    val territories: List<Territory>
)

data class Territory(
    val id: TerritoryId,
    val isLand: Boolean,
    val neighbours: MutableList<Territory>
) {

    override fun toString(): String {
        return "Territory(id=$id)"
    }
}

enum class TerritoryId {
    MOSCOW,
    GERMANY,
    ITALY,
    WESTERN_EUROPE,
    EASTERN_EUROPE,
    GREAT_BRITAIN,
    SOUTH_USSR,
    SIBERIA,
    EASTERN_CHINA,
    JAPAN,
    BALKANS
    // TODO
}