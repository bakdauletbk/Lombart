package kz.pillikan.lombart.content.view.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.pillikan.lombart.R
import kz.pillikan.lombart.content.model.response.profile.CardModel

class CardAdapter : RecyclerView.Adapter<CardAdapter.CardAdapterViewHolder> {

    private val cardList: ArrayList<CardModel> = ArrayList()

    private var callback: ProfileFragment

    constructor(callback: ProfileFragment) : super() {
        this.callback = callback
    }

    fun addCard(cardList: List<CardModel>) {
        this.cardList.addAll(cardList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardAdapter.CardAdapterViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cards, parent, false)
        return CardAdapter.CardAdapterViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: CardAdapter.CardAdapterViewHolder,
        position: Int
    ) {
        holder.bind(cardList[position], callback)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    class CardAdapterViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        private val tvCardName = root.findViewById(R.id.tv_card_name) as TextView
        private val tvCardNumber = root.findViewById(R.id.tv_card_number) as TextView
        private val ivMenu = root.findViewById(R.id.iv_menu) as ImageView
        fun bind(cardList: CardModel, callback: ProfileFragment) {
            tvCardName.text = cardList.cardName
            tvCardNumber.text = cardList.cardNumber.toString()
        }
    }
}