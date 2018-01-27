package io.github.droidkaigi.confsched2018.util.ext

import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import kotlin.reflect.KProperty0


fun <T> KotprefModel.asFlowable(property: KProperty0<T>): Flowable<T> {
    return Flowable.create<T>({ emitter ->
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == property.name) {
                emitter.onNext(property.get())
            }
        }
        emitter.setCancellable {
            Prefs.preferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
        Prefs.preferences.registerOnSharedPreferenceChangeListener(listener)
    }, BackpressureStrategy.LATEST)
}
