package com.cafe24.persistencecontext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.cafe24.persistencecontext.domain.Member;

public class App {
    public static void main( String[] args ){
    	// 1. Entity Manager Factory 생성
    	EntityManagerFactory emf = 
    			Persistence.createEntityManagerFactory("persistencecontext");
    	
    	// 2. Entity Manager 생성
    	EntityManager em = emf.createEntityManager();
    	
    	// 3. GetTx 트랜잭션
    	EntityTransaction tx = em.getTransaction(); 
    	
    	// 4. TX Begins
    	tx.begin();
    	
    	// 5. Business Logic
    	try {
    		// testInsert(em);
    		
    		// testFind01(em);
    		
    		// testFind02(em);
    		
    		testIdendtity(em);
    	} catch(Exception e) {
    		tx.rollback();
    		e.printStackTrace();
    	}
    	
    	// 6. TX Commit
    	tx.commit();
    	
    	// 7. Entity Manager Close
    	em.close();
    	
    	// 8. Entity Manager Factory 종료
    	emf.close();
    }
    
    // 동질성 테스트 (Singleton)
    // Persistence.xml ==> <property name="hibernate.hbm2ddl.auto" value="update" />
    public static void testIdendtity(EntityManager em) {
    	// a, b 생성할 때마다 쿼리를 때리는 것이 아니라
    	// b객체를 생성할 때는 1차캐시에서 가져오게된다. 즉, 영속화된 객체를 찾는 것임.
    	// DB에서 member1 id값 조회
    	Member a = em.find(Member.class, "member1");
 
    	// b는 1차 캐시에서 값을 조회한 값
    	Member b = em.find(Member.class, "member1");
    	
    	System.out.println(a==b);    	
    }
    
    // DB에 있는 데이터를 가져오는 경우
    // Persistence.xml ==> <property name="hibernate.hbm2ddl.auto" value="update" />
    public static void testFind02(EntityManager em) {
    	// DB에서 member1 id값 조회
    	Member findMember = em.find(Member.class, "member1");
    	System.out.println(findMember);
    }
    
    // 1차 캐시에서 데이터 조회
    // Persistence.xml ==> <property name="hibernate.hbm2ddl.auto" value="create" />
    public static void testFind01(EntityManager em) {
    	// 1차 캐시에 로드 될 때
    	Member member = new Member(); //Entity
    	
    	member.setId("member1"); // @Id값
    	member.setName("회원1");
    	
    	// 1. 1차 캐시 저장
    	em.persist(member);
    	
    	// DB에 가서 데이터를 가져온 것이 아닌
    	// 1차 캐시에서 조회를 한 후에 데이터를 가져온거임
    	Member findMember = em.find(Member.class, "member1");
    	System.out.println(findMember);
    }
    
    // member insert
    // Persistence.xml ==> <property name="hibernate.hbm2ddl.auto" value="create" />
    public static void testInsert(EntityManager em) {
    	// 1차 캐시에 로드 될 때
    	Member member = new Member(); //Entity
    	
    	member.setId("member1"); // @Id값
    	member.setName("회원1");
    	
    	em.persist(member);
    }
}
