package com.intesasanpaolo.ispmobileinspector.ui

import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.annotation.JsonProperty
import com.intesasanpaolo.ispmobileinspector.Listener
import com.intesasanpaolo.ispmobileinspector.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.card_view.*

class CardAdapterRawLog(
    private val mCards: ArrayList<CardRawLog>,
    private val mContext: Context,
    private val mInterface: Listener,
) :
    RecyclerView.Adapter<CardAdapterRawLog.MovementViewHolderRawLog>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolderRawLog {

        val inflater = LayoutInflater.from(parent.context)
        val movementView = inflater.inflate(R.layout.card_view_raw_log, parent, false)
        return MovementViewHolderRawLog(movementView)
    }

    override fun onBindViewHolder(holder: MovementViewHolderRawLog, position: Int) {
        Log.d("tagg", "mess: ")
        val movement: CardRawLog = mCards[position]
        holder.bind(movement)
    }

    override fun getItemCount(): Int {
        return mCards.size
    }

    fun refreshData() {
        notifyDataSetChanged()
    }

    inner class MovementViewHolderRawLog(itemView: View) : RecyclerView.ViewHolder(itemView),
        LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bind(card: CardRawLog) {
            Log.d("tagg", "mess")
            id_timestamp_response.text = card.timeStamp
            id_url_response.text = card.message
            id_status_response.text = card.caller


            itemView.setOnClickListener {
                val mCard = CardRawLog(
                    timeStamp = card.timeStamp,
                    message = card.message,
                    caller = card.caller,
                    level = card.level,
                    id = card.id,
                    url = card.url,
                    method = card.method,
                    requestBody = card.requestBody,
                    responseBody = card.responseBody,
                    statusCode = card.statusCode
                )

                mInterface.goToDettaglio(mCard)
            }

            when (card.level) {
                "verbose" -> id_color.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.green
                    )
                )
                "debug" -> id_color.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.red
                    )
                )
                "info" -> id_color.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.pink
                    )
                )
                "warn" -> id_color.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.purple_500
                    )
                )
                "error" -> id_color.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.blu
                    )
                )
                "assert" -> id_color.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.orange
                    )
                )
            }
        }
    }
}

@Parcelize
data class RawLogModel(
    val cardRawLog: ArrayList<CardRawLog>?
) : Parcelable

@Parcelize
data class CardRawLog(
    val timeStamp: String?,
    val message: String?,
    val caller: String?,
    val level: String?,
    val id: Int?,
    val url: String?,
    val method: String?,
    val requestBody: String?,
    val responseBody: String?,
    val statusCode: String?,
) : Parcelable

data class NetworkCardRawLog(
    @JsonProperty("timeStamp") val timeStamp: String? = null,
    @JsonProperty("message") val message: String? = null,
    @JsonProperty("caller") val caller: String? = null,
    @JsonProperty("level") val level: String? = null,
    @JsonProperty("id") val id: Int? = null,
    @JsonProperty("url") val url: String? = null,
    @JsonProperty("method") val method: String? = null,
    @JsonProperty("requestBody") val requestBody: String? = null,
    @JsonProperty("responseBody") val responseBody: String? = null,
    @JsonProperty("statusCode") val statusCode: String? = null,
)

