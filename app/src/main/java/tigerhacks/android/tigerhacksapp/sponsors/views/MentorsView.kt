package tigerhacks.android.tigerhacksapp.sponsors.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.view_mentor.view.contactTextView
import kotlinx.android.synthetic.main.view_mentor.view.nameTextView
import kotlinx.android.synthetic.main.view_mentor.view.skillsTextView
import kotlinx.android.synthetic.main.view_mentors.view.mentorsEmptyTextView
import kotlinx.android.synthetic.main.view_mentors.view.mentorsLayout
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.sponsors.Mentor

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class MentorsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_mentors, this, true)
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
        LayoutInflater.from(context).inflate(R.layout.view_mentor, this, true)
    }

    fun setup(mentor: Mentor): MentorView {
        nameTextView.text = mentor.name

        mentor.contact?.let { mentorContact ->
            if (mentorContact.isNotEmpty()) {
                contactTextView.text = mentorContact
                contactTextView.setOnClickListener {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("something", mentorContact)
                    clipboard.primaryClip = clip
                    Snackbar.make(
                        rootView,
                        "Contact copied to clipboard",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        if (mentor.skills != "") skillsTextView.text = mentor.skills
        return this
    }
}