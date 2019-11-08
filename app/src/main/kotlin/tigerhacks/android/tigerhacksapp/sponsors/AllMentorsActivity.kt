package tigerhacks.android.tigerhacksapp.sponsors

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_all_mentors.recyclerView
import kotlinx.android.synthetic.main.activity_all_mentors.toolbar
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.Mentor
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import tigerhacks.android.tigerhacksapp.service.extensions.getColorRes
import tigerhacks.android.tigerhacksapp.sponsors.details.MentorView

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class AllMentorsActivity : AppCompatActivity() {
    companion object {
        fun newInstance(context: Context): Intent = Intent(context, AllMentorsActivity::class.java)
    }

    private var db: TigerHacksDatabase? = null
    private var observer: Observer<List<Mentor>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_mentors)

        //Toolbar Setup
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.all_mentors)
        toolbar.setBackgroundResource(R.color.darkBlue)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = getColorRes(R.color.darkBlue)

        val adapter = object : ListAdapter<Mentor, RecyclerView.ViewHolder>(Mentor.diff) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(MentorView(parent.context)) {}

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder.itemView as? MentorView)?.setup(getItem(position))
            }

        }

        recyclerView.adapter = adapter

        db = TigerHacksDatabase.getDatabase(applicationContext)
        observer = Observer { adapter.submitList(it) }
//        observer = db?.sponsorsDAO()?.getAllMentors()?.observe(this, observer)
    }

    override fun onResume() {
        super.onResume()
        observer?.let { db?.sponsorsDAO()?.getAllMentors()?.observe(this, it) }
    }

    override fun onPause() {
        super.onPause()
        observer?.let { db?.sponsorsDAO()?.getAllMentors()?.removeObserver(it) }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}