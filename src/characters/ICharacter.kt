package characters

import equipments.EquipmentSlot
import equipments.IEquipment
import equipments.IEquipmentFactory
import equipments.weapons.IWeapon
import observables.Health
import observables.Mana
import spells.ISpell

// Receiver for command pattern
abstract class ICharacter {

    var name: String = "Unknown Name"
    var type: String = "Unknown Type"
    lateinit var health: Health
    var mana: Mana? = null
    var target: ICharacter? = null

    private var equipments = HashMap<EquipmentSlot, IEquipment>()

    fun equip(slot: EquipmentSlot, name: String) {
        /*
        ICharacter doesn't know or care about the equipment itself.
        It doesn't need to know even if it is a weapon or an armor.
        It just gets the equipment and assigns it to corresponding slot.
         */

        val factory = IEquipmentFactory.getFactory(slot)

        val equipment = factory.getEquipment(name)

        if (equipment == null) {
            println("Unknown Item")
            return
        } else if (equipment.slot != slot) {
            println("Can't equip item to that slot")
            return
        }

        if (equipments.containsKey(slot)) {
            println("An item is already equipped in that slot")
        } else {
            equipments[slot] = equipment
            println("${equipment.name} is equipped")
        }
    }

    fun unequip(slot: EquipmentSlot) {
        equipments.remove(slot)
        println("Item unequipped")
    }

    fun getWeapon() : IWeapon? {
        if (equipments.containsKey(EquipmentSlot.TWO_HAND)) {
            return equipments[EquipmentSlot.TWO_HAND] as IWeapon?
        } else if (equipments.containsKey(EquipmentSlot.RIGHT_HAND)) {
            return equipments[EquipmentSlot.RIGHT_HAND] as IWeapon?
        } else {
            return null
        }
    }

    fun updateEquipment(equipment: IEquipment) {
        equipments[equipment.slot] = equipment
    }

    fun hit() {
        val weapon = getWeapon()

        if(target != null && weapon != null){
            val damage = weapon.calculateDamage()
            target!!.health.decrease(damage)
        }
    }

    fun move(){
        println("$type moved")
    }

    fun cast(spell: ISpell){
        if (mana != null && target != null) {
            if (mana!!.getValue() > spell.mana) {
                mana!!.decrease(spell.mana)
                spell.cast()
            } else {
                println("Not enough mana")
            }
        }
    }
}