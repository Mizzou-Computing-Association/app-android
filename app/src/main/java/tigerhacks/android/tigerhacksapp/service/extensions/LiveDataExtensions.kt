package tigerhacks.android.tigerhacksapp.service.extensions

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

/*
 * @author pauldg7@gmail.com (Paul Gillis)
 */
 
fun <T> LiveData<T>.observeNotNull(lifecycleOwner: LifecycleOwner, block: (T) -> Unit): Observer<T> {
    val observer = Observer<T> { it?.let(block) }
    observe(lifecycleOwner, observer)
    return observer
}