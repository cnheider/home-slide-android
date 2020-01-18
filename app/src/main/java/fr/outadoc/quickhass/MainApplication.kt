package fr.outadoc.quickhass

import android.app.Application
import android.net.nsd.NsdManager
import android.os.Vibrator
import androidx.core.content.getSystemService
import androidx.room.Room
import fr.outadoc.mdi.MaterialIconAssetMapperImpl
import fr.outadoc.mdi.MaterialIconLocator
import fr.outadoc.quickhass.feature.details.vm.EntityDetailViewModel
import fr.outadoc.quickhass.feature.grid.vm.EntityGridViewModel
import fr.outadoc.quickhass.feature.onboarding.rest.DiscoveryRepository
import fr.outadoc.quickhass.feature.onboarding.rest.DiscoveryRepositoryImpl
import fr.outadoc.quickhass.feature.onboarding.rest.HassZeroconfDiscoveryServiceImpl
import fr.outadoc.quickhass.feature.onboarding.rest.ZeroconfDiscoveryService
import fr.outadoc.quickhass.feature.onboarding.vm.*
import fr.outadoc.quickhass.feature.slideover.TileFactory
import fr.outadoc.quickhass.feature.slideover.TileFactoryImpl
import fr.outadoc.quickhass.feature.slideover.rest.EntityRepository
import fr.outadoc.quickhass.feature.slideover.rest.EntityRepositoryImpl
import fr.outadoc.quickhass.persistence.EntityDatabase
import fr.outadoc.quickhass.preferences.PreferenceRepository
import fr.outadoc.quickhass.preferences.PreferenceRepositoryImpl
import fr.outadoc.quickhass.rest.AccessTokenProvider
import fr.outadoc.quickhass.rest.LongLivedTokenProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


@Suppress("unused")
class MainApplication : Application() {

    private val appModule = module {
        single { getSystemService<NsdManager>() }
        single { getSystemService<Vibrator>() }

        single { DiscoveryRepositoryImpl() as DiscoveryRepository }
        single { EntityRepositoryImpl(get(), get(), get()) as EntityRepository }
        single { PreferenceRepositoryImpl(get()) as PreferenceRepository }
        single { Room.databaseBuilder(get(), EntityDatabase::class.java, EntityDatabase.DB_NAME).build() }
        single { HassZeroconfDiscoveryServiceImpl(get()) as ZeroconfDiscoveryService }
        single { LongLivedTokenProviderImpl(get()) as AccessTokenProvider }
        single { TileFactoryImpl() as TileFactory }

        viewModel { WelcomeViewModel() }
        viewModel { HostSetupViewModel(get(), get(), get()) }
        viewModel { AuthSetupViewModel(get(), get()) }
        viewModel { ShortcutSetupViewModel() }
        viewModel { SuccessViewModel(get()) }

        viewModel { EntityGridViewModel(get(), get(), get(), get()) }
        viewModel { EntityDetailViewModel() }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }

        MaterialIconLocator.instance = MaterialIconAssetMapperImpl(applicationContext)
    }
}