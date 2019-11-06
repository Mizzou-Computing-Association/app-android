package tigerhacks.android.tigerhacksapp.service.extensions

import android.view.View

fun View.setOnClickListener(block: () -> Unit) {
    val clickListener = View.OnClickListener { block.invoke() }
    setOnClickListener(clickListener)
}