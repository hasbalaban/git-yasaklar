 package com.example.yasaklar


import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolsbar.*


class MainActivity : AppCompatActivity() {
    var ActionBarDrawerToggle: ActionBarDrawerToggle? = null


    var toogle: ActionBarDrawerToggle? = null
    var toolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager =
            supportFragmentManager.beginTransaction().replace(R.id.fragment, DrawerLayout())
                .commit()
        supportFragmentManager.beginTransaction().replace(R.id.hostFragment, AnasayfaFragment())
            .commit()


        ActionBarSettings()


        BottomClick()


    }




    fun ActionBarSettings() {


        toolbar = toolbar
        toogle = ActionBarDrawerToggle(this, DrawerLayout, toolsBar, R.string.open, R.string.close)
        toogle!!.syncState()

    }

    fun BottomClick () {

        bottom.setOnNavigationItemSelectedListener {

            when (it.itemId){
                R.id.first ->
                {
                    supportFragmentManager.beginTransaction().replace(R.id.hostFragment, AnasayfaFragment())
                        .commit()
                }

                R.id.second ->
                {
                    supportFragmentManager.beginTransaction().replace(R.id.hostFragment, ikinciFragment())
                        .commit()
                }
                R.id.third->
                {
                    supportFragmentManager.beginTransaction().replace(R.id.hostFragment,  DrawerLayout())
                        .commit()
                }

            }

            return@setOnNavigationItemSelectedListener true
        }
    }


    override fun onPause() {
        super.onPause()

        Log.i("deneme10", "klannanana")

        supportFragmentManager.beginTransaction().replace(R.id.hostFragment, DrawerLayout())
            .commit()
    }



}