package com.mageddo.sqldatapartitioning.dao

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity

import java.time.LocalDate

interface SkinPriceNoPtDAO {

	fun create(skinPriceEntity: SkinPriceEntity)

	fun find(date: LocalDate, id: Long?): SkinPriceEntity?

}
