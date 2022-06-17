package com.app.myapplication

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.internal.threadFactory
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.ExecutorService
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ClientProvider {

    @Provides
    @Singleton
    fun provideExecutorService(priorityQueue: PriorityBlockingQueue<Runnable>): ExecutorService {
        return ThreadPoolExecutor(
            0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
            priorityQueue, threadFactory("Custom Dispatcher", false)
        )
    }

    @Provides
    @Singleton
    fun provideExecutorServiceDecorator(executorService: ExecutorService): ExecutorServiceDecorator {
        return ExecutorServiceDecorator(executorService)
    }

    @Provides
    @Singleton
    fun providePriorityQueue(): PriorityBlockingQueue<Runnable> {
        return PriorityBlockingQueue<Runnable>(
            11, compareByDescending {
                (it as? PriorityRunner)?.priority
            }

        )
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptorClient(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun createCustomInterceptorClient(
        interceptor: HttpLoggingInterceptor,
        executorService: ExecutorService
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                dispatcher(Dispatcher(executorService))
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }
}