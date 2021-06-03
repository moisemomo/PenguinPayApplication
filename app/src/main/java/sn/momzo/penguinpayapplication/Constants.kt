package sn.momzo.penguinpayapplication

import sn.momzo.penguinpayapplication.data.Country

object Constants {
    const val BASE_URL= "https://openexchangerates.org/api/"
    const val OPEN_EXCHANGE_RATE_APP_ID = "9c112dc63cfa4e739144cae9197dcea7"

    val countries = listOf(
        Country("Kenya",Currency.KES.name, 254,9,"### ######"),
        Country("Nigeria",Currency.NGN.name, 234,7,"### ####"),
        Country("Tanzania",Currency.TZS.name, 255,9,"### ######"),
        Country("Uganda",Currency.UDX.name, 256,7,"### ####")
    )
}
enum class Currency{ KES, NGN, TZS,UDX}