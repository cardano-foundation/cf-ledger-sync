package org.cardanofoundation.ledgersync.scheduler.util;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectorUtil {
  public static List<Field> getAllFields(Class<?> clazz) {
    List<Class<?>> classes = getAllSuperclasses(clazz);
    classes.add(clazz);
    return getAllFields(classes);
  }

  /**
   * As {@link #getAllFields(Class)} but acts on a list of {@link Class}s and uses only {@link
   * Class#getDeclaredFields()}.
   *
   * @param classes The list of classes to reflect on
   * @return The complete list of fields
   */
  private static List<Field> getAllFields(List<Class<?>> classes) {
    Set<Field> fields = new HashSet<>();
    for (Class<?> clazz : classes) {
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
    }

    return new ArrayList<>(fields);
  }

  /**
   * Return a List of super-classes for the given class.
   *
   * @param clazz the class to look up
   * @return the List of super-classes in order going up from this one
   */
  public static List<Class<?>> getAllSuperclasses(Class<?> clazz) {
    List<Class<?>> classes = new ArrayList<>();

    Class<?> superclass = clazz.getSuperclass();
    while (superclass != null) {
      classes.add(superclass);
      superclass = superclass.getSuperclass();
    }

    return classes;
  }
}
