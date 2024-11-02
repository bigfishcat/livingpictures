package com.github.bigfishcat.livingpictures.domain

import android.util.Log
import com.github.bigfishcat.livingpictures.model.PageUiState

class PagesRepository {
    private val _pages = mutableListOf(PageUiState())
    val pages: List<PageUiState> = _pages

    val lastPage: PageUiState
        get() {
            if (_pages.isEmpty()) {
                _pages += PageUiState()
            }
            return pages.last()
        }

    fun create(): PageUiState {
        val page = PageUiState()
        _pages += page
        return page
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

    private fun getPageIndex(page: PageUiState): Int {
        return _pages.indexOfLast { it.id == page.id }
    }
}