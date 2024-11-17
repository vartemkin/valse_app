package com.example.valseapp.webview

import android.app.Activity
import android.content.Context
import com.example.valseapp.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import java.io.InvalidClassException

@Module
@InstallIn(ActivityComponent::class)
object WebViewModule {

    /**
     * Предоставляет JsExecutor если он вызван через @Inject в иерархии зависимостей Activity.
     * Как JsExecutor используется сама Activity, которая является @AndroidEntryPoint для данной
     * иерархии зависимостей.
     * @throws InvalidClassException если Activity не реализует JsExecutor.
     */
    @Provides
    fun bindJsExecutor(activity: Activity): JsExecutor {
        if (activity !is JsExecutor) throw InvalidClassException(
            "Host Activity must implement the JsExecutor interface."
        )
        return activity as JsExecutor
    }
}