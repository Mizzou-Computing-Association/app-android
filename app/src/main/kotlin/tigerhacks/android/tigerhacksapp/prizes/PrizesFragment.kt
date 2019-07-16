package tigerhacks.android.tigerhacksapp.prizes

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import tigerhacks.android.tigerhacksapp.R

class PrizesFragment : Fragment() {
    companion object {
        fun newInstance() = PrizesFragment()
    }

    private var tabLayout: TabLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.fragment_prizes, container, false)
        val prizeViewPager = layoutView.findViewById<ViewPager>(R.id.prizeViewPager)

        val developerFragment = PrizeCategoryFragment.newInstance(0)
        val startupFragment = PrizeCategoryFragment.newInstance(1)
        val beginnerFragment = PrizeCategoryFragment.newInstance(2)

        prizeViewPager?.apply {
            adapter = object : FragmentPagerAdapter(childFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    return when (position) {
                        0 -> developerFragment
                        1 -> startupFragment
                        else -> beginnerFragment
                    }
                }

                override fun getCount() = 3

                override fun getPageTitle(position: Int): CharSequence? {
                    return when (position) {
                        0 -> "Developer"
                        1 -> "Startup"
                        else -> "Beginner"
                    }
                }
            }
            offscreenPageLimit = 2
        }

        tabLayout = layoutView.findViewById(R.id.typeTabLayout)
        if (prizeViewPager != null) {
            tabLayout?.setupWithViewPager(prizeViewPager)
        }

        return layoutView
    }
}
