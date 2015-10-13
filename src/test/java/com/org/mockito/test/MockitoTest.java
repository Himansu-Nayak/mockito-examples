package com.org.mockito.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.org.pojo.Child;
import com.org.pojo.Parent;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {

	@Mock
	Child child;

	@InjectMocks
	private Parent parent = new Parent();

	@Captor
	ArgumentCaptor<Child> captor;

	@Test
	public void stubObjectTest() {
		Parent parent = new Parent();
		parent.setChild(child);

		@SuppressWarnings("unused")
		String mockObjectWithNullValues = child.getName();
		
		when(child.getName()).thenReturn("foobar");
		String mockObjectWithDesiredValues = child.getName();

		assertThat(mockObjectWithDesiredValues, containsString("foo"));
	}

	@Test
	public void verifyNoOfTimesMethodAreCalledTest() {
		Parent parent = new Parent();
		parent.setChild(child);

		parent.getChildName();
		verify(child, atLeastOnce()).getName();

		parent.getChildName();
		parent.getChildName();

		verify(child, times(3)).getName();
	}

	@Test
	public void injectChildMockInParentObectTest() {
		when(child.getName()).thenReturn("Toofan Singh");

		String result = parent.getChildName();

		assertThat(result, containsString("Toofan"));
	}

	@Test
	public void captureObjectAndUseTest() {
		Parent parent = new Parent();

		// mock using mock() api
		Child child = mock(Child.class);

		parent.setChild(child);
		parent.wuzzle();

		verify(child).display(captor.capture());
		assertThat(captor.getValue().getName(), containsString("Toofan"));
	}

	@Test(expected = RuntimeException.class)
	public void exceptionTest() {
		Parent parent = new Parent();
		Child child = mock(Child.class);
		parent.setChild(child);
		doThrow(new RuntimeException()).when(child).display(isA(Child.class));

		parent.wuzzle();
	}

	@Test
	public void consecutiveStubsTest() {
		Parent parent = new Parent();
		Child child = mock(Child.class);
		parent.setChild(child);
		when(child.getName()).thenReturn("First").thenReturn("Second").thenReturn("Third");

		assertEquals(parent.getChildName(), "First");
		assertEquals(parent.getChildName(), "Second");
		assertEquals(parent.getChildName(), "Third");
	}
	
	@Test
	public void onlyVerifyAPIAreCalledTest() {
		Parent parent = new Parent();
		Child child = mock(Child.class);
		parent.setChild(child);
		parent.getChildName();
		
		//This will throw exception
		//child.setName(null);

		verify(child).getName();
		verifyNoMoreInteractions(child);
		

	}
	
	@Test
	public void setInOrderTest() {
		Parent parent = new Parent();
		Child child = mock(Child.class);
		parent.setChild(child);

		// execute
		parent.setName("Ramu");
		parent.setName("Bhimu");

		InOrder inOrder = inOrder(child);
		inOrder.verify(child).setName("Ramu");
		inOrder.verify(child).setName("Bhimu");
	}
	
	@Test
	public void callRealAPITest() {
		Parent parent = mock(Parent.class);
		when(parent.parent()).thenCallRealMethod();

		assertThat(parent.parent(), is("parent!"));
	}
	
	/*
	 *  All the methods of a spy are real unless stubbed. 
	 *  All the methods of a mock are stubbed unless callRealMethod() is called
	 */
	@Test
	public void createSpyObect() {
		Parent parent = new Parent();
		Parent parentSpy = spy(parent);

		assertThat(parentSpy.parent(), is("parent!"));
		
		when(parentSpy.getChild()).thenReturn(new Child());

		assertThat(parentSpy.getChild(), not(nullValue()));
	}

}