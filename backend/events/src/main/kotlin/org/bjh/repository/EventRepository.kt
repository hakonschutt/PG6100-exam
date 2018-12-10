package org.bjh.repository

import org.bjh.entity.EventEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository:CrudRepository<EventEntity,Long>