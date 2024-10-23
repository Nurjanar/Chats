import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse

class ChatServiceTest {
    private val service = ChatService

    @Before
    fun clearBeforeTest() {
        service.clear()
    }

    @Test
    fun createMessage() {
        val text = "Привет"
        val message = service.createMessage(text, 1)
        val result = message.messageId
        assertEquals(1, result)
    }

    @Test
    fun editMessage() {
        val text = "СУР 265"
        service.createMessage("Галя, отмена!", 1)
        val result = service.editMessage(1, 1, text)
        assertTrue(result)
    }

    @Test
    fun getChatById() {
        val text = "Ульяна +799"
        var result = true
        service.createMessage(text, 1)
        val list = service.getChatById(1, 1)
        list.forEach { sms ->
            result = sms.unRead
        }
        assertFalse(result)
    }

    @Test
    fun getUnreadChatsCount() {
        service.createMessage("Скучно", 1)
        val result = service.getUnreadChatsCount()
        assertEquals(1, result)
    }

    @Test
    fun getLastMessage() {
        service.createMessage("Галя, отмена!", 1)
        service.createMessage("Галя, скорее!", 1)
        service.createMessage("Просят книгу жалоб", 2)

        val result = service.getLastMessage().size

        assertEquals(2, result)
    }

    @Test
    fun getChats() {
        service.createMessage("Галя, отмена!", 1)
        val list = service.getChats()
        val result = list.size
        assertEquals(1, result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessage() {
        service.deleteMessage(1, 1)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChat() {
        service.deleteChat(1, 1)
    }

}