package tigerhacks.android.tigerhacksapp.service

import androidx.fragment.app.Fragment

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
abstract class BaseFragment : Fragment() {
    abstract val titleResId: Int
    abstract val navId: Int
}