package com.cafe24.persistencecontext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.cafe24.persistencecontext.domain.Member;

public class AppINSERT {
    public static void main( String[] args ){
    	// 1. Entity Manager Factory 생성
    	EntityManagerFactory emf = 
    			Persistence.createEntityManagerFactory("persistencecontext");
    	
    	// 2. Entity Manager 생성
    	EntityManager em = emf.createEntityManager();
    	
    	// 3. GetTx 트랜잭션
    	EntityTransaction tx = em.getTransaction(); 
    	
    	// 4. TX Begins
    	// EntityManager는 데이터 변경 시 트랜잭션을 시작하여야 한다.
    	tx.begin(); // [트랜잭션] 시작
    	
    	// 5. Business Logic
    	try {
    		// 현재 SQL 쓰기 저장 장소를 보려고 하는 중.
    		Member memberA = new Member();
    		memberA.setId("memberA");
    		memberA.setName("회원A");
    		
    		// memberA 영속화 (1차 캐시에 객체 값 로드)
    		em.persist(memberA);
    		
    		Member memberB = new Member();
    		memberB.setId("memberB");
    		memberB.setName("회원B");
    		
    		// memberB 영속화 (1차 캐시에 객체 값 로드)
    		em.persist(memberB);
    	} catch(Exception e) {
    		tx.rollback();
    		e.printStackTrace();
    	}
    	
    	// 6. TX Commit
    	// 커밋하는 순간에 데이터베이스에 INSERT SQL을 보낸다.
    	tx.commit(); //[트랜잭션] 커밋
    	
    	// 7. Entity Manager Close
    	em.close();
    	
    	// 8. Entity Manager Factory 종료
    	emf.close();
    }
    
}
