package net.radstevee.packed.ui.draw

data class RenderPosition(val location: Location, val verticalShift: Double = 0.0, val horizontalShift: Int = 0) {
    enum class Location {
        ACTION_BAR, BOSS_BAR
    }
}