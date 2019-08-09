package core

fun Territory.getPlacement(unitPlacements: IUnitPlacements): UnitPlacement {
    return unitPlacements.getPlacementFor(id)
}

fun Territory.isCorruptedByOpponent(unitPlacements: UnitPlacements, yourCountry: Country): Boolean {
    return getPlacement(unitPlacements).isCorruptedByOpponent(yourCountry)
}

fun Territory.hasNeighbourWithUnitsOf(unitPlacements: IUnitPlacements, country: Country): Boolean {
    return neighbours.any { it.getPlacement(unitPlacements).isContainUnitOf(country) }
}

fun Territory.isCorruptedBy(unitPlacements: IUnitPlacements, country: Country): Boolean {
    return getPlacement(unitPlacements).isCorruptedBy(country)
}

fun Territory.isContainUnitOf(unitPlacements: IUnitPlacements, country: Country): Boolean {
    return getPlacement(unitPlacements).isContainUnitOf(country)
}

fun Territory.getOpponentUnit(unitPlacements: IUnitPlacements, country: Country): Unit? {
    return getPlacement(unitPlacements).getOpponentUnit(country)
}

fun UnitPlacement.isCorruptedBy(country: Country): Boolean {
    return any { it != null && it.country == country }
}

fun UnitPlacement.isCorruptedByOpponent(yourCountry: Country): Boolean {
    return any { it != null && it.country.isOpponentOf(yourCountry) }
}

fun UnitPlacement.isContainUnitOf(country: Country): Boolean {
    return any { it != null && it.country == country }
}

fun UnitPlacement.getOpponentUnit(country: Country): Unit? {
    return firstOrNull { unit ->
        unit != null && country.side.isOpponentOf(unit.country.side)
    }
}

fun Country.isOpponentOf(country: Country): Boolean = this.side.isOpponentOf(country.side)

fun Country.isTeammateOf(country: Country): Boolean = this.side.isTeammateOf(country.side)