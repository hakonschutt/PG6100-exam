package org.bjh.wrapper

import org.bjh.dto.UserDetailDto
import org.bjh.pagination.PageDto
import org.bjh.wrappers.WrappedResponse

//TODO Ask andrea if we are supposed to create one response for one object and one for multiple
class UserDetailWrapper(code: Int?,
                       data: PageDto<UserDetailDto>,
                       message: String? = null,
                       status: ResponseStatus? = null)
    : WrappedResponse<PageDto<UserDetailDto>>(code, data, message, status)
