package me.zeepic.aiparkour.messaging

import me.zeepic.aiparkour.AIParkour
import me.zeepic.aiparkour.util.times
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType

fun String.title()
        = split(" ").joinToString(" ") { it[0].uppercase() + it.drop(1) }

fun String.withoutColor() = LegacyComponentSerializer.legacySection().deserialize(this).serialize()

fun String.color() = LegacyComponentSerializer.legacy('&').deserialize(this).serialize()

fun String.enumNameTitle() = lowercase().replace("_"," ").title()

fun String.toEntityType() = EntityType.values().firstOrNull { this == it.name.lowercase() }

fun String.toMaterial() = Material.values().firstOrNull { this == it.name.lowercase() }

fun String.limit(amount: Int) = if (length > amount) this.take(amount - 3) + "..." else this

fun String.pad(amount: Int) = (" " * amount) + this + (" " * amount)

fun Component.serialize() = PlainTextComponentSerializer.plainText().serialize(this)

val String.component get() = Component.text(this.color())
fun String.component(color: NamedTextColor) = this.component.color(color)

fun Component.hoverText(text: String)
    = this.hoverEvent(HoverEvent.showText(text.component(NamedTextColor.GRAY)))

val Location.component
    get() = "(${this.blockX}, ${this.blockY}, ${this.blockZ})"
        .component(NamedTextColor.BLUE)
        .hoverText("Click to teleport!")
        .clickEvent(ClickEvent.runCommand("/tp ${this.x} ${this.y} ${this.z}"))

operator fun Component.plus(other: Component) = this.append(other)

operator fun Collection<Component>.plus(other: Component): List<Component> {
    val mut = this.toMutableList()
    mut += other
    return mut.toList()
}

fun Collection<Component>.join() = this.reduce { acc, component -> acc + component }

fun <M> CommandSender.send(message: M) {
    val component = if (message is Component) message
                  else message.toString().component
    this.sendMessage(
        AIParkour.messagePrefix
            .append(component)
            .hoverText("Server Message")
    )
}

fun <M> log(message: M) = Bukkit.getLogger().info(message.toString())

fun <M> broadcast(message: M) {
    Bukkit.broadcast(
        if (message is Component) message
        else message.toString().component
    )
}
