package com.intesasanpaolo.ispmobileinspector.utils

import android.content.res.AssetManager
import android.os.Build
import android.text.Html
import com.google.gson.Gson
import com.intesasanpaolo.ispmobileinspector.ui.Card
import com.intesasanpaolo.ispmobileinspector.ui.CardRawLog
import com.intesasanpaolo.ispmobileinspector.ui.NetworkCardOption
import com.intesasanpaolo.ispmobileinspector.ui.NetworkCardRawLog
import org.json.JSONObject

object MobileInspectorUtils {

    fun getDecodedJsonObject(json: String?): JSONObject {
        val formattedJson = if (Build.VERSION.SDK_INT >= 24) {
            Html.fromHtml(json, Html.FROM_HTML_MODE_LEGACY).toString().replace("\n", "")
                .replace("\\n", "").trim()
        } else {
            Html.fromHtml(json).toString().replace("\n", "").replace("\\n", "").trim()
        }
        return JSONObject(formattedJson)
    }

    fun AssetManager.readFile(fileName: String) = open(fileName)
        .bufferedReader()
        .use { it.readText() }


    fun mapCardList(json: JSONObject): ArrayList<Card> {
        val gson = Gson()
        val cardOptions = ArrayList<Card>()

        try {
            val cardObject = json.getJSONArray("card")
            for (i in 0 until cardObject.length()) {
                val option =
                    gson.fromJson(cardObject[i].toString(), NetworkCardOption::class.java)

                cardOptions.add(
                    Card(
                        timeStamp = option.timeStamp,
                        url = option.url,
                        status = option.status
                    ))
            }
        } catch (e: Exception) {
            //Timber.e(e)
        }
        return cardOptions
    }

    fun mapCardRawLogList(json: JSONObject): ArrayList<CardRawLog> {
        val gson = Gson()
        val cardOptions = ArrayList<CardRawLog>()

        try {
            val cardObject = json.getJSONArray("cardRawLog")
            for (i in 0 until cardObject.length()) {
                val option =
                    gson.fromJson(cardObject[i].toString(), NetworkCardRawLog::class.java)

                cardOptions.add(
                    CardRawLog(
                        timeStamp = option.timeStamp,
                        message = option.message,
                        caller = option.caller,
                        level = option.level,
                        id = option.id,
                        url = option.url,
                        method = option.method,
                        requestBody = option.requestBody,
                        responseBody = option.responseBody,
                        statusCode = option.statusCode
                    ))
            }
        } catch (e: Exception) {
            //Timber.e(e)
        }
        return cardOptions
    }
}