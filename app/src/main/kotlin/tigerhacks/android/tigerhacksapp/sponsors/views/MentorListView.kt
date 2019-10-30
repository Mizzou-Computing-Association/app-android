package tigerhacks.android.tigerhacksapp.sponsors.views

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_tiger_login.loginContainer
import kotlinx.android.synthetic.main.view_mentor.view.contactTextView
import kotlinx.android.synthetic.main.view_mentor.view.nameTextView
import kotlinx.android.synthetic.main.view_mentor.view.skillsTextView
import kotlinx.android.synthetic.main.view_mentor_list.view.mentorsEmptyTextView
import kotlinx.android.synthetic.main.view_mentor_list.view.mentorsLayout
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.sponsors.models.Mentor

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class MentorListView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_mentor_list, this, true)
    }

    var mentors: List<Mentor>? = null
        set(value) {
            if (field == value) return
            field = value
            redraw()
        }

    private fun redraw() {
        mentorsLayout.removeAllViews()
        mentors?.forEach { mentor ->
            val mentorView = MentorView(context).setup(mentor)
            mentorsLayout.addView(mentorView)
        }

        mentorsEmptyTextView.visibility = if (mentors == null || mentors?.size == 0) View.VISIBLE else View.GONE
    }
}

class MentorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        val typedValue = TypedValue()
        getContext().theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
        LayoutInflater.from(context).inflate(R.layout.view_mentor, this, true)
        contactTextView.setBackgroundResource(typedValue.resourceId)
    }

    fun setup(mentor: Mentor): MentorView {
        nameTextView.text = mentor.name

        if (mentor.contact.isNotEmpty()) {
            contactTextView.setOnClickListener {
                val uriString = "slack://user?team=TN5APUBT9&id=${mentor.contact}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
                intent.flags = FLAG_ACTIVITY_NEW_TASK
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) { //Couldn't Find slack try and open play store to download Slack
                    val playUriString = "https://play.google.com/store/apps/details?id=com.Slack"
                    val playIntent = Intent(Intent.ACTION_VIEW, Uri.parse(playUriString))
                    try { //Couldn't Find Slack or Google Play
                        context.startActivity(playIntent)
                    } catch (e: ActivityNotFoundException) {
                        Snackbar.make(this, "Couldn't find slack app or google play store!", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (mentor.skills != "") skillsTextView.text = mentor.skills
        return this
    }
}