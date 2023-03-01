package org.fundamenta;

import javax.xml.bind.JAXBException;

public class Main {
    public static void main(String[] args) throws JAXBException {
        PremiumCalculator premiumCalculator = new PremiumCalculator(args[0]);
        premiumCalculator.calculate();
    }
}
