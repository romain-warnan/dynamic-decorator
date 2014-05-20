package fr.insee.decorator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;

public class DynamicDecorator<T> {

	private static final List<Method> OBJECT_METHODS = Arrays.asList(Object.class.getMethods());
	
	@SuppressWarnings("unchecked")
	public static<T> T decorate(T underlying, Class<? extends Decorator<T>> decoratorClass) throws Exception {
		
		Constructor<T> constructor = (Constructor<T>) decoratorClass.getDeclaredConstructor(underlying.getClass());
		Decorator<T> decorator = (Decorator<T>)constructor.newInstance(underlying);
		return decorate(underlying, decorator);
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T decorate(T underlying, Object... decorators) {	
		
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(underlying.getClass());
		enhancer.setCallback(new DecoratorsInvocationHandler<T>(underlying, decorators));
		InterfaceMaker interfaceMaker = new InterfaceMaker();
		for (Object decorator : decorators) {
			for (Method method : decorator.getClass().getMethods()) {
				if(!OBJECT_METHODS.contains(method)){
					interfaceMaker.add(method);
				}
			}
		}
		enhancer.setInterfaces(interfaces(decorators));
		return (T)enhancer.create();
	}
	
	private static Class<?> generatedInterface(Object... decorators) {
		
		InterfaceMaker interfaceMaker = new InterfaceMaker();
		for (Object decorator : decorators) {
			for (Method method : decorator.getClass().getMethods()) {
				if(!OBJECT_METHODS.contains(method)){
					interfaceMaker.add(method);
				}
			}
		}
		return interfaceMaker.create();
	}
	
	private static Class<?>[] interfaces(Object... decorators){
		
		Set<Class<?>> set = new HashSet<Class<?>>();
		set.add(generatedInterface(decorators));
		for (Object decorator : decorators) {
			Collections.addAll(set, decorator.getClass().getInterfaces());
		}
		Class<?>[] interfaces = new Class[set.size()];
		Iterator<Class<?>> iterator = set.iterator();
		for(int n = 0 ; n < set.size() ; n ++) {
			interfaces[n] = iterator.next();
		}
		return interfaces;
	}
}
