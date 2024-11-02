package com.github.bigfishcat.livingpictures.domain

import android.util.Log
import com.github.bigfishcat.livingpictures.model.PageUiState

class PagesRepository internal constructor(
    private val _pages: MutableList<PageUiState>
) {
    val pages: List<PageUiState> = _pages

    val lastPage: PageUiState
        get() {
            if (_pages.isEmpty()) {
                _pages += PageUiState()
            }
            return pages.last()
        }

    constructor() : this(mutableListOf(PageUiState()))

    fun create(): PageUiState {
        val page = PageUiState()
        _pages += page
        return page
    }

    fun clone(page: PageUiState): PageUiState {
        val pageIndex = getPageIndex(page)
        val newPage = PageUiState(objects = page.objects)
        if (pageIndex < 0) {
            Log.e("PAGES_REPOSITORY", "Page with id=${page.id} not found in repository")
            _pages += newPage
        } else if (pageIndex == pages.lastIndex) {
            _pages += newPage
        } else {
            _pages.add(pageIndex + 1, newPage)
        }
        return newPage
    }

    fun update(page: PageUiState) {
        val pageIndex = getPageIndex(page)
        if (pageIndex < 0) {
            Log.e("PAGES_REPOSITORY", "Page with id=${page.id} not found in repository")
            return
        }
        _pages[pageIndex] = page
    }

    /**
     * @return next page from removed or new empty page if no one left
     */
    fun remove(page: PageUiState): PageUiState {
        val pageIndex = getPageIndex(page)
        if (pageIndex < 0) {
            Log.e("PAGES_REPOSITORY", "Page with id=${page.id} not found in repository")
            return lastPage
        }
        _pages.removeAt(pageIndex)

        return if (pageIndex >= pages.size) {
            lastPage
        } else {
            pages[pageIndex]
        }
    }

    fun removeAll(): PageUiState {
        _pages.clear()
        _pages += PageUiState()
        return lastPage
    }

    private fun getPageIndex(page: PageUiState): Int {
        return _pages.indexOfLast { it.id == page.id }
    }
}