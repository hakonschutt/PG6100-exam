package org.bjh.dto

import org.bjh.wrappers.WrappedResponse

class MultipleVenuesResponseDto(code: Int?,
                                data: List<VenueDto>?,
                                message: String?=null,
                                status: ResponseStatus?=null)
    : WrappedResponse<List<VenueDto>>(code, data, message, status)