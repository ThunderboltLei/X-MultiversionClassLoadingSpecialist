//package com.pyramid.testE;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
//import dagger.Provides;
//
//public class Test {
//
//	@Singleton
//	class A {
//
//		@Inject
//		public A() {
//			// TODO Auto-generated constructor stub
//		}
//
//		@Provides
//		@Singleton
//		public void provideA() {
//			System.out.println("--->>> singleton: A");
//		}
//	}
//
//	class B {
//		public B() {
//			// TODO Auto-generated constructor stub
//		}
//
//		public void provideB() {
//			System.out.println("--->>> B");
//		}
//	}
//
//	class C extends A {
//		public C() {
//			// TODO Auto-generated constructor stub
//		}
//
//		public void provideC() {
//			System.out.println("--->>> C");
//		}
//	}
//
//	private A a1, a2;
//	private B b;
//	private C c;
//
//	public Test() {
//		// TODO Auto-generated constructor stub
//		a1 = new A();
//		a2 = new A();
//		b = new B();
//		a1 = new A();
//		c = new C();
//	}
//
//	public void doTest() {
//		a1.provideA();
//		a2.provideA();
//		b.provideB();
//		a1.provideA();
//		c.provideA();
//	}
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		Test test = new Test();
//		test.doTest();
//	}
//
//}
