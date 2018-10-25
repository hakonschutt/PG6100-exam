package org.bjh.dto

import main.kotlin.org.bjh.dto.VenueDto
import org.bjh.wrappers.WrappedResponse
//TODO Ask andrea if we are supposed to create one response for one object and one for multiple
class VenueResponseDto(code: Int?, data: VenueDto?, message: String?=null, status: ResponseStatus?=null) : WrappedResponse<VenueDto>(code, data, message, status) {
}