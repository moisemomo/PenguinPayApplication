package sn.momzo.penguinpayapplication

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import sn.momzo.penguinpayapplication.di.penguinModules

class PenguinPayApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@PenguinPayApplication)
            modules(penguinModules)
        }
    }
    private fun initLogger() {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}