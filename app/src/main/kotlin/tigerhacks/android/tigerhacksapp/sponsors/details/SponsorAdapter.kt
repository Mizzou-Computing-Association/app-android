package tigerhacks.android.tigerhacksapp.sponsors.details

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.Mentor
import tigerhacks.android.tigerhacksapp.models.Sponsor

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class SponsorAdapter(val sponsor: Sponsor) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = emptyList<Mentor>()

    fun submitList(list: List<Mentor>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (list.isNotEmpty()) 2 + list.size else 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = when(viewType) {
            0 -> InformationView(parent.context)
            1 -> TextView(parent.context)
            else -> MentorView(parent.context)
        }
        return object : RecyclerView.ViewHolder(view) {}
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val itemView = holder.itemView) {
            is InformationView -> itemView.setSponsor(sponsor)
            is MentorView -> itemView.setup(list[position - 2])
            is TextView -> {
                val textRes = if (position == 1) R.string.mentors else R.string.none_available
                val appRes = if (position == 1) R.style.AppTheme_TitlePrimary else R.style.AppTheme_TextPrimary

                itemView.apply {
                    text = context.getString(textRes)
                    setTextAppearance(context, appRes)

                    val marginStart = context.resources.getDimension(R.dimen.margin_start_large).toInt()
                    val marginTop = context.resources.getDimension(R.dimen.margin_top_large).toInt()
                    setPadding(marginStart, marginTop, marginStart, 0)
                }
            }
        }
    }

    override fun getItemViewType(position: Int) = if (position < 2) position else {
        if(list.isNotEmpty()) 3 else 1
    }
}

