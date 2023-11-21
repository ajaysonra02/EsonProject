/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core.autocomplete;

public abstract class ObjectToStringConverter {
    
    /**
     * Returns all possible <tt>String</tt> representations for a given item.
     * The default implementation wraps the method <tt>getPreferredStringForItem</tt>.
     * It returns an empty array, if the wrapped method returns <tt>null</tt>. Otherwise
     * it returns a one dimensional array containing the wrapped method's return value.
     *
     * @param item the item to convert
     * @return possible <tt>String</tt> representation for the given item.
     */
    public String[] getPossibleStringsForItem(Object item) {
        String preferred = getPreferredStringForItem(item);
        return preferred == null ? new String[0] : new String[] { preferred };
    }
    
    /**
     * Returns the preferred <tt>String</tt> representations for a given item.
     * @param item the item to convert
     * @return the preferred <tt>String</tt> representation for the given item.
     */
    public abstract String getPreferredStringForItem(Object item);
    
    /**
     * This field contains the default implementation, that returns <tt>item.toString()</tt>
     * for any item <tt>!=null</tt>. For any item <tt>==null</tt>, it returns <tt>null</tt> as well.
     */
    public static final ObjectToStringConverter DEFAULT_IMPLEMENTATION = new DefaultObjectToStringConverter();
    
    private static class DefaultObjectToStringConverter extends ObjectToStringConverter {
        @Override
        public String getPreferredStringForItem(Object item) {
            return item==null ? null : item.toString();
        }
    }    
}
