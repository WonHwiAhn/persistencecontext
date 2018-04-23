package com.cafe24.persistencecontext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.cafe24.persistencecontext.domain.Member;

public class AppUPDATE {
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
    		 * UPDATE
    		 */
    		Member memberC = new Member();
    		memberC.setId("memberC");
    		memberC.setName("회원C");
    		
    		// memberB 영속화 (1차 캐시에 객체 값 로드)
    		em.persist(memberC); // 여기까지는 Snapshot과 Entity 값이 동일
    		
    		// memberC의 이름을 UPDATE
    		// 이름이 바뀌게 됨으로써 Dirty(값이 변경)됨.
    		// 즉, Snapshot과 Entity값이 달라짐. [Dirty Checking에 의해 파악]
    		// Commit이 될 때 update를 해주게됨.
    		// 따라서, em.update(memberC) ==> update메소드는 존재하지 않다.
    		memberC.setName("둘리");
    		
    		/*
    		 *  PS. UPDATE Query를 살펴보면 항상 모든 컬럼 값을 SET함.
    		 *      EX) 내가 name값만 변경해도 컬럼에 age, address가 있다면 항상 UPDATE를 때릴 때
    		 *      UPDATE table_name SET name=?, age=?, address=? WHERE id=?  <== 요런식으로 값이 생성됨.
    		 *      미리 쿼리를 생성할 수 있다는 점에서 성능상 이점이 있음. (setName, setAge를 어디서 하든 간에 1번만 때림.)
    		 *  
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
    	// 6-1. 커밋하는 순간에 Dirty Checking(변경감지)을 한다.
    	// 6-2. 변경이 감지되면 update sql을 저장소에 저장
    	// 6-3. flush()
    	tx.commit(); //[트랜잭션] 커밋
    	
    	// 7. Entity Manager Close
    	em.close();
    	
    	// 8. Entity Manager Factory 종료
    	emf.close();
    }
    
}
