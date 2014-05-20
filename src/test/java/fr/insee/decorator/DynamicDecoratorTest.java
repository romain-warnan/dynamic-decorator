package fr.insee.decorator;

import java.util.concurrent.Executor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.insee.bean.Bean;
import fr.insee.bean.BeanDecorator;
import fr.insee.bean.ComparableDecorator;
import fr.insee.bean.ExecutorDecorator;

public class DynamicDecoratorTest {

	private Bean bean, decorated;
	
	@Before
	public void before() throws Exception {
		bean = new Bean();
		bean.setNom("nom");
		bean.setId(12345l);
	}
	
	@Test
	public void testSurcharge() throws Exception {
		decorated = DynamicDecorator.decorate(bean, BeanDecorator.class);
		Assert.assertEquals(bean.getNom() + " [décoré]", decorated.getNom());
	}
	
	@Test
	public void testSousJacent() throws Exception {
		decorated = DynamicDecorator.decorate(bean, BeanDecorator.class);
		Assert.assertEquals(bean.getId(), decorated.getId());
	}
	
	@Test
	public void testDecorateur() throws Exception {
		decorated = DynamicDecorator.decorate(bean, BeanDecorator.class);
		Assert.assertEquals("Dossier", decorated.getClass().getMethod("getGabarit").invoke(decorated));
	}
	
	@Test(expected = NoSuchMethodException.class)
	public void testDecorateurException() throws Exception {
		decorated = DynamicDecorator.decorate(bean, BeanDecorator.class);
		bean.getClass().getMethod("getGabarit").invoke(decorated);
	}
	
	@Test
	public void testInterface() throws Exception {
		decorated = DynamicDecorator.decorate(bean, BeanDecorator.class);
		((Runnable)decorated).run();
		Assert.assertTrue(true);
	}
	
	@Test
	public void testCastDecorator() throws Exception {
		ExecutorDecorator executorDecorator = new ExecutorDecorator();
		decorated = DynamicDecorator.decorate(bean, executorDecorator);
		((Executor)decorated).execute(new BeanDecorator(bean));
		Assert.assertTrue(true);
	}
	
	@Test(expected = ClassCastException.class)
	public void testCastDecoratorException() throws Exception {
		decorated = DynamicDecorator.decorate(bean, BeanDecorator.class);
		((Executor)decorated).execute(new BeanDecorator(bean));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testMultiCastDecorator() throws Exception {
		ComparableDecorator comparableDecorator = new ComparableDecorator(1);
		ExecutorDecorator executorDecorator = new ExecutorDecorator();
		decorated = DynamicDecorator.decorate(bean, executorDecorator, comparableDecorator);
		((Executor)decorated).execute(new BeanDecorator(bean));
		((Comparable<ComparableDecorator>)decorated).compareTo(new ComparableDecorator(2));
		Assert.assertTrue(true);
	}
	
	@Test
	public void testRandomDecorator() throws Exception {
		ComparableDecorator comparableDecorator = new ComparableDecorator(1);
		decorated = DynamicDecorator.decorate(bean, comparableDecorator);
		decorated.getClass().getMethod("getOrder").invoke(decorated);
		Assert.assertTrue(true);
	}
	
	@Test
	public void testRandomDecoratorWithArgs() throws Exception {
		ExecutorDecorator executorDecorator = new ExecutorDecorator();
		decorated = DynamicDecorator.decorate(bean, executorDecorator);
		decorated.getClass().getMethod("execute", Runnable.class).invoke(decorated, new BeanDecorator(bean));
		Assert.assertTrue(true);
	}
}
