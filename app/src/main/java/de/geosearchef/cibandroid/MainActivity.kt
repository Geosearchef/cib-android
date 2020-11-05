package de.geosearchef.cibandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.ScrollView
import android.widget.TextView
import com.google.gson.Gson
import java.net.URL
import java.net.URLConnection
import java.util.*
import kotlin.collections.ArrayList

const val API_URL = "http://geosearchef.de:31100/api"
const val CITY_LIST_ROUTE = "/cities"
const val CITY_ROUTE = "/city"

class MainActivity : AppCompatActivity() {

//    lateinit var mainLabel: TextView
    lateinit var cityList: LinearLayout

    val cityButtons = ArrayList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        mainLabel = findViewById(R.id.mainLabel)
        cityList = findViewById(R.id.cityList)

//        Thread {
//            val cities = getCityList()
//            val params = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
//
//            runOnUiThread {
////                mainLabel.setText(cities.map { it.replace("_", " ") }.joinToString("\n"))
//                cities.map { it.replace("_", " ") }.forEach { city ->
//                    val button = Button(this)
//                    val id = button.id
//                    cityButtons.add(button)
//                    cityList.addView(Button(this), params)
//
//                    findViewById<Button>(id).text = "test"
//                }
//            }
//        }.start()

        Thread {
            println(getCityInfo("München_Stadt"))
        }.start()
    }



    companion object {
        val gson = Gson()

        fun getCityList(): List<String> {
            val response = URL(API_URL + CITY_LIST_ROUTE).readText()
            return gson.fromJson(response, Array<String>::class.java).toList()
        }

        fun getCityInfo(city: String): Info {
            val response = URL(API_URL + CITY_ROUTE + "?c=$city").readText()
            return gson.fromJson(response, Info::class.java)
        }

        data class Info(val name: String,
                        val caseNumber: Int,
                        val caseChange: String,
                        val casesPer100k: Double,
                        val casesPast7Days: Int,
                        val sevenDayIncidencePer100k: Double,
                        val deathCount: Int,
                        val deathsChange: String,
                        val dayString: String) {

            fun toInfoString() : String {
                return String.format("$dayString: %.1f", sevenDayIncidencePer100k)
            }

        }
    }
}
