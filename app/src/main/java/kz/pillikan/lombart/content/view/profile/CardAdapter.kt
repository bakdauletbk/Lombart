package kz.pillikan.lombart.content.view.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.pillikan.lombart.R
import kz.pillikan.lombart.content.model.response.home.CardList
import org.jetbrains.anko.sdk27.coroutines.onClick

class CardAdapter : RecyclerView.Adapter<CardAdapter.CardAdapterViewHolder> {

    private val cardList: ArrayList<CardList> = ArrayList()

    private var callback: ProfileFragment

    constructor(callback: ProfileFragment) : super() {
        this.callback = callback
    }

    fun addCard(cardList: List<CardList>) {
        this.cardList.clear()
        this.cardList.addAll(cardList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardAdapterViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cards, parent, false)
        return CardAdapterViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: CardAdapterViewHolder,
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
        fun bind(cardList: CardList, callback: ProfileFragment) {
            tvCardName.text = cardList.name
            tvCardNumber.text = cardList.id

            ivMenu.onClick {
                val popup = PopupMenu(callback.context, ivMenu)
                popup.inflate(R.menu.custom_menu)

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_delete -> {
                        }
                    }
                    false
                }
                popup.show()
            }

        }
    }
}