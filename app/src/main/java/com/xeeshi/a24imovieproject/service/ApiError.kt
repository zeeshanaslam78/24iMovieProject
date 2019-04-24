package com.xeeshi.a24imovieproject.service

import com.xeeshi.a24imovieproject.model.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response

object ApiError {

    fun <T> parseError(response: Response<T>) : ErrorResponse {
        //arrayOf(object : Annotation{})
        val converter: Converter<ResponseBody, ErrorResponse> =
            MoviesApiService.retrofit.responseBodyConverter(ErrorResponse::class.java,
                arrayOfNulls<Annotation>(0))

        val errorResponse: ErrorResponse
        try {
            errorResponse = converter.convert(response.errorBody()!!)!!
        } catch (t: Throwable) {
            return ErrorResponse()
        }

        return errorResponse
    }

}