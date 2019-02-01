package com.mageddo.sqldatapartitioning.dao

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity
import com.mageddo.sqldatapartitioning.enums.SkinPriceType
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders.FROM
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

import java.sql.Timestamp
import java.time.LocalDate

@Repository
class SkinPriceByDayDAOPg(private val parameterJdbcTemplate: NamedParameterJdbcTemplate) : SkinPriceByDayDAO {

	override fun create(skinPriceEntity: SkinPriceEntity) {
		parameterJdbcTemplate.update(
	"""
			INSERT INTO BSK_SKIN_SALE_HISTORY_BY_DAY (
				DAT_OCCURRENCE, IND_TYPE, NUM_PRICE, COD_SKIN
			) VALUES (
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
			SELECT * FROM BSK_SKIN_SALE_HISTORY_BY_DAY BSSH
			WHERE BSSH . DAT_OCCURRENCE > :from
			AND BSSH . DAT_OCCURRENCE < :to
			AND BSSH . IND_TYPE = :type
			AND BSSH . IDT_BSK_SKIN_SALE_HISTORY = :id
			""",
			mapOf(
				"from" to Timestamp.valueOf(date.atStartOfDay()),
				"to" to Timestamp.valueOf(date.plusDays(1).atStartOfDay()),
				"type" to SkinPriceType.RAW.code,
				"id" to id!!
			),
			SkinPriceEntity.mapper()
			)
		} catch (e: EmptyResultDataAccessException) {
			return null
		}
	}

	override fun createPartition(date: LocalDate, type: SkinPriceType): String {
		return parameterJdbcTemplate.queryForObject(
			"""SELECT createSkinSaleByDayPartition(:from, :to, :type)""",
			MapSqlParameterSource()
				.addValue("from", date.toString())
				.addValue(
					"to", date.plusDays(1).atStartOfDay().minusNanos(1).toString()
				)
				.addValue("type", type.code),
			String::class.java
		)
	}
}
