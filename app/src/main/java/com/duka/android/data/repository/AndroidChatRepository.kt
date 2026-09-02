package com.duka.android.data.repository

import com.duka.app.data.local.dao.ChatMessageDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.ChatRepository
import com.duka.shared.domain.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidChatRepository @Inject constructor(
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {

    override suspend fun sendMessage(message: ChatMessage): Long =
        chatMessageDao.insert(message.toRoom())

    override fun getMessages(businessId: Long): Flow<List<ChatMessage>> =
        chatMessageDao.getMessagesByBusiness(businessId).map { list -> list.map { it.toShared() } }

    override suspend fun deleteClientMessages(businessId: Long) =
        chatMessageDao.deleteClientMessages(businessId)
}
