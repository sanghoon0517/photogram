package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>{
	
	@Modifying //INSERT,DELETE,UPDATE를 네이티브 쿼리로 작성하려면 해당 어노테이션이 필요하다.
	@Query(value="INSERT INTO subscribe(fromUserId,toUserId,createDate) VALUES (:fromUserId, :toUserId, now())", nativeQuery = true)
	void mSubscribe(int fromUserId, int toUserId); //int인 이유 : 성공하면 N(변경된 행의 개수만큼), 실패하면 -1을 리턴한다.
	
	@Modifying
	@Query(value="DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	void mUnSubscribe(int fromUserId, int toUserId);
	
	@Query(value="SELECT COUNT(*) FROM SUBSCRIBE WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
	int mSubscribeState(int principalId, int pageUserId);
	
	@Query(value="SELECT COUNT(*) FROM SUBSCRIBE WHERE fromUserId = :pageUserId", nativeQuery = true)
	int mSubscribeCount(int pageUserId);
}
