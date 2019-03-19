package tigerhacks.android.tigerhacksapp.sponsors.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx
import tigerhacks.android.tigerhacksapp.service.extensions.getColorRes
import tigerhacks.android.tigerhacksapp.sponsors.Mentor

/*
 * @author pauldg7@gmail.com (Paul Gillis)
 */

private const val infoTextSize = 16f
private const val infoStartMargin = 16
private const val topMargin = 8

class MentorsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = VERTICAL
    }

    private val infoStartMarginDp = context.dpToPx(infoStartMargin)
    private val topMarginDp = context.dpToPx(topMargin)

    var mentors: List<Mentor>? = null
        set(value) {
            if (field == value) return
            field = value
            redraw()
        }

    private fun redraw() {
        removeAllViews()
        val mentorsTitle = TextView(context).apply {
            layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                setMargins(infoStartMarginDp, topMarginDp, infoStartMarginDp, topMarginDp)
            }
            setTextColor(context.getColorRes(R.color.black))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            setTypeface(null, Typeface.BOLD)
            text = context.getString(R.string.mentors)
        }
        addView(mentorsTitle)

        mentors?.forEach { mentor ->
            val mentorView = MentorView(context).apply {
                setup(mentor)
            }
            addView(mentorView)
        }

        if (mentors == null || mentors?.size == 0) {
            val naTextView = TextView(context).apply {
                layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    setMargins(infoStartMarginDp, 0, infoStartMarginDp, 0)
                }
                setTextSize(TypedValue.COMPLEX_UNIT_SP, infoTextSize)
                text = context.getString(R.string.none_available)
            }
            addView(naTextView)
        }
    }
}

class MentorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = VERTICAL
    }

    private val infoStartMarginDp = context.dpToPx(infoStartMargin)
    private val topMarginDp = context.dpToPx(topMargin)
    private val infoLayoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
        setMargins(infoStartMarginDp, 0, infoStartMarginDp, 0)
    }

    fun setup(mentor: Mentor) {
        val name = TextView(context).apply {
            layoutParams = infoLayoutParams
            setTypeface(null, Typeface.BOLD)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, infoTextSize)
            text = mentor.name
        }
        name.setTypeface(null, Typeface.BOLD)
        name.text = mentor.name
        addView(name)

        mentor.contact?.let { mentorContact ->
            if (mentorContact.isNotEmpty()) {
                val contact = TextView(context).apply {
                    setTextColor(context.getColorRes(R.color.linkColor))
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, infoTextSize)
                    layoutParams = infoLayoutParams
                    text = mentorContact
                    setOnClickListener {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("something", text)
                        clipboard.primaryClip = clip
                        Snackbar.make(
                            rootView,
                            "Contact copied to clipboard",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                addView(contact)
            }
        }

        val skills = TextView(context).apply {
            layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                setMargins(infoStartMarginDp, 0, 0, 2*topMarginDp)
            }
            setTextSize(TypedValue.COMPLEX_UNIT_SP, infoTextSize)
            text = if (mentor.skills != "") mentor.skills else context.getString(R.string.na)
        }
        addView(skills)
    }
}