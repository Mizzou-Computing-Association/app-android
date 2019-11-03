package tigerhacks.android.tigerhacksapp.tigerpass

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.graduationYearSpinner
import kotlinx.android.synthetic.main.activity_register.shirtSizeSpinner
import tigerhacks.android.tigerhacksapp.R

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val graduationYearList = listOf("2018", "2019", "2020", "2021", "2022", "2023", "2024")
        val shirtSizeList = listOf("XS", "S", "M", "L", "XL", "XXL")

        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, graduationYearList)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val shirtAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, shirtSizeList)
        shirtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        graduationYearSpinner.adapter = yearAdapter
        shirtSizeSpinner.adapter = shirtAdapter
    }
}