package main.kotlin.org.bjh.api

import io.swagger.annotations.ApiParam
import main.kotlin.org.bjh.dto.VenuesDto
import main.kotlin.org.bjh.entity.VenuesEntity
import main.kotlin.org.bjh.repository.VenuesRepository
import main.kotlin.org.bjh.service.VenuesService
import org.bjh.wrappers.WrappedResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/venues")
class VenuesApi {
    private lateinit var venuesService: VenuesService

    @GetMapping("/",produces =[(MediaType.APPLICATION_JSON_VALUE)])
    fun getAllVenues(): WrappedResponse<List<VenuesDto>> {

       val respons = WrappedResponse<List<VenuesDto>>()
        respons.code = 200
        respons.data = venuesService.findAll()
        respons.validated()

        return respons
    }
    @GetMapping(path= (arrayOf("/{id}")), produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getVenue( @ApiParam("The unique id of the venue")
                  @PathVariable("id" ) id:Long): WrappedResponse<VenuesDto> {

        val respons = WrappedResponse<VenuesDto>()
        respons.code = 200
        respons.data = (venuesService.findById(id))
        respons.validated()
        return respons

    }



}