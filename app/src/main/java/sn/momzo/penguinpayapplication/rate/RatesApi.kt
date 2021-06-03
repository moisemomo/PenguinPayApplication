package sn.momzo.penguinpayapplication.rate

import retrofit2.http.GET
import retrofit2.http.Query
import sn.momzo.penguinpayapplication.Constants

interface RatesApi {
    @GET(RATES)
    suspend fun getRates(
        @Query("app_id") appId : String = Constants.OPEN_EXCHANGE_RATE_APP_ID,
        @Query("symbols") neededCurrenciesRate: String = CURRENCY_SYMBOLS): GetRateResponse


    companion object {
        const val CURRENCY_SYMBOLS= "KES,NGN,TZS,UGX"
        const val RATES = "latest.json"
    }
}