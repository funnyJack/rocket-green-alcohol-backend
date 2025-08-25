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

    @ManyToOne(fetch = FetchType.LAZY)
    var user: User? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var contractType: ContractType,

    @Column
    var contractedOwnerId: Int? = null,

    @Column
    var contractedOwnerAmount: Int? = null,

    @Column
    var greenAlcoholPioneerId: Int? = null,

    @Column
    var greenAlcoholPioneerAmount: Int? = null,

    @Column
    var greenAlcoholPartnersId: Int? = null,

    @Column
    var greenAlcoholPartnersAmount: Int? = null
)

enum class ContractType(val displayName: String, val totalGrossProfit: Int) {
    CONTRACTED_OWNER("签约车主", 200),
    GREEN_ALCOHOL_PIONEER("绿醇先锋", 300),
    GREEN_ALCOHOL_PARTNERS("绿醇合伙人", 1500)
}

@Repository
interface OrderRepository : CrudRepository<Order, Long>
