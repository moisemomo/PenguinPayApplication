package sn.momzo.penguinpayapplication.data

data class Country(val name : String = "",
                   val currencySymbol : String = "",
                   val phoneNumberPrefix : Int = 0,
                   val phoneNumberLength : Int = 0,
                   val phoneNumberFormat : String = "")
