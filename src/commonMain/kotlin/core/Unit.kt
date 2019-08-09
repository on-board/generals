package core

data class Unit(
    val id: Int,
    val country: Country,
    val type: UnitType
)

enum class UnitType {
    ARMY,
    FLEET
}
