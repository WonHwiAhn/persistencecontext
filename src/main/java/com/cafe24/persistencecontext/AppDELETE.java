package com.cafe24.persistencecontext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.cafe24.persistencecontext.domain.Member;

public class AppDELETE {
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
    		/*
    		 * DELETE
    		 */
    		
    		// 삭제할 대상 엔티티 조회
    		Member memberA = em.find(Member.class, "memberA");
    		
    		// 엔티티 삭제
    		// 삭제를 함으로 써 쓰기지연 SQL에 delete쿼리가 생성되게된다.
    		// 그래서 값을 삭제하게 됨. memberA에 값이 있긴하지만 사용하지 않을 것을 권장!
    		em.remove(memberA);
    	} catch(Exception e) {
    		tx.rollback();
    		e.printStackTrace();
    	}
    	
    	// 6. TX Commit
    	// 커밋되는 순간 쓰기지연 SQL에 있는 delete쿼리를 DB에 보낸다.
    	tx.commit(); //[트랜잭션] 커밋
    	
    	// 7. Entity Manager Close
    	em.close();
    	
    	// 8. Entity Manager Factory 종료
    	emf.close();
    }
    
}
