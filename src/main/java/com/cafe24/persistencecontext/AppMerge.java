package com.cafe24.persistencecontext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.cafe24.persistencecontext.domain.Member;

public class AppMerge {
	// 1. Entity Manager Factory 생성
	private static EntityManagerFactory emf = 
			Persistence.createEntityManagerFactory("persistencecontext");
    public static void main( String[] args ){
    	Member member = createMember("user5", "회원5");
    	
    	// 준영속 상태에서 변경
    	member.setName("도웅넛");
    	
    	mergeMember(member);
    }
    
    public static Member createMember(String id, String name) {
    	//영속성 컨텍스트 시작
    	EntityManager em = emf.createEntityManager();
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	
    	// Create Entity = 비영속 상태(New)
    	Member member = new Member();
    	member.setId(id);
    	member.setName(name);
		
		// memberA 영속화 (1차 캐시에 객체 값 로드)
		// 영속 상태(Managed) Flush는 아직 안되어 있는 상태
		em.persist(member);
		
		// flush()..
		tx.commit();
		
		// 영속성 컨텍스트 종료
		// member 엔티티는 준영속 상태
		em.close();
		
		return member;
    }
    
    public static void mergeMember(Member member) {
    	/*
    	 * 긴 로직 처리 후 UPDATE를 떄려야될 때 이렇게 비영속을 만들고 처리함.
    	 */
    	//영속성 컨텍스트 시작
    	EntityManager em = emf.createEntityManager();
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	
    	// 비영속에서 영속
    	// merge할 경우 1차 캐시에 만약에 없다면 디비에서 찾아봄.
    	Member mergeMember = em.merge(member);
		
		// flush()..
		tx.commit();  // 여기서 이제 update 쿼리가 날라가게됨.
		
		System.out.println("member = " + member.getName());
		System.out.println("mergeMember = " + mergeMember.getName());
		
		// entity manager가 member를 가지고 있는지 확인하는 작업.
		System.out.println("em contains member = " + em.contains(member));
		// entity manager가 MergeMember를 가지고 있는지 확인하는 작업.
		System.out.println("em contains mergeMember = " + em.contains(mergeMember));
		
		// 영속성 컨텍스트 종료
		// member 엔티티는 준영속 상태
		em.close();
    }
}
