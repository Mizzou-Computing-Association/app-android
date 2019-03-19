package tigerhacks.android.tigerhacksapp.sponsors.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.getColorRes
import tigerhacks.android.tigerhacksapp.sponsors.Mentor

/*
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class MentorsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = VERTICAL
    }

    var mentors: List<Mentor>? = null
        set(value) {
            if (field == value) return
            field = value
            redraw()
        }

    private fun redraw() {
        removeAllViews()
        if (mentors?.size == 0) return
        mentors?.let { mentors ->
            val mentorsTitle = TextView(context).apply {
                setTextColor(context.getColorRes(R.color.black))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
                gravity = Gravity.CENTER_HORIZONTAL
                setTypeface(null, Typeface.BOLD)
                text = context.getString(R.string.mentors)
            }
            addView(mentorsTitle)

            mentors.forEach { mentor ->
                val mentorView = MentorView(context).apply {
                    setup(mentor)
                }
                addView(mentorView)
            }
        }
    }
}

class MentorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = VERTICAL
    }

    fun setup(mentor: Mentor) {
        val name = TextView(context)
        name.setTypeface(null, Typeface.BOLD)
        name.text = mentor.name
        addView(name)

        mentor.contact?.let { mentorContact ->
            if (mentorContact.isNotEmpty()) {
                val contact = TextView(context)
                contact.setTextColor(context.getColorRes(R.color.linkColor))
                contact.text = mentorContact
                contact.setOnClickListener {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("something", contact.text)
                    clipboard.primaryClip = clip
                    Snackbar.make(
                        contact.rootView,
                        "Contact copied to clipboard",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                addView(contact)
            }
        }

        val skills = TextView(context)
        skills.text = mentor.skills
        addView(skills)
    }
}