package com.funnyjack.monolith.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
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

    @Column(name = "is_super_admin", nullable = false)
    var superAdmin: Boolean = false,

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

    @Column(name = "referral_code", length = 7)
    var referralCode: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referrer_id")
    var referrer: User? = null,

    @OneToMany(mappedBy = "referrer", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var referredUsers: MutableSet<User> = mutableSetOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var orders: List<Order> = mutableListOf()
)

@Repository
interface UserRepository : CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    fun existsByOpenid(openid: String): Boolean
    fun findByOpenid(openid: String): User?
    fun existsByOpenidAndSuperAdminTrue(openid: String): Boolean
}
