package it.unive.michelsonlisa.cfg.statement;

import java.util.HashSet;
import java.util.Set;

import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;

public interface MichelsonBinaryNumericalOperation {

	public default Set<Type> resultType(SymbolicExpression left, SymbolicExpression right) {
		if (left.getRuntimeTypes(null).stream().noneMatch(Type::isNumericType)
				&& right.getRuntimeTypes(null).stream().noneMatch(Type::isNumericType))
			// if none have numeric types in them, we cannot really compute the
			// result
			return Set.of(Untyped.INSTANCE);

		Set<Type> result = new HashSet<>();
		for (Type t1 : left.getRuntimeTypes(null))
			if(t1.isNumericType() || t1.isUntyped())
				for (Type t2 : right.getRuntimeTypes(null))
					if(t2.isNumericType() || t2.isUntyped())
						if (t1.isUntyped() && t2.isUntyped())
							// we do not really consider this case,
							// it will fall back into the last corner case before return
							continue;
						else if (t1.isUntyped())
							result.add(t2);
						else if (t2.isUntyped())
							result.add(t1);
						else if (t1.canBeAssignedTo(t2))
							result.add(t2);
						else if (t2.canBeAssignedTo(t1))
							result.add(t1);
						else
							result.add(Untyped.INSTANCE);
		if (result.isEmpty())
			result.add(Untyped.INSTANCE);
		return result;
	}

	public default Type resultType(Type left, Type right) {
		if (!left.isNumericType() && !right.isNumericType())
			// if none have numeric types in them, we cannot really compute the
			// result
			return Untyped.INSTANCE;

		if (left.isUntyped() && right.isUntyped())
			return left;
		else if (left.isUntyped())
			return right;
		else if (right.isUntyped())
			return left;
		else if (left.canBeAssignedTo(right))
			return right;
		else if (right.canBeAssignedTo(left))
			return left;
		else
			return Untyped.INSTANCE;
	}
}
