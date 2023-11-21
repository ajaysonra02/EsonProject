/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core.autocomplete;

/**
 * Utility class for checking contracts.
 * 
 * @author Jeanette Winzenburg
 */
public class Contract {

    private Contract() {
        
    }

    /**
     * Tests the input parameter against null. If the input is 
     * an array, checks all of its elements as well. Returns the 
     * unchanged parameter if not null, throws a NullPointerException
     * otherwise. <p>
     * 
     * PENDING: type of exception? there are raging debates, some
     *   favour an IllegalArgument? <p>
     *   
     * PENDING: the implementation uses a unchecked type cast to an array.
     *   can we do better, how?
     *     
     * 
     * @param <T> the type of the input parameter
     * @param input the argument to check against null.
     * @param message the text of the exception if the argument is null
     * @return the input if not null
     * @throws NullPointerException if input is null
     */
    @SuppressWarnings("unchecked")
    public static <T> T asNotNull(T input, String message) {
        if (input == null) 
            throw new NullPointerException(message);
        
        if (input.getClass().isArray()) {
            if (!input.getClass().getComponentType().isPrimitive()) {
                T[] array = (T[]) input;
                for (int i = 0; i < array.length; i++) {
                    asNotNull(array[i], message);
                }
            }
        }
        
        return input;
    }
}
