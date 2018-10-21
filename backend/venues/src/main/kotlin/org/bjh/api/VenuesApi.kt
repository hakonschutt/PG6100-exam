package main.kotlin.org.bjh.api

import main.kotlin.org.bjh.dto.VenuesDto
import main.kotlin.org.bjh.entity.VenuesEntity
import main.kotlin.org.bjh.repository.VenuesRepository
import main.kotlin.org.bjh.service.VenuesService
import org.bjh.wrappers.WrappedResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class VenuesController  constructor() {
    private lateinit var venuesService: VenuesService

    @GetMapping("/venues")
    fun getAllVenues(): WrappedResponse<VenuesDto> {

       val respons = WrappedResponse<VenuesDto>()
        respons.code=200
        respons.data = venuesService.findAll()
        respons.validated()

        return respons
    }



}