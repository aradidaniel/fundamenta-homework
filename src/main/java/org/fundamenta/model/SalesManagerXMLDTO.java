package org.fundamenta.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "SalesManagers")
public class SalesManagerXMLDTO {
    private List<SalesManager> salesManagers = new ArrayList<>();

    public SalesManagerXMLDTO(List<SalesManager> salesManagers) {
        this.salesManagers = salesManagers;
    }

    public SalesManagerXMLDTO() {
    }

    @XmlElement(name = "SalesManager")
    public List<SalesManager> getSalesManagers() {
        return salesManagers;
    }
}
