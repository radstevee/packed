package net.radstevee.packed.ui.draw

data class RenderPosition(val location: Location, val shift: Double) {
    enum class Location {
        ACTION_BAR, BOSS_BAR
    }
}