package kz.pillikan.lombart.common.models

class Page<T> {

    private var content: List<T>? = null
    private var pageNumber = 0
    private var hasNextPage = false

    constructor(content: List<T>?, pageNumber: Int, hasNextPage: Boolean) {
        this.content = content
        this.pageNumber = pageNumber
        this.hasNextPage = hasNextPage
    }

    fun getContent(): List<T>? {
        return content
    }

    fun setContent(content: List<T>?) {
        this.content = content
    }

    fun getPageNumber(): Int {
        return pageNumber
    }

    fun setPageNumber(pageNumber: Int) {
        this.pageNumber = pageNumber
    }

    fun hasNextPage(): Boolean {
        return hasNextPage
    }

    fun setHasNextPage(hasNextPage: Boolean) {
        this.hasNextPage = hasNextPage
    }
}