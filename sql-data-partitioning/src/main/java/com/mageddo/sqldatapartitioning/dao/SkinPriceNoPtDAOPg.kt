package com.mageddo.sqldatapartitioning.dao

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity
import com.mageddo.sqldatapartitioning.enums.SkinPriceType
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders.FROM
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

import java.sql.Timestamp
import java.time.LocalDate

import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.lastDayOfMonth

@Repository
class SkinPriceNoPtDAOPg(private val parameterJdbcTemplate: NamedParameterJdbcTemplate) : SkinPriceNoPtDAO {

	override fun create(skinPriceEntity: SkinPriceEntity) {
		parameterJdbcTemplate.update(
			"""
			INSERT INTO BSK_SKIN_SALE_HISTORY_NO_PT (
				DAT_OCCURRENCE, IND_TYPE, NUM_PRICE, COD_SKIN
			) VALUES(
			:occurrence, :type, :price, :hash
			)
			""",
			mapOf(
				"occurrence" to Timestamp.valueOf(skinPriceEntity.occurrence),
				"type" to skinPriceEntity.type.code,
				"price" to skinPriceEntity.price,
				"hash" to skinPriceEntity.hashName
			)
		)
	}

	override fun find(date: LocalDate, id: Long?): SkinPriceEntity? {
		try {
			return parameterJdbcTemplate.queryForObject<SkinPriceEntity>(
				"""
			SELECT * FROM BSK_SKIN_SALE_HISTORY_NO_PT BSSH
			WHERE BSSH . DAT_OCCURRENCE > :from
			AND BSSH . DAT_OCCURRENCE < :to
			AND BSSH . IND_TYPE = :type
			AND BSSH . IDT_BSK_SKIN_SALE_HISTORY = :id
			""",
				mapOf(
					"from" to Timestamp.valueOf(date.with(firstDayOfMonth()).atStartOfDay()),
					"to" to Timestamp.valueOf(date.with(lastDayOfMonth()).plusDays(1).atStartOfDay()),
					"type" to SkinPriceType.RAW.code,
					"id" to id!!
				),
				SkinPriceEntity.mapper()
			)
		} catch (e: EmptyResultDataAccessException) {
			return null
		}

	}

}
