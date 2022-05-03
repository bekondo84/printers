package cm.pak.canon.populator;

public interface Populator<T , Y  > {
    Y  populate(T data) ;

    /**
     * Reverse populator strub method
     * @param data
     * @return
     */
   default T reversePopulate(Y data) {
       return null;
   }
}
