package org.fundamenta;

import org.fundamenta.model.Deal;
import org.fundamenta.model.SalesManager;
import org.fundamenta.model.SalesManagerXMLDTO;

import java.io.*;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class PremiumCalculator {

    private final String tierAName = "A";
    private final String tierBName = "B";
    private final String tierCName = "C";

    private final String xmlOutputFilename = "premiums.xml";

    private final int tierAUpperLimit = 20000000;
    private final int tierBUpperLimit = 16000000;
    private final int tierCUpperLimit = 10000000;

    private List<String> rawTextLines = new ArrayList<>();

    private List<SalesManager> salesManagers = new ArrayList<>();

    private String inputFilename;

    public PremiumCalculator(String inputFilename) {
        this.inputFilename = inputFilename;
    }

    public void calculate() throws JAXBException {
        readData();
        collectSalesManagersAndDeals();
        calculateBasicPremiumsPerSalesManager();
        calculateExtraPremiumPerSalesManager();
        convertToXmlAndSave();
    }

    private void readData() {
        try {
            File file = new File(inputFilename);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
            String line;

            while ((line = reader.readLine()) != null) {
                this.rawTextLines.add(line);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void convertToXmlAndSave() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(SalesManagerXMLDTO.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter xmlWriter = new StringWriter();
        SalesManagerXMLDTO dto = new SalesManagerXMLDTO(salesManagers);

        marshaller.marshal(dto, xmlWriter);
        writeToFile(xmlWriter.toString());
    }

    private void writeToFile(String text) {
        try {
            File myObj = new File(xmlOutputFilename);
            myObj.createNewFile();
            System.out.println("File created: " + myObj.getName());

            FileWriter myWriter = new FileWriter(xmlOutputFilename);
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void collectSalesManagersAndDeals() {
        for (String line : rawTextLines) {
            String[] splittedLine = line.split("\\|");
            Optional<SalesManager> optionalSalesManager = salesManagers.stream()
                    .filter(manager -> manager.getName().equals(splittedLine[1]))
                    .findFirst();
            Deal actualDeal = new Deal(splittedLine[0], Integer.parseInt(splittedLine[2]));

            if (optionalSalesManager.isPresent()) {
                SalesManager sm = optionalSalesManager.get();
                sm.addDeal(actualDeal);
            } else {
                salesManagers.add(new SalesManager(splittedLine[1], actualDeal));
            }
        }
    }

    private void calculateBasicPremiumsPerSalesManager() {
        for (SalesManager salesManager : salesManagers) {
            int sumOfAllSales = salesManager.getDeals().stream().map(Deal::getPrice).reduce(0, Integer::sum);
            double basicPremium = sumOfAllSales * 0.01;
            salesManager.setBasicPremium(basicPremium);
        }
    }

    private void calculateSumOfSalesPerType(SalesManager salesManager) {
        int sumOfTypeASales = salesManager.getDeals().stream()
                .filter(deal -> deal.getType().equals(tierAName))
                .map(Deal::getPrice)
                .reduce(0, Integer::sum);

        int sumOfTypeBSales = salesManager.getDeals().stream()
                .filter(deal -> deal.getType().equals(tierBName))
                .map(Deal::getPrice)
                .reduce(0, Integer::sum);

        int sumOfTypeCSales = salesManager.getDeals().stream()
                .filter(deal -> deal.getType().equals(tierCName))
                .map(Deal::getPrice)
                .reduce(0, Integer::sum);

        salesManager.putSumOfSalesPerType(tierAName, sumOfTypeASales);
        salesManager.putSumOfSalesPerType(tierBName, sumOfTypeBSales);
        salesManager.putSumOfSalesPerType(tierCName, sumOfTypeCSales);
    }

    private void calculateExtraPremiumPerSalesManager() {
        for (SalesManager salesManager : salesManagers) {
            int extraPremium = 0;
            calculateSumOfSalesPerType(salesManager);

            if (salesManager.getSumOfSalesPerType().get(tierAName) >= 10000000 && salesManager.getSumOfSalesPerType().get(tierAName) < tierAUpperLimit) {
                extraPremium += 25000;
            } else if (salesManager.getSumOfSalesPerType().get(tierAName) > tierAUpperLimit) {
                extraPremium += 40000;
            }

            if (salesManager.getSumOfSalesPerType().get(tierBName) >= 8000000 && salesManager.getSumOfSalesPerType().get(tierBName) < tierBUpperLimit) {
                extraPremium += 30000;
            } else if (salesManager.getSumOfSalesPerType().get(tierBName) > tierBUpperLimit) {
                extraPremium += 50000;
            }

            if (salesManager.getSumOfSalesPerType().get(tierCName) >= 5000000 && salesManager.getSumOfSalesPerType().get(tierCName) < tierCUpperLimit) {
                extraPremium += 20000;
            } else if (salesManager.getSumOfSalesPerType().get(tierCName) > tierCUpperLimit) {
                extraPremium += 40000;
            }

            salesManager.setExtraPremium(extraPremium);
            salesManager.calculatePremium();
        }
    }
}
