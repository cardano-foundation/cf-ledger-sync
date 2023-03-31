package org.cardanofoundation.configs.junitconfigs;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

public class ChangeDisplayName implements DisplayNameGenerator {
    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return testClass.getSimpleName().replace("_", " ");
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return nestedClass.getSimpleName();
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return changeTestMethodNameToPascalCase(testMethod.getName()).replace("_", " ");
    }

    private String changeTestMethodNameToPascalCase(String name) {
        char[] charArr = name.toCharArray();
        charArr[0] = Character.toLowerCase(charArr[0]);
        return new String(charArr);
    }
}
