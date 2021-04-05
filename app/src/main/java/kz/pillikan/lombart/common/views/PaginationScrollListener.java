package kz.pillikan.lombart.common.views;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Listens when list's last item appears to the bottom of RecyclerView and requests more items. */
public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

    public abstract void loadMoreItems();

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        // Check if the next page is not currently loading and if we actually have the next page.
        if (!isLoading() && !isLastPage()) {
            int visibleItemsCount        = layoutManager.getChildCount();
            int totalItemsCount          = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            // Check if the last visible item is last in the list, too.
            // If so, then load the next page.
            if (visibleItemsCount + firstVisibleItemPosition >= totalItemsCount && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }
    }
}
