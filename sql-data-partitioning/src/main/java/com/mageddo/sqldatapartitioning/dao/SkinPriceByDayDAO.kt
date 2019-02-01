package com.mageddo.sqldatapartitioning.dao

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity
import com.mageddo.sqldatapartitioning.enums.SkinPriceType

import java.time.LocalDate

interface SkinPriceByDayDAO {

	fun create(skinPriceEntity: SkinPriceEntity)

	fun find(date: LocalDate, id: Long?): SkinPriceEntity?

	fun createPartition(date: LocalDate, type: SkinPriceType): String
}
