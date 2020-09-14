package com.galaplat.comprehensive.bidding.utils;

/**
 * @Description: 工具类，可以用于封装两个对象用
 * @return
 * @throws
 * @date 2019/5/13 14:02
 */
public class Tuple3<K, V, T> {

    public K _1;
    public V _2;
    public T _3;

    public Tuple3(K k, V v, T t) {
        this._1 = k;
        this._2 = v;
        this._3 = t;
    }

    public static <K, V, T> Tuple3<K, V, T> createTuple(K k, V v, T t) {
        return new Tuple3<K, V, T>(k, v, t);
    }

    @Override
    public String toString() {
        return "Tuple [_1=" + _1 + ", _2="   +  ", _3=" + _3 + "]";
    }

}
