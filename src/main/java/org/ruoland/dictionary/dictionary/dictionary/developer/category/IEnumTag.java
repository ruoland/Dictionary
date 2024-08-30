package org.ruoland.dictionary.dictionary.dictionary.developer.category;

public interface IEnumTag<T> {

    public boolean containsKey(String keyword);

    public String name();

    public IEnumTag<T> value(String tags);
}
