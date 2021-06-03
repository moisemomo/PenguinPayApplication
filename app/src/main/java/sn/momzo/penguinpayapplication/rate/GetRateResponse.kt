package sn.momzo.penguinpayapplication.rate

import sn.momzo.penguinpayapplication.data.Rate

data class GetRateResponse(val base: String?,
                           val disclaimer: String?,
                           val license: String?,
                           val rates: Rate?,
                           val timestamp: Int?)
