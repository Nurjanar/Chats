object ChatService {
    private var chats = mutableListOf<Chats>()
    private var chatCounter = 0
    private var messageCounter = 0

    private fun createChat(chat: Chats): Chats {
        chats += chat.copy(
            chatId = ++chatCounter
        )
        return chats.last()
    }

    fun deleteMessage(messageId: Int): Boolean {
        var result = false
        chats.forEach { chat ->
            result = chat.directMessages.remove(
                chat.directMessages.find {
                    it.messageId == messageId
                })
            chat.unReadMessages -= 1
        }
        if (!result) {
            throw MessageNotFoundException()
        }
        return true
    }

    fun deleteChat(chatId: Int, fromId: Int): Boolean {
        chats.remove(chats.find {
            it.chatId == chatId &&
                    it.fromId == fromId
        }
            ?: throw ChatNotFoundException())
        return true
    }

    fun getChatById(fromId: Int): List<Message> {
        val list = mutableListOf<Message>()
        val chat = chats.find { it.fromId == fromId }
            ?: throw ChatNotFoundException()
        chat.directMessages.forEach { sms ->
            sms.unRead = false
            list.add(sms)
        }
        chat.unReadMessages = 0
        return list
    }

    fun getUnreadChatsCount(): Int {
        var count = 0
        chats.forEach { chat ->
            if (chat.unReadMessages > 0) {
                count += 1
            }
        }
        return count
    }

    fun getLastMessage(): List<String> {
        val lastMessage = mutableListOf("")
        var sms: String
        chats.forEachIndexed { _: Int, chat: Chats ->
            val result = chat.directMessages.findLast { true }?.text
            sms = if (result.isNullOrEmpty()) {
                "Нет сообщений"
            } else {
                result
            }
            lastMessage.add(sms + "\n")
        }
        return lastMessage
    }

    fun getChats(): List<Chats> {
        val result = chats.sortedWith(compareByDescending<Chats> {
            it.unReadMessages
        })
        return result
    }

    fun createMessage(text: String, fromId: Int): Message {
        val message = Message(
            text = text,
            fromId = fromId,
            messageId = ++messageCounter
        )
        val result = chats.find { it.fromId == fromId }
        if (result == null) {
            createChat(
                Chats(
                    fromId = fromId,
                    directMessages = mutableListOf(message),
                    unReadMessages = 1
                )
            )
        } else {
            result.directMessages += message
            result.unReadMessages += 1
        }

        return message
    }

    fun editMessage(fromId: Int, messageId: Int, newText: String): Boolean {
        chats.forEach { chat ->
            val message = chat.directMessages.find {
                it.fromId == fromId &&
                        it.messageId == messageId
            }
            message?.text = newText
        }
        return true
    }

    fun clear() {
        chats.clear()
        chatCounter = 0
        messageCounter = 0
    }
}