package com.funnyjack.monolith.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Entity
@Table(name = "\"order\"")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var openid: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "openid", referencedColumnName = "openid", insertable = false, updatable = false)
    var user: User? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var contractType: ContractType
)

enum class ContractType {
    HYDROGEN_FRIENDS,
    CONTRACTED_OWNER,
    GREEN_ALCOHOL_PIONEER,
    GREEN_ALCOHOL_PARTNERS
}

@Repository
interface OrderRepository : CrudRepository<Order, Long> {
    fun findByOpenid(openid: String): List<Order>
}