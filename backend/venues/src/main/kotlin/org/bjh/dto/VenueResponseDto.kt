package org.bjh.dto

import org.bjh.pagination.PageDto
import org.bjh.wrappers.WrappedResponse

//TODO Ask andrea if we are supposed to create one response for one object and one for multiple
class VenueResponseDto(code: Int?,
                       data: PageDto<VenueDto>,
                       message: String? = null,
                       status: ResponseStatus? = null)
    : WrappedResponse<PageDto<VenueDto>>(code, data, message, status) {
}