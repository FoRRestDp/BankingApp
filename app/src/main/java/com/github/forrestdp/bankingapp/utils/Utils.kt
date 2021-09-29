package com.github.forrestdp.bankingapp.utils

import android.content.Context
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.github.forrestdp.bankingapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun ImageView.loadWithCrossfade(url: String) =
    load(url) {
        crossfade(true)
    }

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit,
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}

fun ImageView.setCardTypeImage(cardType: String?) {
    if (cardType != null) {
        setImageResource(when (cardType) {
            "mastercard" -> R.drawable.ic_mastercard_icon
            "visa" -> R.drawable.ic_visa_icon
            "unionpay" -> R.drawable.ic_unionpay_icom
            else -> R.drawable.ic_blue_circle
        })
    }
}

fun Fragment.getDrawable(@DrawableRes id: Int) =
    ContextCompat.getDrawable(requireContext(), id)

@ColorInt
fun Fragment.getColor(@ColorRes id: Int) =
    ContextCompat.getColor(requireContext(), id)


