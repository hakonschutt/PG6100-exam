package org.bjh.converter

import org.bjh.dto.RoomDto
import org.bjh.entity.RoomEntity

class RoomConverter {
    companion object {

        fun transform(entity: RoomEntity): RoomDto {
            return RoomDto(
                    id = entity.id?.toString(),
                    name = entity.name,
                    rows = entity.rows,
                    columns = entity.columns
            ).apply {
                id = entity.id?.toString()
            }
        }

        fun transform(entities: Set<RoomEntity>): Set<RoomDto> {
            return entities.asSequence().map { transform(it) }.toSet()
        }
        fun transformDtoToEntity(entities: Set<RoomDto>): Set<RoomEntity> {
            return entities.asSequence().map { transform(it) }.toSet()
        }

        private fun transform(roomDto: RoomDto): RoomEntity {
            return RoomEntity(
                    name = roomDto.name,
                    rows = roomDto.rows,
                    columns = roomDto.columns
            )
        }
    }
}
