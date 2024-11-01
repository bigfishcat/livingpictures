package com.github.bigfishcat.livingpictures.domain

import com.github.bigfishcat.livingpictures.model.PageUiState

class PagesRepository {
    private val _pages = mutableListOf<PageUiState>()
    val pages: List<PageUiState> = _pages

    fun push(page: PageUiState) {
        _pages += page
    }

    fun pop(): PageUiState? {
        return _pages.removeLastOrNull()
    }
}