package me.mattstudios.triumphchat.api

import me.mattstudios.msg.base.internal.Format
import java.util.UUID

interface ChatPlayer {
    val uuid: UUID

    fun getFormats(): Set<Format>

}