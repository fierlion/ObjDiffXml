import java.util.HashSet;
import java.util.Set;

/**
 * @author Jon Skeet from
 *         http://stackoverflow.com/questions/709961/determining-if
 *         -an-object-is-of-primitive-type 
 *  modified by Ray Allan Foote
 */
public class WrapperTest {
	public static void main(String[] args) {
		System.out.println(isWrapperType(String.class));
		System.out.println(isWrapperType(Integer.class));
	}

	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

	public static boolean isWrapperType(Class<?> clazz) {
		return WRAPPER_TYPES.contains(clazz);
	}

	private static Set<Class<?>> getWrapperTypes() {
		Set<Class<?>> ret = new HashSet<>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		return ret;
	}
}