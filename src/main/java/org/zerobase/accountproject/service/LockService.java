package org.zerobase.accountproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.zerobase.accountproject.exception.AccountException;
import org.zerobase.accountproject.type.ErrorCode;

import java.util.concurrent.TimeUnit;

import static org.zerobase.accountproject.type.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {

    private final RedissonClient redissonClient;
    


    public void lock(String accountNumber) throws AccountException {
        RLock lock = redissonClient.getLock(getLockKey(accountNumber));
        log.debug("Trying lock for accountNumber : {}", accountNumber);

        try{
            boolean isLock = lock.tryLock(1,15, TimeUnit.SECONDS);
            if(!isLock){
                log.error("=======Lock acquisition failed======");
                throw new AccountException(ACCOUNT_ALREADY_UNREGISTERED);
            }
        } catch (AccountException e){
            throw e;
        } catch (Exception e){
            log.error("Redis lock 실패", e);
        }
    }

    public void unlock(String accountNumber){
        log.debug("Unlock for accountNumber : {} " , accountNumber);
        redissonClient.getLock(getLockKey(accountNumber)).unlock();
    }

    private String getLockKey(String accountNumber) {
        return "ALCK:" + accountNumber;
    }
}
