class ChatNotFoundException(
    message: String = "Переписка не найдена!"
) : RuntimeException(message)

class MessageNotFoundException(
    message: String = "Сообщение не найдено!"
) : RuntimeException(message)