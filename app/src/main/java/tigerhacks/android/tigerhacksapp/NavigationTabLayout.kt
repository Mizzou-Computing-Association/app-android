package tigerhacks.android.tigerhacksapp

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx
import tigerhacks.android.tigerhacksapp.service.extensions.getColorRes

/*
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class NavigationTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TabLayout(context, attrs, defStyleAttr) {

    init {
        setBackgroundResource(R.color.colorPrimary)
        setSelectedTabIndicatorHeight(context.dpToPx(4))
        setSelectedTabIndicatorColor(context.getColorRes(R.color.black))
    }

    fun setup(viewPager: ViewPager) {
        setupWithViewPager(viewPager)
        val icons = arrayOf(R.drawable.ic_map_24dp, R.drawable.ic_prizes_24dp, R.drawable.ic_schedule_24dp, R.drawable.ic_sponsors_24dp, R.drawable.ic_tigertalks_24dp)
        postDelayed({
            for(i in 0..tabCount) {
                getTabAt(i)?.setIcon(icons[i])
            }
        }, 1)
    }
}