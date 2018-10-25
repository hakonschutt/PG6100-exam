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

        fun transform(entities: Iterable<RoomEntity>): List<RoomDto> {
            return entities.map { transform(it) }
        }
    }
}
