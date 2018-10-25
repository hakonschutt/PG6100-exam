package main.kotlin.org.bjh.repository

import main.kotlin.org.bjh.entity.VenueEntity
import org.springframework.data.repository.PagingAndSortingRepository

interface VenuesRepository : PagingAndSortingRepository<VenueEntity, Long>{

}
