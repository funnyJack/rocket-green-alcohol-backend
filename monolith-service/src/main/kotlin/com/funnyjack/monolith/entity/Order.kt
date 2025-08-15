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
    var contractType: ContractType
)

enum class ContractType(val displayName:String) {
    HYDROGEN_FRIENDS("氢友"),
    CONTRACTED_OWNER("签约车主"),
    GREEN_ALCOHOL_PIONEER("绿醇先锋"),
    GREEN_ALCOHOL_PARTNERS("绿醇合伙人")
}

@Repository
interface OrderRepository : CrudRepository<Order, Long>
