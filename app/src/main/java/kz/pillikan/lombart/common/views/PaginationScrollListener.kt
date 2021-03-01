package kz.pillikan.lombart.common.views

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

public abstract class PaginationScrollListener(private var layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    abstract fun isHasNext(): Boolean

    abstract fun isLoading(): Boolean

    abstract fun loadMoreItems()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        Log.d("testP", "herse")

        // Check if the next page is not currently loading and if we actually have the next page.
        if (!isLoading() && isHasNext()) {
            val visibleItemsCount = layoutManager.childCount
            val totalItemsCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            // Check if the last visible item is last in the list, too.
            // If so, then load the next page.
            if (visibleItemsCount + firstVisibleItemPosition >= totalItemsCount && firstVisibleItemPosition >= 0) {
                Log.d("testP", "here")
                loadMoreItems()
            }
        }
    }
}