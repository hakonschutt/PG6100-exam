package org.bjh.repository

import org.bjh.entity.VenueEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface VenuesRepository : PagingAndSortingRepository<VenueEntity, Long>{

}
