package com.flutzpanel.domain;

import java.util.ArrayList;
import java.util.List;

public class Program {
    private List<Element> elements = new ArrayList<Element>();

    public void addElement(Element element) {
        this.elements.add(element);
    }

    public List<Element> getElements() {
        return this.elements;
    }

    public void removeElement(int index) {
        this.elements.remove(index);
    }

    public void setElement(int index, Element element) {
        elements.set(index, element);
    }



}