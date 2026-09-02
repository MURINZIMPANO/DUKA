package com.duka.app.data.repository

import com.duka.app.data.local.dao.ChatMessageDao
import com.duka.app.data.local.entity.ChatMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatMessageDao: ChatMessageDao
) {
    suspend fun sendMessage(message: ChatMessage): Long = chatMessageDao.insert(message)
    fun getMessages(businessId: Long): Flow<List<ChatMessage>> =
        chatMessageDao.getMessagesByBusiness(businessId)
    suspend fun deleteClientMessages(businessId: Long) =
        chatMessageDao.deleteClientMessages(businessId)
}
