package com.example.data.services

import android.accounts.NetworkErrorException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.models.Page
import com.example.data.models.query_params.PageParams

public const val DEFAULT_PAGING_SIZE: Long = 20
public const val MAX_PAGES_IN_MEMORY: Long = 10
public const val DEFAULT_MAX_SIZE: Long = DEFAULT_PAGING_SIZE * MAX_PAGES_IN_MEMORY

class MyPagingSource<T: Any>(
    private val getPage: suspend (Map<String,String>) -> Page<T>,
    private val limit: Long = DEFAULT_PAGING_SIZE
) : PagingSource<Int, T>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, T> {
        try {
            val nextPageNumber = params.key ?: 0
            val page = getPage(
                PageParams(
                    offset = nextPageNumber * limit,
                    limit = limit
                ).toMap()
            )
            return LoadResult.Page(
                data = page.content,
                prevKey = if (page.page.number < 1) null else page.page.number.toInt() - 1, // Only paging forward.
                nextKey = if (page.page.number + 1 >= page.page.totalPages) null else page.page.number.toInt() + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}