package main.kotlin.org.bjh.repository

import main.kotlin.org.bjh.entity.VenuesEntity
import org.springframework.data.repository.PagingAndSortingRepository

interface VenuesRepository : PagingAndSortingRepository<VenuesEntity, Long>{

}
