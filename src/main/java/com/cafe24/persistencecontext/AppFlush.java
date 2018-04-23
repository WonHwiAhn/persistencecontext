package com.cafe24.persistencecontext;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.cafe24.persistencecontext.domain.Member;

public class AppFlush {
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
    		Member user1 = new Member();
    		user1.setId("user1");
    		user1.setName("유저1");
    		
    		// memberA 영속화 (1차 캐시에 객체 값 로드)
    		em.persist(user1);
    		
    		Member user2 = new Member();
    		user2.setId("user2");
    		user2.setName("유저2");
    		
    		// memberB 영속화 (1차 캐시에 객체 값 로드)
    		em.persist(user2);
    		
    		
    		// 쿼리 날라가기 전에 flush가 실행.
    		// 즉, JPQL을 하기 전에 쓰기지연 SQL에 쌓여있는 값들이 몽땅 DB에 QUERY날려서 동일하게 만듬.
    		TypedQuery<Member> query = 
    				em.createQuery("SELECT m FROM Member m", Member.class);
    		List<Member> list = query.getResultList();
    		
    		for(Member member : list) {
    			System.out.println(member);
    		}
    		
    		/*
    		 *  @Entity
    		 *  @Table...
    		 *  @org.hibernate.annotation.DynamicUpdate ==> 이렇게 생성하면 update 쿼리를 변경될 때마다 때림. (필드에 값이 여러개라면 효율적.)
    		 *  Class Member{
    		 *  }
    		 *  
    		 *  em.setFlushMode(FlushModeType.AUTO) ==> 기본셋팅, commit, JPQL 실행할 때 모두 flush
    		 *  em.setFlushMode(FlushModeType.COMMIT) ==> commit만 flush
    		 *  =====> em.flush ==> 이놈은 직접 명령하는거라 언제나 
    		 */
    	} catch(Exception e) {
    		tx.rollback();
    		e.printStackTrace();
    	}
    	
    	// 6. TX Commit
    	tx.commit(); //[트랜잭션] 커밋
    	
    	// 7. Entity Manager Close
    	em.close();
    	
    	// 8. Entity Manager Factory 종료
    	emf.close();
    }
    
}
