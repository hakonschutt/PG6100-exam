package org.bjh.dto

import java.io.Serializable

data class MessageDto(var msgs: List<String> = listOf(), var fromUser: String? = null, var toUser: String? = null, var id: String? = null)
    :Serializable