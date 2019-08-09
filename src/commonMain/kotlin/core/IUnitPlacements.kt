package core

interface IMutableUnitPlacements : IUnitPlacements {

    fun putUnit(territoryId: TerritoryId, unit: Unit)

    fun removeUnit(territoryId: TerritoryId, unit: Unit)
}

interface IUnitPlacements {

    fun getPlacementFor(territoryId: TerritoryId): UnitPlacement

    fun getAllUnitOf(country: Country): List<Unit>
}

class UnitPlacements : IMutableUnitPlacements {

    private val placements: MutableMap<TerritoryId, UnitPlacement> = mutableMapOf()

    override fun getPlacementFor(territoryId: TerritoryId): UnitPlacement {
        return placements.getOrElse(territoryId) { UnitPlacement.EMPTY }
    }

    override fun putUnit(territoryId: TerritoryId, unit: Unit) {
        val placement = getPlacementFor(territoryId)
        val newPlacement = placement.put(unit)
        placements[territoryId] = newPlacement
    }

    override fun removeUnit(territoryId: TerritoryId, unit: Unit) {
        val placement = getPlacementFor(territoryId)
        val idx = placement.indexOfFirst { it == unit }
        when (idx) {
            0 -> placement.copy(first = placement.second)
            1 -> placement.copy(second = null)
            else -> throw IllegalStateException("No unit on territory")
        }.also { placements[territoryId] = it }
    }

    override fun getAllUnitOf(country: Country): List<Unit> {
        return placements.values
            .asSequence()
            .map { placement -> placement.firstOrNull { it != null && it.country == country } }
            .filterNotNull()
            .toList()
    }

    private fun UnitPlacement.put(unit: Unit): UnitPlacement {
        return when {
            first == null -> UnitPlacement(first = unit, second = null)
            second == null -> copy(second = unit)
            else -> throw IllegalStateException("Placement is full")
        }
    }

    override fun toString(): String {
        return buildString {
            append("UnitPlacements(placements=")
            append("\n")
            placements.forEach { keyValue ->
                append("\t")
                append(keyValue.key)
                append(" -->\t\t")
                append(keyValue.value)
                append("\n")
            }
        }
    }
}

data class UnitPlacement(
    val first: Unit?,
    val second: Unit?
) : Iterable<Unit?> {

    override fun iterator(): Iterator<Unit?> = UnitPlacementIterator(this)

    fun isEmpty(): Boolean {
        return first == null && second == null
    }

    fun isFull(): Boolean {
        return first != null && second != null
    }

    companion object {

        val EMPTY = UnitPlacement(null, null)
    }

    override fun toString(): String {
        return when {
            isEmpty() -> "[-]"
            isFull() -> "[$first,$second]"
            else -> "[$first]"
        }

    }

    class UnitPlacementIterator(private val unitPlacement: UnitPlacement) : Iterator<Unit?> {

        private var idx = 0

        override fun hasNext(): Boolean = idx < 2

        override fun next(): Unit? {
            return when (idx) {
                0 -> unitPlacement.first
                1 -> unitPlacement.second
                else -> throw IllegalStateException("Incorrect state")
            }.also { idx++ }
        }
    }
}
