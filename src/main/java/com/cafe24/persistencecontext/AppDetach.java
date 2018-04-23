package com.cafe24.persistencecontext;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.cafe24.persistencecontext.domain.Member;

public class AppDetach {
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
    		// 비영속 테스트
    		testDetached(em);
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
    
    public static void testDetached(EntityManager em) {
    	// Create Entity = 비영속 상태(New)
    	Member user4 = new Member();
		user4.setId("user4");
		user4.setName("유저4");
		
		// memberA 영속화 (1차 캐시에 객체 값 로드)
		// 영속 상태(Managed) Flush는 아직 안되어 있는 상태
		em.persist(user4);
		
		// 회원 엔티티를 영속 상태에서 준영속 상태(영속성 컨텍스트에서 분리)로 만드는 작업
		em.detach(user4);
		
		// update가 안됨
		System.out.println(user4);
		user4.setName("마이콜");
		System.out.println(user4); //==> user3의 값은 마이콜로 변경되있지만 DB에서 값을 확인하게 되면 값이 안바뀌어있다는 것을 알 수 있따.
								   // Detach상태였기 때문에 commit이 된다해도 값이 변경되지가 않음.
    }
    
    public static void testEMClear(EntityManager em) {
    	// 엔티티 조회, 영속 상태
    	Member member = em.find(Member.class, "member1");
    	
    	// 영속성 컨텍스트를 초기화하는 메소드
    	em.clear();
    	
    	// 준영속 상태
    }
}
