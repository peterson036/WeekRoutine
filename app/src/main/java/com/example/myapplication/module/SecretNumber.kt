package com.example.myapplication.module

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.myapplication.R
import com.example.myapplication.module.SecretNumber.sp
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64

object SecretNumber {

    private lateinit var sp: SharedPreferences

    fun init(context: Context) {
        sp = context.applicationContext.getSharedPreferences(
            "preference_key_secret_numbers",
            Context.MODE_PRIVATE
        )
        setDefaultContentIfNot()
    }

    var DABEC:String = ""
    var BECAD:String = ""
    var ACEBD:String = ""
    var EDBAC:String = ""
    var CABDE:String = ""

    var BDCEA:String = ""
    var ADBCE:String = ""
    var CEADB:String = ""
    var EACDB:String = ""
    var BCDAE:String = ""

    var DEACB:String = ""
    var AECBD:String = ""
    var CBAED:String = ""
    var DBECA:String = ""
    var ECABD:String = ""

    var BACDE:String = ""
    var CDEBA:String = ""
    var ABEDC:String = ""
    var DCABE:String = ""
    var EBADC:String = ""

    val primeNumbers:IntArray = intArrayOf(2,3,7,11)
    val pn:IntArray = primeNumbers

    fun oneTurn(order:Int):String{

        val no = (order * pn[0] + 1) % 5
        return when(order){
            1 -> DABEC
            2 -> BECAD
            3 -> CEADB
            4 -> EDBAC
            0 -> CABDE
            else -> CABDE
        }
    }
    fun twoTurn(order:Int):String{
        val no = (order * pn[1] + 2) % 5
        return when(order){
            1 -> BDCEA
            2 -> ADBCE
            3 -> ACEBD
            4 -> EACDB
            0 -> ECABD
            else -> ECABD
        }
    }
    fun threeTurn(order:Int):String{
        val no = (order * pn[2]  + 3) % 5
        return when(order){
            1 -> DEACB
            2 -> AECBD
            3 -> CBAED
            4 -> DBECA
            0 -> BCDAE
            else -> BCDAE
        }
    }
    fun fourTurn(order:Int):String{
        val no = (order * pn[3]  + 4) % 5
        return when(order){
            1 -> BACDE
            2 -> CDEBA
            3 -> ABEDC
            4 -> DCABE
            0 -> EBADC
            else -> EBADC
        }
    }

    private fun setDefaultContentIfNot(){
        val sharedPref = sp
        val SHORT_PASSWORD_LENGTH:Int = 3
        val LONG_PASSWORD_LENGTH:Int = 10
        val passwordLength = SHORT_PASSWORD_LENGTH
        val pl = passwordLength

        val hadSet = sharedPref.getBoolean("had_set_default_value",false)

        if(!hadSet){

            val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

            var randomString = "000"

            // oneTurn
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "DABEC",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "BECAD",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "CEADB",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "EDBAC",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "CABDE",
                    randomString
                )
            }

            // twoTurn
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "BDCEA",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "ADBCE",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "ACEBD",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "EACDB",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "ECABD",
                    randomString
                )
            }

            // threeTurn
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "DEACB",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "AECBD",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "CBAED",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "DBECA",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "BCDAE",
                    randomString
                )
            }

            // fourTurn
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "BACDE",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "CDEBA",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "ABEDC",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "DCABE",
                    randomString
                )
            }
            randomString = (1..pl)
                .map { chars.random() }
                .joinToString("")
            sharedPref.edit {
                putString(
                    "EBADC",
                    randomString
                )
            }

            sharedPref.edit {
                putBoolean(
                    "had_set_default_value",
                    true
                )
            }
        }
    }

    private fun getOneTurn(s: SharedPreferences):String {
        return getSP(s,"DABEC") + getSP(s,"BECAD") + getSP(s,"CEADB") + getSP(s,"DCABE") + getSP(s,"CABDE")

    }
    private fun getTwoTurn(s: SharedPreferences):String {
        return getSP(s,"BDCEA") + getSP(s,"ADBCE") + getSP(s,"ACEBD") + getSP(s,"EACDB") + getSP(s,"ECABD")

    }
    private fun getThreeTurn(s: SharedPreferences):String {
        return getSP(s,"DEACB") + getSP(s,"AECBD") + getSP(s,"CBAED") + getSP(s,"DBECA") + getSP(s,"BCDAE")

    }
    private fun getFourTurn(s: SharedPreferences):String {
        return getSP(s,"BACDE") + getSP(s,"CDEBA") + getSP(s,"ABEDC") + getSP(s,"DCABE") + getSP(s,"EBADC")

    }

    private fun getKeyWord():String {
        // nobody would cry, version 1.0
        return getOneTurn(sp) + getTwoTurn(sp) + getThreeTurn(sp) + getFourTurn(sp)
    }

    private fun getSP(
        s: SharedPreferences,
        key: String
    ): String {
        return s.getString(key, "000") ?: "000"
    }

    fun encodeString(original:String):String {
        var encodedText:String = ""
        encodedText = encrypt(original, createKey(getKeyWord()))
        return encodedText
    }

    fun decodeString(original:String):String {
        var decodedText:String = ""
        decodedText = decrypt(original, createKey(getKeyWord()))
        return decodedText
    }

    private fun encrypt(text: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv
        val encrypted = cipher.doFinal(text.toByteArray(Charsets.UTF_8))

        val result = iv + encrypted

        return Base64.encode(result)
    }

    private fun decrypt(text: String, secretKey: SecretKey): String {
        val data = Base64.decode(text)

        val iv = data.copyOfRange(0, 12)
        val encrypted = data.copyOfRange(12, data.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKey,
            GCMParameterSpec(128, iv)
        )

        return String(
            cipher.doFinal(encrypted),
            Charsets.UTF_8
        )
    }
    private fun createKey(password: String): SecretKey {
        val digest = MessageDigest.getInstance("SHA-256")
        val keyBytes = digest.digest(password.toByteArray(Charsets.UTF_8))

        return SecretKeySpec(keyBytes, "AES")
    }

}