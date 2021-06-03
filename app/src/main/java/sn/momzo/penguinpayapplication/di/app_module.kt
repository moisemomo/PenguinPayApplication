package sn.momzo.penguinpayapplication.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sn.momzo.penguinpayapplication.Constants
import sn.momzo.penguinpayapplication.rate.RateRepository
import sn.momzo.penguinpayapplication.rate.RatesApi
import sn.momzo.penguinpayapplication.sendmoney.SendMoneyViewModel
import sn.momzo.penguinpayapplication.utils.BinaryConverter
import java.util.concurrent.TimeUnit

val penguinModules = module {
    single { provideRetrofit(get(), get()) }
    single { provideGson() }
    single { provideOkHttpClient() }
    single {
        get<Retrofit>().create(RatesApi::class.java)
    }

    factory { RateRepository(get()) }
    factory { BinaryConverter }

    viewModel {
        SendMoneyViewModel(get(), get())
    }
}

private fun provideGson(): Gson {
    return GsonBuilder()
        .setLenient()
        .create()
}


private fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(Constants.BASE_URL)
        .build()
}


private fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()
}