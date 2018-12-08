package org.bjh.pagination

import io.swagger.annotations.ApiModelProperty


/*
   Taken from Andreas repo
 */
open class HalObject(

        @ApiModelProperty("HAL links")
        var _links: MutableMap<String, HalLink> = mutableMapOf()
)
