package edu.ort.pastillapp.Services

data class PaginateResponse<T>(
    val results: List<T>
)