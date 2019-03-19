package tigerhacks.android.tigerhacksapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/*
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, HomeScreenActivity::class.java))
        finish()
    }
}