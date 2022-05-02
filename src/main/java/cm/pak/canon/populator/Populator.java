package cm.pak.canon.populator;

public interface Populator<T , Y  > {
    Y  populate(T data) ;
}
