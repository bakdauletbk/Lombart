package kz.pillikan.lombart.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import com.yandex.mapkit.mapview.MapView

class MapViewCustom(context: Context?, attrs: AttributeSet?) :
    MapView(context, attrs) {
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP -> {
                this.parent.requestDisallowInterceptTouchEvent(false)
            }
            MotionEvent.ACTION_DOWN -> {
                this.parent.requestDisallowInterceptTouchEvent(true)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}