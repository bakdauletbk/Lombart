package kz.pillikan.lombart.content.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_sliders.view.*
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.response.home.SlidersList

class PagerAdapter(val context: Context?) : PagerAdapter() {

    private var inflater: LayoutInflater? = null

    private var sliderList: ArrayList<SlidersList> = ArrayList()

    fun addImageSlider(sliderList: ArrayList<SlidersList>) {
        this.sliderList.addAll(sliderList)
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return sliderList.size
    }

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater!!.inflate(R.layout.item_sliders, null)

        Glide
            .with(this.context)
            .load(Constants.IMG_BASE_URL + Constants.IMG_SLIDER_URL + sliderList[position].img)
            .centerCrop()
            .into(view.iv_banner)

        val pager = container as ViewPager

        pager.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val pager = container as ViewPager
        val view = `object` as View
        pager.removeView(view)
    }
}