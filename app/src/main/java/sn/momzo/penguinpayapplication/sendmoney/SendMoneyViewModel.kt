package sn.momzo.penguinpayapplication.sendmoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import sn.momzo.penguinpayapplication.Currency
import sn.momzo.penguinpayapplication.data.Rate
import sn.momzo.penguinpayapplication.rate.RateRepository
import sn.momzo.penguinpayapplication.utils.BinaryConverter
import java.math.BigDecimal

class SendMoneyViewModel(private val rateRepository: RateRepository, private val binaryConverter: BinaryConverter) : ViewModel(){
    private var rates: Rate? = null

    fun getRates() = liveData(Dispatchers.IO) {
        val rates = try {
            val response = rateRepository.getRates()
            rates = response
            response
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
        emit(rates)
    }

    fun calculateRecipientAmount(amount: String, currency: String) = liveData(Dispatchers.IO) {
        val decimalAmount = binaryConverter.binaryToIntNumber(amount)
        val decimalRate = getRateByCurrency(currency).times(BigDecimal(decimalAmount))
        val binaryAmount = binaryConverter.bigDecimalToBinary(decimalRate)

        emit(binaryAmount)
    }

    fun convertRateToBinary(currency: String): String {
        val rate = getRateByCurrency(currency)
        return  binaryConverter.bigDecimalToBinary(rate)
    }

    private fun getRateByCurrency(currency: String) = when (currency) {
        Currency.KES.name -> rates?.KES ?: BigDecimal.ZERO
        Currency.NGN.name -> rates?.NGN ?: BigDecimal.ZERO
        Currency.TZS.name -> rates?.TZS ?: BigDecimal.ZERO
        Currency.UGX.name -> rates?.UGX ?: BigDecimal.ZERO
        else -> rates?.KES ?: BigDecimal.ZERO
    }
}