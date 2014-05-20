package fr.insee.decorator;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.InvocationHandler;

class DecoratorsInvocationHandler<T> implements InvocationHandler {

	private T underlying;
	private Object[] decorators;
	
	public DecoratorsInvocationHandler(T underlying, Object... decorators) {
		super();
		this.underlying = underlying;
		this.decorators = decorators;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
		for (Object decorator : decorators) {
			try {
				Method decoratorMethod = decorator.getClass().getMethod(method.getName(), method.getParameterTypes());
				return decoratorMethod.invoke(decorator, args);
			}
			catch (NoSuchMethodException noSuchMethodException) {
				// Just try the next decorator
			}
		}
		return method.invoke(underlying, args);
	}
}
