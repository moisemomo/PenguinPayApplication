package sn.momzo.penguinpayapplication.rate

class RateRepository(private val ratesApi: RatesApi) {
    suspend fun getRates() = ratesApi.getRates().rates
}