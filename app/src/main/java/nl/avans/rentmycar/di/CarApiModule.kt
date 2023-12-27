package nl.avans.rentmycar.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.avans.rentmycar.model.Car
import nl.avans.rentmycar.network.CarApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CarApiModule {

    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder): CarApi {
        return builder
            .build()
            .create(CarApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .addConverterFactory(MoshiConverterFactory.create())
    }
}