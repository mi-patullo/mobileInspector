package com.intesasanpaolo.ispmobileinspector.ui

import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.annotation.JsonProperty
import com.intesasanpaolo.ispmobileinspector.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.card_view.*

class CardAdapter(
    private val mCards: ArrayList<Card>, private val mContext: Context,
) :
    RecyclerView.Adapter<CardAdapter.MovementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val movementView = inflater.inflate(R.layout.card_view, parent, false)
        return MovementViewHolder(movementView)
    }

    override fun onBindViewHolder(holder: MovementViewHolder, position: Int) {
        Log.d("tagg", "mess: ")
        val movement: Card = mCards[position]
        holder.bind(movement)
    }

    override fun getItemCount(): Int {
        return mCards.size
    }

    fun refreshData() {
        notifyDataSetChanged()
    }

    inner class MovementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bind(card: Card) {
            Log.d("tagg", "mess")
            id_timestamp_response.text = card.timeStamp
            id_url_response.text = card.url
            id_status_response.text = card.status

            if (card.status == "200" || card.status == "0") {
                id_color.setBackgroundColor(getColor(mContext, R.color.green)

                )
            } else {
                id_color.setBackgroundColor(getColor(mContext, R.color.red))

            }
        }
    }
}

@Parcelize
data class BackendLogModel(
    val card: ArrayList<Card>?
) : Parcelable

@Parcelize
data class Card(
    val timeStamp: String?,
    val url: String?,
    val status: String?,
) : Parcelable


data class NetworkCardOption(
    @JsonProperty("timeStamp") val timeStamp: String? = null,
    @JsonProperty("url") val url: String? = null,
    @JsonProperty("status") val status: String? = null
)
