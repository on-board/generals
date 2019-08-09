package core

data class Card(
    val id: Int,
    val country: Country,
    val kind: CardKind
)

enum class CardKind {
    ARMY_CREATION,
    FLEET_CREATION,
    ATTACK_ON_LAND,
    ATTACK_ON_SEA,
    COUNTRY_SPECIFIC // TODO create class for country specific cards or specify all cards there
}

