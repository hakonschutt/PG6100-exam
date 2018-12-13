package org.bjh.repository

import org.bjh.entity.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthenticationRepository : CrudRepository<UserEntity, String>