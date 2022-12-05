package ir.amirhparhizgar.snappboxtask.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.amirhparhizgar.snappboxtask.domain.CloudMessageHandler
import ir.amirhparhizgar.snappboxtask.domain.CloudMessageHandlerImpl

/**
 * Created by AmirHossein Parhizgar on 12/5/2022.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Reusable
    @Binds
    abstract fun bindCloudMessageHandler(cloudMessageHandlerImpl: CloudMessageHandlerImpl): CloudMessageHandler
}