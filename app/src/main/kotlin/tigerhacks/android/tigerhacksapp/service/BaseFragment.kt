package tigerhacks.android.tigerhacksapp.service

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    abstract val titleResId: Int
    abstract val navId: Int
}