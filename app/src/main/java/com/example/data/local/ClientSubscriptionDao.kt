package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientSubscriptionDao {
    @Query("SELECT * FROM client_subscriptions ORDER BY createdAt DESC")
    fun getAllSubscriptions(): Flow<List<ClientSubscriptionEntity>>

    @Query("SELECT * FROM client_subscriptions WHERE id = :id")
    suspend fun getSubscriptionById(id: Long): ClientSubscriptionEntity?

    @Query("SELECT * FROM client_subscriptions WHERE status = :status ORDER BY createdAt DESC")
    fun getSubscriptionsByStatus(status: String): Flow<List<ClientSubscriptionEntity>>

    @Query("""
        SELECT * FROM client_subscriptions 
        WHERE fullName LIKE '%' || :query || '%' 
           OR phoneNumber LIKE '%' || :query || '%' 
           OR phoneModel LIKE '%' || :query || '%' 
           OR serviceName LIKE '%' || :query || '%'
           OR transactionRef LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """)
    fun searchSubscriptions(query: String): Flow<List<ClientSubscriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: ClientSubscriptionEntity): Long

    @Update
    suspend fun updateSubscription(subscription: ClientSubscriptionEntity)

    @Query("UPDATE client_subscriptions SET status = :status, isApproved = :approved WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, approved: Boolean)

    @Query("UPDATE client_subscriptions SET ownerNotes = :notes WHERE id = :id")
    suspend fun updateOwnerNotes(id: Long, notes: String)

    @Query("DELETE FROM client_subscriptions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun deleteSubscription(subscription: ClientSubscriptionEntity)

    @Query("DELETE FROM client_subscriptions")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM client_subscriptions")
    fun getSubscribersCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(priceAmount), 0) FROM client_subscriptions WHERE currency = 'YER' AND (status = 'تم التحقق والسداد' OR status = 'مكتمل' OR isApproved = 1)")
    fun getTotalRevenueYER(): Flow<Int>

    @Query("SELECT COALESCE(SUM(priceAmount), 0) FROM client_subscriptions WHERE currency = 'SAR' AND (status = 'تم التحقق والسداد' OR status = 'مكتمل' OR isApproved = 1)")
    fun getTotalRevenueSAR(): Flow<Int>

    @Query("SELECT COALESCE(SUM(priceAmount), 0) FROM client_subscriptions WHERE currency = 'USD' AND (status = 'تم التحقق والسداد' OR status = 'مكتمل' OR isApproved = 1)")
    fun getTotalRevenueUSD(): Flow<Int>
}
