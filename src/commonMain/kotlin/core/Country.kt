package core

enum class Country(val side: WarSide) {
    USSR(side = WarSide.ALLIES),
    USA(side = WarSide.ALLIES),
    GREAT_BRITAIN(side = WarSide.ALLIES),
    GERMANY(side = WarSide.AXIS),
    ITALY(side = WarSide.AXIS),
    JAPAN(side = WarSide.AXIS)
}

enum class WarSide {
    ALLIES,
    AXIS;

    fun isOpponentOf(side: WarSide): Boolean {
        return this != side
    }

    fun isTeammateOf(side: WarSide): Boolean {
        return this == side
    }
}