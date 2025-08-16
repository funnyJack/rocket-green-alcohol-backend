package com.funnyjack.monolith.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Entity
@Table(name = "\"user\"")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var openid: String,

    @Column(nullable = false)
    var isSuperAdmin: Boolean = false,

    @Column
    var avatarUrl: String? = null,

    @Column(length = 255)
    var nickname: String? = null,

    @Column(length = 255)
    var phoneNumber: String? = null,

    @Column
    var address: String? = null,

    @Column
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var currentContractType: ContractType? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var orders: List<Order> = mutableListOf()
)

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByOpenid(openid: String): User?
}
