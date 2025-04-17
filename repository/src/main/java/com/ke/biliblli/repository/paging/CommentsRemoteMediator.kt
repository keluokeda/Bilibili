package com.ke.biliblli.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler
import com.ke.biliblli.db.dao.CommentDao
import com.ke.biliblli.db.entity.CommentEntity

@OptIn(ExperimentalPagingApi::class)
class CommentsRemoteMediator(
    private val bilibiliRepository: BilibiliRepository,
    private val oid: Long,
    private val type: Int,
    private val commentDao: CommentDao,
    var sort: Int = 0
) : RemoteMediator<Int, CommentEntity>() {
    private var index = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CommentEntity>
    ): MediatorResult {

        try {
            when (loadType) {
                LoadType.REFRESH -> {
                    commentDao.deleteAll(oid, type)
                    index = 1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    index += 1
                }
            }


            val response = bilibiliRepository.comments(index, oid, type, sort)


            val list = response.data!!.replies ?: emptyList()

            commentDao.insertAll(
                list.map {
                    CommentEntity(
                        commentId = it.rpid,
                        oid = it.oid,
                        type = it.type,
                        username = it.member.name,
                        avatar = it.member.avatar,
                        parent = it.parent,
                        liked = it.action == 1,
                        like = it.like.toLong(),
                        date = it.ctime,
                        ip = it.replyControl.location,
                        timeDesc = it.replyControl.timeDesc,
                        content = it.content.message,
                        userId = it.member.mid.toLong(),
                        userSign = it.member.sign
                    )
                }
            )

            return MediatorResult.Success(
                endOfPaginationReached =
                    list.isEmpty()
            )
        } catch (e: Exception) {
//            e.printStackTrace()
            CrashHandler.handler(e)
            return MediatorResult.Error(e)
        }
    }
}