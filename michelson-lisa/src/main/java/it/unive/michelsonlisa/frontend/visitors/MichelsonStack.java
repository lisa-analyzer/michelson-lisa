package it.unive.michelsonlisa.frontend.visitors;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import it.unive.lisa.program.cfg.statement.VariableRef;

public class MichelsonStack<E> extends Stack<E>{

	private int topProtectedSlots;

	@Override
	public synchronized E pop() {
        E       obj;
        int     len = size() - topProtectedSlots;

        obj = peek();
        removeElementAt(len - 1);

        return obj;
	}
	
	/**
	 * 
	 * @param n number of pop to perform
	 * @return a list of removed values from the stack ordered starting from the top 
	 */
	private synchronized List<E> pop(int n, boolean considerTopProtection) {

		if(considerTopProtection)
			return pop(n);
		
		List<E> c = new ArrayList<>();

		for(int i = n; i > 0; i--) {
			int len =  size() - 1;
		    E obj = peek(i, considerTopProtection);
		    removeElementAt(len);
		    c.add(obj);
		}
		return c;
	}
	
	
	public synchronized List<E> pop(int n) {
		List<E> c = new ArrayList<>();
		for(int i = 0; i < n; i++) {
			int top = size() - topProtectedSlots;
		    E obj = peek(top);
		    removeElementAt(top - 1);
		    c.add(obj);
		}
		return c;
	}
	
	@Override
	public synchronized E peek() {
        int len = size() - (topProtectedSlots);

        if (len == 0)
			if(size() == 0)
				throw new EmptyStackException();
			else
				throw new IllegalStateException("Theres no unprotected values to peek"); 
        return elementAt(len - 1);
	}
	
	private synchronized E peek(int n) {
		return peek(n,true);
	}
	
	private synchronized E peek(int n, boolean considerTopProtection) {
        int len = considerTopProtection ? size() - topProtectedSlots : size();

        if (len == 0)
			if(size() == 0)
				throw new EmptyStackException();
			else
				throw new IllegalStateException("Theres no unprotected values to peek"); 
        return elementAt(len - 1);
	}
	

	@Override
    public E push(E item) {
    	if(topProtectedSlots == 0)
    		addElement(item);
    	else {
    		List<E> tmp = pop(topProtectedSlots, false);
    		addElement(item);
    		for(int i = tmp.size()-1; i >= 0; i--)
    			addElement(tmp.get(i));
    	}
    		
        return item;
    }

	/*
	 * Set the number of slot to protect from the top
	 */
	public void setTopSlotProtection(int n) {
		if(n > size() || n < 0)
			throw new IllegalArgumentException("The value " + n + " cannot be greater than "+size()+" (the size of the stack) or negative");
		topProtectedSlots = n;
	}
	
	public int getTopProtectedSlots() {
		return topProtectedSlots;
	}
	
	
    public void add(int index, E element) {
        insertElementAt(element, index);
    }
    
	public static MichelsonStack<VariableRef> copy(MichelsonStack<VariableRef> michelsonStack) {
		MichelsonStack<VariableRef> copy = new MichelsonStack<>();
		for(int i=0; i < michelsonStack.size(); i++)
			copy.push(michelsonStack.get(i));
		
		copy.setTopSlotProtection(michelsonStack.getTopProtectedSlots());
		
		return copy;
	}

}
