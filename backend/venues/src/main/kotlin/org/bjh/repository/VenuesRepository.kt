package org.bjh.repository

import org.bjh.entity.VenueEntity
import org.springframework.data.repository.PagingAndSortingRepository

interface VenuesRepository : PagingAndSortingRepository<VenueEntity, Long>{

}
