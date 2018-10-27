package org.bjh.service

import org.bjh.converter.RoomConverter
import org.bjh.dto.RoomDto
import org.bjh.entity.RoomEntity
import org.bjh.repository.RoomRepository
import org.springframework.stereotype.Service

@Service
//ha-ha RoomService, please bring me food.
class RoomService {
    private lateinit var roomRepository: RoomRepository
    fun save(roomDto: RoomDto): RoomEntity {
        if (!(roomDto.name == null || roomDto.rows == null || roomDto.columns == null)) {
            return RoomEntity(id = null, name = null, rows = null, columns = null)
        }
        return roomRepository.save(RoomEntity(id = null, name = roomDto.name!!, rows = roomDto.rows!!, columns = roomDto.columns!!))

    }

    fun findAll(): Set<RoomDto> {
        val rooms = roomRepository.findAll().map { roomEntity ->
            RoomConverter.transform(roomEntity)
        }

        return rooms.toSet()
    }

    fun findById(id: Long): RoomDto {
        val room = roomRepository.findById(id)

        return when {
            room.isPresent -> RoomConverter.transform(room.get())
            else ->
                return RoomDto(id = null, name = null, rows = null, columns = null)
        }
    }

    fun saveAll(rooms: Set<RoomDto>): Set<RoomEntity> {
        val validRooms = !rooms.any { it.name == null || it.rows == null || it.columns == null }

        if (!validRooms) return setOf()

        return roomRepository.saveAll(rooms.map {

            roomDto ->
            RoomEntity(id = null, name = roomDto.name!!, rows = roomDto.rows!!, columns = roomDto.columns!!)
        }).toSet()

    }
}
