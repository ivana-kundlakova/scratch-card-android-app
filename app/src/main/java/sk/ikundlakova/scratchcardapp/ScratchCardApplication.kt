package sk.ikundlakova.scratchcardapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import sk.ikundlakova.scratchcardapp.data.di.dataModule
import sk.ikundlakova.scratchcardapp.ui.di.uiModule

class ScratchCardApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ScratchCardApplication)
            modules(
                dataModule,
                uiModule
            )
        }
    }
}