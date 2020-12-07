package com.example.yasaklar

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_anasayfa.*
import kotlinx.android.synthetic.main.toolsbar.*
import java.time.LocalDateTime


class AnasayfaFragment : Fragment() {

    private val YasakBitis = 5
    private var kalanSaat: Int? = null
    private var kalanDakika: Int? = null
    private var kalanSaniye: Int? = null

    var KalanSaniyeLong: Long? = null

    var SaatOlcer: CountDownTimer? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anasayfa, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //kalan saniye-dk-saat atamalarını yapma
        kalan_sure()

        //her saniyede yapılan işlem
        SaniyelikIslem()







    }


    override fun onPause() {
        super.onPause()

        SaatOlcer!!.cancel()
    }







    fun SaniyelikIslem() {

        EkranaYazdirSure()


        if (kalanSaniye != null && kalanSaat != null && kalanSaat != null) {


            SaatOlcer = object : CountDownTimer((KalanSaniyeLong!! * 1000) + 5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    kalanSaniye = kalanSaniye!! - 1



                    if (kalanSaniye == -1) {
                        kalanSaniye = 59
                        saniye.text = kalanSaniye.toString()
                        kalanDakika = kalanDakika!! - 1




                        if (kalanDakika == -1) {
                            kalanDakika = 59
                            kalanSaat = kalanSaat!! - 1
                            dakika.text = kalanDakika.toString()



                            if (kalanSaat == -1) {
                                Toast.makeText(
                                    context,
                                    "Süre Bitti1...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                SaatOlcer!!.cancel()
                                kalan_sure()
                                SaniyelikIslem()

                            } else {

                                saat.text = kalanSaat.toString()
                            }

                        } else {
                            dakika.text = kalanDakika.toString()

                        }

                    } else {


                        saniye.text = kalanSaniye.toString()


                        //oyuncuKarakteri.animate().rotation(dondurf).duration=600
                    }


                }

                override fun onFinish() {
                    Toast.makeText(context, "Süre Bitti2...", Toast.LENGTH_SHORT).show()
                }

            }
            (SaatOlcer as CountDownTimer).start()

        }

    }

    fun animasyon() {
        saniye.animate().translationX(50f).duration = 800
        saniye.animate().translationY(-50f).duration = 800
    }


    private fun EkranaYazdirSure() {


        saat.text = kalanSaat.toString()
        dakika.text = kalanDakika.toString()
        saniye.text = kalanSaniye.toString()

    }


    private fun kalan_sure() {


        val saat = LocalDateTime.now().hour
        val dakika = LocalDateTime.now().minute
        val saniye = LocalDateTime.now().second





        if (saat >= 0 && saat <= 4) {

            Yasak_Var_Yok.text = "Yasağın Bitmesine kalan süre"
            val YasakToplamSuresiSaniye = 5 * 60 * 60
            KalanSaniyeLong = YasakToplamSuresiSaniye.toLong()

            val YasakGecenSureSaniye = (saat * 60 * 60) + (dakika * 60) + (saniye)
            var YasakBitmesineKalanSureSaniye = YasakToplamSuresiSaniye - YasakGecenSureSaniye


            kalanSaat = YasakBitmesineKalanSureSaniye / (60 * 60)
            YasakBitmesineKalanSureSaniye = YasakBitmesineKalanSureSaniye - (kalanSaat!! * 60 * 60)


            kalanDakika = YasakBitmesineKalanSureSaniye / 60
            YasakBitmesineKalanSureSaniye = YasakBitmesineKalanSureSaniye - (kalanDakika!! * 60)
            kalanSaniye = YasakBitmesineKalanSureSaniye


        } else if (saat >= 21) {
            Yasak_Var_Yok.text = "Yasağın Bitmesine kalan süre"

            val gunToplamSaniye = 24 * 60 * 60
            val gunGecenSaniye = (saat * 60 * 60) + (dakika * 60) + (saniye)
            kalanSaniye = gunToplamSaniye - gunGecenSaniye
            KalanSaniyeLong = kalanSaniye!!.toLong() + (5L * 60 * 60)

            kalanSaat = kalanSaniye!! / (60 * 60)
            kalanSaniye = kalanSaniye!! - (kalanSaat!! * 60 * 60)


            kalanDakika = kalanSaniye!! / 60
            kalanSaniye = kalanSaniye!! - (kalanDakika!! * 60)
            kalanSaat = kalanSaat!! + YasakBitis
        } else {
            //Yasak başlangic // ekstra düzenlenecek daha sonra
            Yasak_Var_Yok.text = "Yasağın Başlamasına  kalan süre"

            val gunToplamSaniye = 21 * 60 * 60
            val gunGecenSaniye = (saat * 60 * 60) + (dakika * 60) + (saniye)
            kalanSaniye = gunToplamSaniye - gunGecenSaniye
            KalanSaniyeLong = kalanSaniye!!.toLong()

            Log.i("deneme10", kalanSaniye.toString())

            kalanSaat = kalanSaniye!! / (60 * 60)
            kalanSaniye = kalanSaniye!! - (kalanSaat!! * 60 * 60)


            kalanDakika = kalanSaniye!! / 60
            kalanSaniye = kalanSaniye!! - (kalanDakika!! * 60)


        }


    }
}