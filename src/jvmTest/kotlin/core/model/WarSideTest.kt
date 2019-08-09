package core.model

import core.WarSide
import org.junit.Assert.assertTrue
import org.junit.Test

class WarSideTest {

    @Test
    fun isOpponentOf() {
        assertTrue(WarSide.ALLIES.isOpponentOf(WarSide.AXIS))
        assertTrue(WarSide.AXIS.isOpponentOf(WarSide.ALLIES))
    }

    @Test
    fun isTeammateOf() {
        assertTrue(WarSide.ALLIES.isTeammateOf(WarSide.ALLIES))
        assertTrue(WarSide.AXIS.isTeammateOf(WarSide.AXIS))
    }
}