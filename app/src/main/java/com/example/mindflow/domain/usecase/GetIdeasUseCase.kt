package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.repository.IdeaRepository
import com.example.mindflow.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetIdeasUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ideaRepository: IdeaRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Idea>> {
        return flow {
            val user = userRepository.getActiveSession()
            emit(user)
        }.flatMapLatest { user ->
            if (user != null) {
                ideaRepository.getIdeasFlow(user.id)
            } else {
                flowOf(emptyList())
            }
        }
    }
}