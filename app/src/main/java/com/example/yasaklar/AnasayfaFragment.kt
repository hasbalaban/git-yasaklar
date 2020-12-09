package com.example.yasaklar

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.fragment_anasayfa.*
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime


class AnasayfaFragment : Fragment() {
    private val YasakBitis = 5
    private var kalanSaat: Int? = null
    private var kalanDakika: Int? = null
    private var kalanSaniye: Int? = null
    private var bugun: String? = null

    /// saat atama
    private var gunSaat: Int? = null
    private var gunDakika: Int? = null
    private var gunSaniye: Int? = null

    private val yazi1 = "Yasağın başlamasına kalan süre"
    private val yazi2 = "Yasağın Bitmesine kalan süre"

    private val gunler =
        arrayListOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")

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

    }

    fun AcilisIslemleri() {
        runBlocking {

            DayOfWeek()
        }
        SaniyelikIslem()
    }

    //uygulama duraktıldğında sayım durur
    override fun onPause() {
        super.onPause()
        SaatOlcer!!.cancel()
    }

    //uygulama açıldığında sayım baştan başlar
    override fun onResume() {
        super.onResume()
        AcilisIslemleri()
    }

    //saniye bazlı işlemler - Her saniye sayacaın değişme işlemi ve Süre  Bittiğinde Baştan yeni sayacı başlatması..
    fun SaniyelikIslem() {

        EkranaYazdirSure()

        if (kalanSaniye != null && kalanSaat != null && kalanSaat != null) {
            SaatOlcer = object : CountDownTimer((KalanSaniyeLong!! * 1000) + 5000, 500) {
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

                                DayOfWeek()
                                SaniyelikIslem()

                            }
                            else {
                                saat.text = kalanSaat.toString()
                            }
                        }
                        else {
                            dakika.text = kalanDakika.toString()
                        }

                    }
                    else {
                        saniye.text = kalanSaniye.toString()
                    }
                }

                override fun onFinish() {
                    Toast.makeText(context, "Süre Bitti2...", Toast.LENGTH_SHORT).show()
                }

            }
            (SaatOlcer as CountDownTimer).start()
        }
    }

    //Haftanın gününü belirleyip ona göre süre sayma işlemleri
    fun DayOfWeek() {
        guncelSaat()
        bugun = LocalDateTime.now().dayOfWeek.toString() // günü belirleme

        when (bugun) {
            gunler[0], gunler[1], gunler[2], gunler[3] -> {

                runBlocking {
                    kalan_sure()
                }
            }
            gunler[4] -> {
                if (gunSaat!! < 21) {
                    kalan_sure()
                } else {
                    val ekGunSaniye = 24 * 60 * 60

                    runBlocking {
                        kalan_sure((ekGunSaniye * 2))
                    }
                    Yasak_Var_Yok.text = "Yasağın Bitmesine kalan süre"
                }
            }

            gunler[5] -> {

                val ekGunSaniye = 24 * 60 * 60 // günün toplam saniyesi
                val gunkalanSaniye =
                    ekGunSaniye - ((gunSaat!! * 60 * 60) + (gunDakika!! * 60) + (gunSaniye!!))

                runBlocking {

                    kalan_sure_HaftaSonu(ekGunSaniye + gunkalanSaniye)

                }

                Yasak_Var_Yok.text = "Yasağın Bitmesine kalan süre"
            }

            gunler[6] -> {

                val ekGunSaniye = 24 * 60 * 60 // günün toplam saniyesi
                val gunkalanSaniye =
                    ekGunSaniye - ((gunSaat!! * 60 * 60) + (gunDakika!! * 60) + (gunSaniye!!))


                runBlocking {
                    kalan_sure_HaftaSonu(gunkalanSaniye)
                }

                Yasak_Var_Yok.text = "Yasağın Bitmesine kalan süre"
            }
        }

    }

    //ekran widgetlerine anlık kalan süreyi yazdırma.
    private fun EkranaYazdirSure() {
        saat.text = kalanSaat.toString()
        dakika.text = kalanDakika.toString()
        saniye.text = kalanSaniye.toString()

    }

    // güncel saati almak
    fun guncelSaat() {
        gunSaat = LocalDateTime.now().hour
        gunDakika = LocalDateTime.now().minute
        gunSaniye = LocalDateTime.now().second

    }

    // Hafta sonu günlerinde kalan süreyi hesaplama
    private fun kalan_sure_HaftaSonu(ekSaniye: Int = 0) {

        val YasakToplamSuresiSaniye = (5 * 60 * 60) + ekSaniye
        Log.i("gungun1", YasakToplamSuresiSaniye.toString())
        KalanSaniyeLong = YasakToplamSuresiSaniye.toLong()

        var YasakBitmesineKalanSureSaniye = YasakToplamSuresiSaniye


        kalanSaat = YasakBitmesineKalanSureSaniye / (60 * 60)
        YasakBitmesineKalanSureSaniye = YasakBitmesineKalanSureSaniye - (kalanSaat!! * 60 * 60)


        kalanDakika = YasakBitmesineKalanSureSaniye / 60
        YasakBitmesineKalanSureSaniye = YasakBitmesineKalanSureSaniye - (kalanDakika!! * 60)
        kalanSaniye = YasakBitmesineKalanSureSaniye
    }

    // Hafta içi günlerinde kalan süreyi hesaplama
    private fun kalan_sure(ekSaniye: Int = 0) {

        if (gunSaat!! >= 0 && gunSaat!! <= 4) {

            Yasak_Var_Yok.text = "Yasağın Bitmesine kalan süre"
            val YasakToplamSuresiSaniye = (5 * 60 * 60) + ekSaniye
            Log.i("gungun1", YasakToplamSuresiSaniye.toString())
            KalanSaniyeLong = YasakToplamSuresiSaniye.toLong()

            val YasakGecenSureSaniye = (gunSaat!! * 60 * 60) + (gunDakika!! * 60) + (gunSaniye!!)
            var YasakBitmesineKalanSureSaniye = YasakToplamSuresiSaniye - YasakGecenSureSaniye


            kalanSaat = YasakBitmesineKalanSureSaniye / (60 * 60)
            YasakBitmesineKalanSureSaniye = YasakBitmesineKalanSureSaniye - (kalanSaat!! * 60 * 60)


            kalanDakika = YasakBitmesineKalanSureSaniye / 60
            YasakBitmesineKalanSureSaniye = YasakBitmesineKalanSureSaniye - (kalanDakika!! * 60)
            kalanSaniye = YasakBitmesineKalanSureSaniye


        } else if (gunSaat!! >= 21) {
            Yasak_Var_Yok.text = "Yasağın Bitmesine kalan süre"

            val gunToplamSaniye = 24 * 60 * 60 + (ekSaniye)
            Log.i("gungun2", gunToplamSaniye.toString())
            val gunGecenSaniye = (gunSaat!! * 60 * 60) + (gunDakika!! * 60) + (gunSaniye!!)
            kalanSaniye = gunToplamSaniye - gunGecenSaniye
            kalanSaniye = kalanSaniye!! + 5 * 60 * 60 // diğer günün 5 saati eklendi
            KalanSaniyeLong = kalanSaniye!!.toLong()


            kalanSaat = kalanSaniye!! / (60 * 60)
            kalanSaniye = kalanSaniye!! - (kalanSaat!! * 60 * 60)


            kalanDakika = kalanSaniye!! / 60
            kalanSaniye = kalanSaniye!! - (kalanDakika!! * 60)
        } else {
            //Yasak başlangic // ekstra düzenlenecek daha sonra
            Yasak_Var_Yok.text = "Yasağın Başlamasına  kalan süre"

            val gunToplamSaniye = 21 * 60 * 60 + (ekSaniye)
            Log.i("gungun3", gunToplamSaniye.toString())
            val gunGecenSaniye = (gunSaat!! * 60 * 60) + (gunDakika!! * 60) + (gunSaniye!!)
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