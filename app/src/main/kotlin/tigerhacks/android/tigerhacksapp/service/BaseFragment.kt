package tigerhacks.android.tigerhacksapp.service

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract val titleResId: Int
    abstract val navId: Int
}