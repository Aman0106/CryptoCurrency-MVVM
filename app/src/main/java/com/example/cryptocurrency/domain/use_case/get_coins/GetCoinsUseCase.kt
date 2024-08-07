package com.example.cryptocurrency.domain.use_case.get_coins

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.data.remote.dto.toCoin
import com.example.cryptocurrency.domain.model.Coin
import com.example.cryptocurrency.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke() : Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading())
            val coins = repository.getCoins().map { it.toCoin() }
            emit(Resource.Success(coins))
        }
        catch (e: HttpException) {
            //When the response is of not type 2(Success)

            emit(Resource.Error(message = e.localizedMessage ?: "Unexpected Error"))
        }
        catch (e: IOException)  {
            // When Can not connect to the server i.e internet
            emit(Resource.Error(message = "Internet error"))
        }
    }
}