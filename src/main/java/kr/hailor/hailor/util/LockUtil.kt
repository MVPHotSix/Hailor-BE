package kr.hailor.hailor.util

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@Component
class LockUtil(
    private val redissonClient: RedissonClient,
) {
    fun lock(
        lockName: String,
        waitTime: Long,
        leaseTime: Long,
        timeUnit: TimeUnit,
    ): Boolean {
        val rLock = redissonClient.getLock(lockName)
        return rLock.tryLock(waitTime, leaseTime, timeUnit)
    }

    fun unlock(lockName: String) {
        val rLock = redissonClient.getLock(lockName)
        rLock.unlock()
    }

    fun getReservationLockKey(
        designerId: Long,
        reservationDate: LocalDate,
    ): String = "reservation:$designerId:$reservationDate"
}
