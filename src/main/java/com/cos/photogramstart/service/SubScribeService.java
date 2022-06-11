package com.cos.photogramstart.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubScribeService {
	
	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; //모든 Repository는 EntityManager를 구현해서 만들어져 있는 구현체이다.
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalId, int pageUserId) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, u.profileImageUrl, ");
		sb.append("if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0 ) subscribeState, ");
		sb.append("if((?=u.id), 1, 0) equalUserState ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ?");
		
		// 물음표 principalId
		// 물음표 principalId
		// 마지막 물음표 pageUserId
		
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		//쿼리 실행(qlrm라이브러리 필요)
		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDto> subscribeDtos =  result.list(query, SubscribeDto.class);
		return subscribeDtos;
	}
	

	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch(Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다.");
		}
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) { //삭제라서 오류처리 할 필요 없음
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
