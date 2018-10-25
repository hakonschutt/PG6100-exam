package org.bjh.service

import org.bjh.converter.RoomConverter
import org.bjh.dto.RoomDto
import org.bjh.entity.RoomEntity
import org.bjh.repository.RoomRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
//ha-ha RoomService, please bring me food.
class RoomService {
    private lateinit var roomRepository: RoomRepository
    fun findAll(): Set<RoomDto> {
        val rooms =  roomRepository.findAll().map{
            roomEntity -> RoomConverter.transform(roomEntity)
        }

        return rooms.toSet()
    }

    fun saveAll(rooms: Set<RoomDto>): Any {

        return roomRepository.saveAll(rooms.map {
            roomDto -> RoomEntity(id = null,name = roomDto.name,rows = roomDto.rows, columns = roomDto.columns)
        })

    }
}
