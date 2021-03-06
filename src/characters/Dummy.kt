package characters

import observables.Health
import observables.Mana
import observers.HealthBar

class Dummy : ICharacter() {

    init {
        type = "Dummy"
        health = Health(1000000)
        health.addObserver(HealthBar(type))
    }
}