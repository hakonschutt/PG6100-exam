package org.bjh.pagination

import io.swagger.annotations.ApiModelProperty

/*
 Taken from Andreas repo
 */
open class HalLink(

        @ApiModelProperty("URL of the link")
        var href: String = ""
)